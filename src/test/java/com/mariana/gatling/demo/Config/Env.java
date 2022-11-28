package com.mariana.gatling.demo.Config;

public enum Env {

    LOCAL("local"),
    DAILY("daily"),
    PROD("prod");

    private final String value;

    public String getValue() {
        return value;
    }

    Env(String value) {
        this.value = value;
    }
}
