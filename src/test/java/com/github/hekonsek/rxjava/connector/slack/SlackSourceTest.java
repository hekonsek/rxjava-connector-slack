/**
 * Licensed to the RxJava Connector HTTP under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hekonsek.rxjava.connector.slack;

import com.github.hekonsek.rxjava.event.Headers;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assume.assumeTrue;

@RunWith(VertxUnitRunner.class)
public class SlackSourceTest {

    static String token = System.getenv("SLACK_TOKEN");

    static String channel = System.getenv("SLACK_CHANNEL");

    @BeforeClass
    public static void beforeClass() {
        assumeTrue(token != null && channel != null);
    }

    @Test
    public void shouldSendEcho(TestContext testContext) {
        testContext.async();
        new SlackSource(token, channel).build().
                subscribe( e -> Headers.responseCallback(e).get().respond(e.payload().text()));
    }

}