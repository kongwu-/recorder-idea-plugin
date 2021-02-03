package com.github.kongwu.recorder.plugin.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.github.kongwu.recorder.common.model.ThreadNode;

public class JacksonTest {
    public static void main(String[] args) throws JsonProcessingException {
        ThreadNode threadNode = new ThreadNode();
//        System.out.println(objectMapper.writeValueAsString(threadNode));
    }
}
