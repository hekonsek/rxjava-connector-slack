# RxJava Slack connector

[![Version](https://img.shields.io/badge/RxJava%20Connector%20Slack-0.1-blue.svg)](https://github.com/hekonsek/rxjava-connector-slack/releases)
[![Build](https://api.travis-ci.org/hekonsek/rxjava-connector-slack.svg)](https://travis-ci.org/hekonsek/rxjava-connector-slack)
[![Coverage](https://sonarcloud.io/api/badges/measure?key=com.github.hekonsek%3Arxjava-connector-slack&metric=coverage)](https://sonarcloud.io/component_measures?id=com.github.hekonsek%3Arxjava-connector-slack&metric=coverage)

Connector bridging events generated by Slack bot with [RxJava events](https://github.com/hekonsek/rxjava-event).

## Installation

In order to start using Vert.x Pipes add the following dependency to your Maven project:

    <dependency>
      <groupId>com.github.hekonsek</groupId>
      <artifactId>rxjava-connector-slack</artifactId>
      <version>0.1</version>
    </dependency>

## Usage

This is how you can create "echo" simple Slack bot responding to all the messages posted to the channel using 
texts of those arriving messages:

```
String botToken = ...;
String channelId = ...;
new SlackSource(botToken, channelId).build().
  subscribe(event -> responseCallback(event).get().respond(event.payload().text()));
```

## License

This project is distributed under Apache 2.0 license.