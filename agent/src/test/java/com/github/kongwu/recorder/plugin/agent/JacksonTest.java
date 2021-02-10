//package com.github.kongwu.recorder.plugin.agent;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
//import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
//import com.github.kongwu.recorder.common.model.ThreadNode;
//import com.github.kongwu.recorder.common.model.TraceNode;
//
//public class JacksonTest {
//    public static void main(String[] args) throws JsonProcessingException {
//        ThreadNode threadNode = new ThreadNode();
//
//        ObjectMapper objectMapper = createObjectMapper();
//
//        String s = objectMapper.writeValueAsString(threadNode);
//        System.out.println(s);
//
//        TraceNode traceNode = objectMapper.readValue(s, TraceNode.class);
//
//        System.out.println(traceNode);
//    }
//
//    public static ObjectMapper createObjectMapper(){
//        ObjectMapper objectMapper = new ObjectMapper();
//        BasicPolymorphicTypeValidator validator = BasicPolymorphicTypeValidator
//                .builder()
//                .allowIfBaseType(Object.class)
//                .build();
////        objectMapper.activateDefaultTyping(ptv); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE
//        objectMapper.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL);
//
//        return objectMapper;
//    }
//}
