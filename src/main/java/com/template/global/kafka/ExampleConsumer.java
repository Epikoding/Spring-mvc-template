package com.template.global.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

//@Configuration
@RequiredArgsConstructor
@Slf4j
public class ExampleConsumer {
    private final StreamBridge streamBridge;

    //Consumer, Function, Supplier 3개가 트리거로 동작 한다.
    @Bean
    public Consumer goodsUpdate() {
        return (o) -> {
            log.info("1234");
            //받을때는
            //Dto dto = new Jackson2JsonObjectMapper().fromJson(o, DTO.class) 하면 자동 컨버팅 된다

            //토픽에 메세지를 보낼때는 streamBridge.send(토픽명-out-0 + 바디 값)
//            streamBridge.send("goodsUpdate-out-0", Map.of("123","456"));
        };
    }
}
