package com.github.hekonsek.rxjava.connector.slack;

public class SlackMessage {

    private final String text;

    public SlackMessage(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

}
