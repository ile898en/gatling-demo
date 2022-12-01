package com.mariana.gatling.demo.Config;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.netty.handler.codec.http.HttpHeaderNames;

import java.util.Map;

public class SystemProperties {

    /**
     * 配置要压测endpoint的主机名（域名）
     */
    public static final String BASE_URL = "https://api.hhocool.com";

    /**
     * 配置要执行的Simulation类路径，例如：
     *      com.mariana.gatling.demo.Simulations.SampleSimulation
     */
    public static final String SIMULATION_NAME               = getSimulationName();

    /**
     * 配置要压到多少QPS
     */
    public static final Integer QPS                          = getQPS();

    /**
     * 配置压测持续多长时间，单位是秒(s)
     */
    public static final Integer DURATION                     = getDuration();

    /**
     * 默认的HttpProtocol，其中包含默认的请求头、base url、是否复用连接等等
     */
    public static final HttpProtocolBuilder DEFAULT_PROTOCOL = defaultProtocol();

    private static String getSimulationName() {
        String name = System.getProperty("gatling.simulationClass", "");
        if (Strings.isNullOrEmpty(name))
            throw new RuntimeException("<gatling.simulationClass> is required.");
        return name;
    }

    private static Integer getQPS() {
        int qps = Integer.parseInt(System.getProperty("qps", "1"));
        if (qps <= 0)
            throw new RuntimeException("<qps> invalid.");
        return qps;
    }

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
