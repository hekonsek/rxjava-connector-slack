package com.github.hekonsek.rxjava.connector.slack;

import com.github.hekonsek.rxjava.event.ResponseCallback;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;

public class SlackResponseCallback implements ResponseCallback {

    private final SlackSession session;

    private final SlackChannel channel;

    public SlackResponseCallback(SlackSession session, SlackChannel channel) {
        this.session = session;
        this.channel = channel;
    }

    @Override public void respond(Object response) {
        session.sendMessage(channel, response.toString());
    }

}
