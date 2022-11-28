package com.mariana.gatling.demo.Simulations;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

public class SampleSimulation extends AbstractSimulation{

    @Override
    String scenarioName() {
        return "query ip info";
    }

    @Override
    HttpRequestActionBuilder requestBuilder() {
        return HttpDsl.http("ipinfo.io")
                .get("https://ipinfo.io/json")
                .check(HttpDsl.status().is(200));
    }

    @Override
    FeederBuilder<Object> feederBuilder() {
        return null;
    }
}
