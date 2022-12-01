package com.mariana.gatling.demo.Simulations;

import com.google.common.collect.Lists;
import io.gatling.javaapi.core.CoreDsl;
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

        // 你可以在此处串联多个不同的请求
        // 你也可以按照先后顺序，处理前面请求返回的json结构，解析出某个字段的值缓存到Session对象中供后面的请求使用
        HttpRequestActionBuilder ipInfo1 = HttpDsl.http("ipinfo.io")
                .get("https://ipinfo.io/json")
                // 下面一行中，我们通过jsonpath解析response body拿到city字段的值存储在session中，
                // key为"CITY"，后面我们可以直接在session中拿到这个值（第32行）
                .check(CoreDsl.jsonPath("$.city").ofString().saveAs("CITY"))
                .check(HttpDsl.status().is(200));

        HttpRequestActionBuilder ipInfo2 = HttpDsl.http("ipinfo.io")
                .get("https://ipinfo.io/json")
                .header("X-city", session -> session.getString("CITY"))
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
