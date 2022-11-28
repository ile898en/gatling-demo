package com.mariana.gatling.demo.Config;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.netty.handler.codec.http.HttpHeaderNames;

import java.util.Map;

public class SystemProperties {

    private static final String BASE_URL = "https://api.hhocool.com";

    public static final String SIMULATION_NAME               = getSimulationName();
    public static final Integer QPS                          = getQPS();
    public static final Integer DURATION                     = getDuration();
    public static final HttpProtocolBuilder DEFAULT_PROTOCOL = defaultProtocol();

    /**
     * choose which simulation class will be processed
     */
    private static String getSimulationName() {
        String name = System.getProperty("gatling.simulationClass", "");
        if (Strings.isNullOrEmpty(name))
            throw new RuntimeException("<gatling.simulationClass> is required.");
        return name;
    }

    /**
     * QPS
     */
    private static Integer getQPS() {
        int qps = Integer.parseInt(System.getProperty("qps", "1"));
        if (qps <= 0)
            throw new RuntimeException("<qps> invalid.");
        return qps;
    }

    /**
     * how long will the performance test last (seconds)
     */
    private static Integer getDuration() {
        int duration = Integer.parseInt(System.getProperty("duration", "1"));
        if (duration <= 0)
            throw new RuntimeException("<duration> invalid.");
        return duration;
    }

    /**
     * add custom http request headers here
     */
    private static Map<String, String> defaultHeaders() {
        return ImmutableMap.of(
                HttpHeaderNames.CONTENT_TYPE.toString(), "application/json",
                HttpHeaderNames.ACCEPT.toString(), "*/*",
                HttpHeaderNames.USER_AGENT.toString(), "performance_test"
//                "__pt_", "1"
        );
    }

    /**
     * the default http protocol
     * @see <a href="https://gatling.io/docs/gatling/reference/current/http/protocol/">Gatling Documentation</a>
     */
    private static HttpProtocolBuilder defaultProtocol() {
        return HttpDsl.http
                .baseUrl(BASE_URL)
                .warmUp(BASE_URL)
                .headers(defaultHeaders())
                .shareConnections();
    }

    private static void checkNon(Object o) {

    }

}
