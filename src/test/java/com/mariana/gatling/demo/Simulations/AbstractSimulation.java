package com.mariana.gatling.demo.Simulations;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.mariana.gatling.demo.Config.SystemProperties;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.function.Function;

public abstract class AbstractSimulation extends Simulation {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSimulation.class);

    // 0时区的SimpleDateFormat，格式为 yyyyMMddHHmmssSSS
    private static final SimpleDateFormat ZERO_TIMEZONE_DATE_FORMAT =
            DateUtil.newSimpleFormat(DatePattern.PURE_DATETIME_MS_PATTERN, null, TimeZone.getTimeZone("GMT+00:00"));

    private static final String BASE_DIR = "/home/admin/app/aquaman-pt/aquaman-pt.gatling/target/gatling";

    private long starsAt;

    {
        setUp(
                CoreDsl.scenario(this.scenarioName())
                        .feed(this.feederBuilder().circular())
                        .exec(this.requestBuilder())
                        .injectOpen(this.stepBuilder())
                        .protocols(SystemProperties.DEFAULT_PROTOCOL)
        );
    }

    /**
     * The performance test scenario's name. Cannot be empty.
     * eg:
     *  return "用户刷Feeds流";
     */
    abstract String scenarioName();

    /**
     * Build HttpRequest by {@link io.gatling.javaapi.http.HttpDsl}
     * @see <a href="https://gatling.io/docs/gatling/reference/current/http/request/">https://gatling.io/docs/gatling/reference/current/http/request/</a>
     */
    abstract HttpRequestActionBuilder requestBuilder();

    /**
     * <p>Make performance test data({@link FeederBuilder})</p>
     * You can get {@link FeederBuilder} from:
     * <ul>
     *     <li>
     *         A List<{@code Map}<{@code String}, {@code Object}> object:
     *         <pre>
     *             return CoreDsl.listFeeder(
     *                  Lists.newArrayList(
     *                      ImmutableMap..of("token", "token-value-1"),
     *                      ImmutableMap..of("token", "token-value-2"),
     *                      ImmutableMap..of("token", "token-value-3")
     *                      ...
     *                  )
     *             ).circular();
     *         </pre>
     *     </li>
     *     <li>
     *         <p>A json file like <span style="color:#78dce8">resources/data/example_data.json</span></p><br>
     *         then you can use {@link CoreDsl#jsonFile(String)} like this:
     *         <pre>
     *             return CoreDsl.jsonFile("data/example_data.json")
     *                           .circular();
     *         </pre>
     *     </li>
     * </ul>
     *
     * <p>
     *     The {@link FeederBuilder} will cache in a {@link io.gatling.javaapi.core.Session} object,
     *      you can use the Session object during per HttpRequest (per request will load only one line record from FeederBuilder). For example: <br>
     *      <pre>
     *          // {@link io.gatling.javaapi.http.HttpDsl#header(Function)}
     *          HttPDsl.http("xxx")
     *              .get("xxx")
     *              .header(
     *                  "Authorization", session -> {
     *                      String token = session.getString("token");
     *                      return Strings.nullToEmpty(token);
     *                  }
     *              );
     *      </pre>
     * </p>
     *
     */
    abstract FeederBuilder<Object> feederBuilder();

    /**
     * The hook function execute before performance test started.
     */
    @Override
    public void before() {

    }

    /**
     * The hook function execute after performance test finished.
     * <p>
     *     The report folder will be generated in 'target/gatling/' after performance test finished，<br>
     *     the folder will like:
     *     <pre>
     *         - target
     *              - gatling
     *
     *     </pre>
     * </p>
     */
    @Override
    public void after() {

    }

    private OpenInjectionStep stepBuilder() {
        return CoreDsl.constantUsersPerSec(SystemProperties.QPS)
                .during(SystemProperties.DURATION);
    }

}
