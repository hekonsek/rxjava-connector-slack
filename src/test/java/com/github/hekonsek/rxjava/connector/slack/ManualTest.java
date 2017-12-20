/**
 * Licensed to the RxJava Connector HTTP under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hekonsek.rxjava.connector.slack;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.github.hekonsek.rxjava.connector.slack.SlackSource.slackSource;
import static com.github.hekonsek.rxjava.event.Headers.requiredReplyHandler;
import static java.util.Arrays.asList;
import static org.junit.Assume.assumeTrue;

@Ignore
@RunWith(VertxUnitRunner.class)
public class ManualTest {

    static String token = System.getenv("SLACK_TOKEN");

    static String channel = System.getenv("SLACK_CHANNEL");

    @BeforeClass
    public static void beforeClass() {
        assumeTrue(token != null && channel != null);
    }

    @Test
    public void shouldGenerateTable(TestContext testContext) {
        testContext.async();
        slackSource(token, channel).build().
                subscribe(e -> {
                    requiredReplyHandler(e).reply(
                            new SlackTable("Response you wanted:", ImmutableList.of("foo", "bar"), ImmutableList.of(
                                    ImmutableList.of("a", "b"),
                                    ImmutableList.of("c", "d"),
                                    ImmutableList.of("e", "f")
                            ))
                    );
                    requiredReplyHandler(e).reply(
                            new SlackTableList("title", ImmutableMap.of(
                                    "item1", asList("foo", "bar"),
                                    "item2", asList("baz")
                            ))
                    );
                });
    }

}