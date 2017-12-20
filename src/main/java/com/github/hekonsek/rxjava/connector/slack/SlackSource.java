/**
 * Licensed to the RxJava Connector Slack under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hekonsek.rxjava.connector.slack;

import com.github.hekonsek.rxjava.event.Event;
import com.google.common.collect.ImmutableMap;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import io.reactivex.Observable;

import java.util.Map;

import static com.github.hekonsek.rxjava.event.Events.event;
import static com.github.hekonsek.rxjava.event.Headers.ORIGINAL;
import static com.github.hekonsek.rxjava.event.Headers.REPLY_CALLBACK;
import static com.ullink.slack.simpleslackapi.impl.SlackSessionFactory.createWebSocketSlackSession;

public class SlackSource {

    private final SlackSession session;

    private final String channel;

    public SlackSource(SlackSession session, String channel) {
        this.session = session;
        this.channel = channel;
    }

    public static SlackSource slackSource(String token, String channel) {
        SlackSession session = createWebSocketSlackSession(token);
        return new SlackSource(session, channel);
    }

    public Observable<Event<SlackMessage>> build() {
        return Observable.create(observable -> {
            try {
                session.connect();
                SlackChannel channel = session.findChannelById(this.channel);
                SlackReplyHandler responseCallback = new SlackReplyHandler(session, channel);
                session.addMessagePostedListener((slackMessagePosted, slackSession) -> {
                    Map<String, Object> headers = ImmutableMap.of(
                            ORIGINAL, slackMessagePosted,
                            REPLY_CALLBACK, responseCallback
                    );
                    if(!slackMessagePosted.getUser().isBot() && slackMessagePosted.getChannel().getId().equals(this.channel)) {
                        observable.onNext(event(headers, new SlackMessage(slackMessagePosted.getMessageContent())));
                    }
                });
            } catch (Exception e) {
                observable.onError(e);
            }
        });
    }

}
