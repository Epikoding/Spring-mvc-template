package com.template.global.util;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Duration;

public class RateLimitingFilter implements Filter {

    private static Bucket bucket;

    public RateLimitingFilter() {
                bucket = Bucket.builder()
                .addLimit(Bandwidth.simple(100, Duration.ofSeconds(10)))
                .build();

//        // Every 10 seconds, the bucket will be refilled with 2 tokens, but it will never exceed its maximum capacity of 3 tokens.
//        Refill refill = Refill.intervally(50, Duration.ofSeconds(1));
//
//        // Initially, the bucket starts with a capacity of 3 tokens (as defined by Bandwidth.classic(3, refill)).
//        // Each time a request is made (or an action requiring a token), a token is consumed from the bucket.
//        // If there are no tokens left in the bucket, further requests/actions are either denied or queued until the bucket is refilled with more tokens.
//        Bandwidth limit = Bandwidth.classic(10, refill);
//
//        // up to 3 requests per the refill cycle, with additional tokens being replenished every 10 seconds.
//        bucket = Bucket.builder()
//                .addLimit(limit)
//                .build();
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            handleRateLimitExceeded((HttpServletRequest) request, (HttpServletResponse) response);
            // since RateLimitExceededException is thrown from a servlet filter (RateLimitingFilter), it does not naturally propagate to the Spring MVC layer where @ControllerAdvice operates.
            // Filters are at a lower level in the request processing chain, outside of the Spring MVC dispatching system.
            // To have the @ExceptionHandler in @ControllerAdvice handle an exception thrown from a filter like RateLimitExceededException, you need to ensure the exception is propagated to the Spring MVC layer.
        }
    }

    private void handleRateLimitExceeded(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().write("Too Many Requests");
    }
}
