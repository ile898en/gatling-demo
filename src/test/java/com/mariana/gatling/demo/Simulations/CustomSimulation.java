package com.mariana.gatling.demo.Simulations;

import com.google.common.collect.Lists;
import com.mariana.gatling.demo.Config.SystemProperties;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;

/**
 * 在项目根目录下执行以下命令即可开始运行本类，qps参数控制并发数，duration参数控制持续时间，单位是秒
 * <pre>
 *     sudo mvn -e gatling:test \
 *          -Dlogback.level=DEBUG \
 *          -Dgatling.simulationClass=com.mariana.gatling.demo.Simulations.CustomSimulation \
 *          -Dqps=1 \
 *          -Dduration=1
 * </pre>
 */
public class CustomSimulation extends Simulation {

    {
        setUp(
                CoreDsl.scenario("压测场景名称")
                        .feed(CoreDsl.listFeeder(Lists.newArrayList()))
                        .exec(
                                HttpDsl.http("http请求名称/描述")
                                        .get("https://ipinfo.io/json")
                                        // 下面一行中，我们通过jsonpath解析response body拿到city字段的值存储在session中，
                                        // key为"CITY"，后面我们可以直接在session中拿到这个值
                                        .check(CoreDsl.jsonPath("$.city").ofString().saveAs("CITY"))
                                        .check(HttpDsl.status().is(200))
                        )
                        .exec(session -> {
                            System.out.println(session.getString("CITY"));
                            return session;
                        })
                        .exec(
                                HttpDsl.http("http请求名称/描述")
                                        .get("https://ipinfo.io/json")
                                        .header("X-city", session -> session.getString("CITY"))
                                        .check(CoreDsl.jsonPath("$.city").ofString().saveAs("CITY"))
                                        .check(HttpDsl.status().is(200))
                        )
                        .injectOpen(
                                CoreDsl.constantUsersPerSec(SystemProperties.QPS)
                                        .during(SystemProperties.DURATION)
                        )
//                        .protocols()
        );
    }


}
