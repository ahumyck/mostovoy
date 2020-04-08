package com.mostovoy_company;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
