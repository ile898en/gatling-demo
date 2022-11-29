package com.mariana.gatling.demo.Simulations;

import com.google.common.collect.Lists;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import java.util.List;

public class SampleSimulation extends AbstractSimulation{

    @Override
    String scenarioName() {
        return "query ip info";
    }

    @Override
    List<HttpRequestActionBuilder> requestBuilders() {

        HttpRequestActionBuilder ipInfo1 = HttpDsl.http("ipinfo.io")
                .get("https://ipinfo.io/json")
                .check(HttpDsl.status().is(200));

        HttpRequestActionBuilder ipInfo2 = HttpDsl.http("ipinfo.io")
                .get("https://ipinfo.io/json")
                .check(HttpDsl.status().is(200));

        return Lists.newArrayList(
                ipInfo1, ipInfo2
        );
    }

    @Override
    FeederBuilder<Object> feederBuilder() {
        return null;
    }
}
