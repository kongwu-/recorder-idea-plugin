package com.github.kongwu.recorder.common.logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

/**
 * 一个迷你的日志工具，供agent使用
 *
 * 因为agent需要集成实际应用，所以可能存在各种日志库的问题，索性不兼容了
 * agent日志直接输出至控制台
 */
public class Logger {
    private static final Map<Class,Logger> loggerCache = new HashMap<>();

    private String name;

    public static Logger getLogger(Class clazz){
        return loggerCache.computeIfAbsent(clazz,Logger::new);
    }

    public void info(String msg,Object... args){
        append(msg,args);
    }

    public Logger(String name) {
        this.name = name;
    }

    public Logger(Class clazz) {
        this.name = clazz.getName();
    }

    private void append(String msg, Object ...args){
        StringBuilder sb = new StringBuilder();
        sb.append(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.format(new Date()));
        sb.append(" ");
        sb.append(name);
        sb.append(" - ");
        sb.append(new Formatter().format(msg, args));
        System.err.println(sb.toString());
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(ArrayList.class);
        logger.info("hhh");
    }

    public void error(String msg, Throwable e) {
        String stackTrace = ExceptionUtils.getStackTrace(e);
        append(msg+" : "+stackTrace);
    }
}
