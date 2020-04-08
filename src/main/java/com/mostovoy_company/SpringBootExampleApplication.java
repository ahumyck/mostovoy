package com.mostovoy_company;

import com.mostovoy_company.kafka.dto.Message;
import com.mostovoy_company.kafka.dto.Response;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringBootExampleApplication {

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

//    @Bean
//    public RecordMessageConverter converter() {
//        StringJsonMessageConverter converter = new StringJsonMessageConverter();
//        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
//        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
//        typeMapper.addTrustedPackages("com");
//        Map<String, Class<?>> mappings = new HashMap<>();
//        mappings.put("message", Message.class);
//        mappings.put("response", Response.class);
//        typeMapper.setIdClassMapping(mappings);
//        converter.setTypeMapper(typeMapper);
//        return converter;
//    }
}
