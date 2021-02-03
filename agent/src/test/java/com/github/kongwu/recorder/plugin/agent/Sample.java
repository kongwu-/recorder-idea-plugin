package com.github.kongwu.recorder.plugin.agent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;

public class Sample {
    public String randomStr(int length){
        NumberUtils.max(length);
        return doRandomStr(length);
    }

    public String doRandomStr(int length){
        return RandomStringUtils.randomAscii(length);
    }
}
