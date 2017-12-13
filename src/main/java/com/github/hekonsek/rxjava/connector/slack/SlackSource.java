package com.github.hekonsek.rxjava.connector.slack;

import com.github.hekonsek.rxjava.event.Event;
import com.google.common.collect.ImmutableMap;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import io.reactivex.Observable;

import java.util.Map;

import static com.github.hekonsek.rxjava.event.Events.event;
import static com.github.hekonsek.rxjava.event.Headers.ORIGINAL;
import static com.github.hekonsek.rxjava.event.Headers.RESPONSE_CALLBACK;
import static com.ullink.slack.simpleslackapi.impl.SlackSessionFactory.createWebSocketSlackSession;

public class SlackSource {

    private final String token;

    private final String channel;

    public SlackSource(String token, String channel) {
        this.token = token;
        this.channel = channel;
    }

    public Observable<Event<SlackMessage>> build() {
        return Observable.create(observable -> {
            try {
                SlackSession session = createWebSocketSlackSession(token);
                session.connect();
                SlackChannel channel = session.findChannelById(this.channel);
                SlackResponseCallback responseCallback = new SlackResponseCallback(session, channel);
                session.addMessagePostedListener((slackMessagePosted, slackSession) -> {
                    Map<String, Object> headers = ImmutableMap.of(
                            ORIGINAL, slackMessagePosted,
                            RESPONSE_CALLBACK, responseCallback
                    );
                    if(!slackMessagePosted.getUser().isBot() && slackMessagePosted.getChannel().getId().equals(this.channel)) {
                        observable.onNext(event(headers, new SlackMessage(slackMessagePosted.getMessageContent())));
                    }
                });
            } catch (Throwable t) {
                observable.onError(t);
            }
        });
    }

}
