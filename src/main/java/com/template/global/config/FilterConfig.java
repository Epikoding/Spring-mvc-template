package com.template.global.config;


import com.template.global.util.RateLimitingFilter;
import com.template.global.util.RequestLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> loggingFilter(){
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLoggingFilter());
        registrationBean.setOrder(1); // set lower order for higher precedence
        return registrationBean;
    }

    @Bean
    @Profile("!local")
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter(){
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter());
        registrationBean.setOrder(2); // set higher order for lower precedence
        // other configurations
        return registrationBean;
    }
}
