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
                        // feed方法用于加载后续步骤中需要消费的压测数据，具体解释可以查看AbstractSimulation类中关于这部分的注释
                        // 这份数据将被缓存在Session对象中，是一个List<Map<String, Obj>>对象中的一行数据：
                        // 每一个并发（user）进入场景后会按一定的策略消费消费List中的一行数据
                        // 因此在后面的步骤中，我们可以直接用session.get("key")获取到value
                        .feed(CoreDsl.listFeeder(Lists.newArrayList()))
                        // exec代表一个步骤，多个步骤串联构成一个场景。例如"新用户场景"，可能涉及步骤：浏览首页 -> 注册 -> 登陆 等等
                        // 在exec之后其实还可以调用pause()，repeat()等方法来控制该步骤暂停、重复执行等操作
                        // 一个Session对象的作用范围：同一并发（user）在同一个场景下的不同步骤（exec）中
                        // 例如我在第一个exec中往session中塞入一个值，我可以在后面的exec中拿到这个值：
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
                        // injectOpen方法用来控制压力，这部分建议查看官方文档
                        .injectOpen(
                                CoreDsl.constantUsersPerSec(SystemProperties.QPS)
                                        .during(SystemProperties.DURATION)
                        )
                        // protocols方法用来为过程中所有的http请求定义一套统一的配置
                        // 假如你的HttpRequest中是这样定义的：
                        // HttpDsl.get("/user/1") ... 没有定义域名或base url，
                        // 你就可以用protocols方法配置统一的一些配置，详情查看SystemProperties类中相关代码
//                        .protocols()
        );
    }


}
