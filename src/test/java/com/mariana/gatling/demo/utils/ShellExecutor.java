package com.mariana.gatling.demo.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ShellExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ShellExecutor.class);

    public static String execute(String[] command) {
        try {
            final Process process = Runtime.getRuntime().exec(command);
            final String output = parseProcessInputStream(process.getInputStream());
            final int exitValue = process.waitFor();
            if (exitValue != 0) {
                final String  errorInfo = parseProcessInputStream(process.getErrorStream());
                logger.error("ShellExecutor: execute command {} error. existValue {}, errorInfo {}, output {}",
                        JSON.toJSONString(command), exitValue, errorInfo, output);
                throw new RuntimeException("ShellExecutor: execute command error. existValue is " + exitValue + " errorInfo is : " + errorInfo + ", output is " + output);
            }
            return output;
        } catch (Exception e) {
            logger.error("ShellExecutor: execute command {} error.", JSON.toJSONString(command), e);
            throw new RuntimeException("ShellExecutor: execute command error." + e.getMessage());
        }
    }

    private static String parseProcessInputStream(InputStream inputStream) {
        return new BufferedReader(
                new InputStreamReader(inputStream)).lines().collect(Collectors.joining()
        );
    }


}
