package com.fizzblock.wechat.util.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日志工具包
 */
public class LogUtils {

    static private Logger logger = LogManager.getLogger("testTime");


    public static void trace(String object) {
        logger.trace(object);
    }

    public static void debug(String object) {
        logger.debug(object);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String object) {
        logger.warn(object);
    }

    public static void error(String object) {
        logger.error(object);
    }
    
    public static void error(String msg,Throwable ex) {
    	logger.error(msg, ex);;
    }

    public static void fatal(Object object) {
        logger.fatal(object);
    }

}
