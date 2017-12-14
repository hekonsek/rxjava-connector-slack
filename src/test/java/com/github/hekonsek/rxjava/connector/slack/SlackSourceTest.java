package com.github.hekonsek.rxjava.connector.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.Test;

import java.net.ConnectException;

import static com.github.hekonsek.rxjava.connector.slack.SlackSource.slackSource;
import static com.github.hekonsek.rxjava.event.Events.event;
import static io.reactivex.Observable.just;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SlackSourceTest {

    SlackSession session = mock(SlackSession.class);

    @Test
    public void shouldHandleSlackConnectionError() {
        String text = slackSource("token", "channel").build().
                onErrorResumeNext(e -> {
                    assertThat(e).isInstanceOf(ConnectException.class);
                    return just(event(new SlackMessage("foo")));
                }).blockingFirst().payload().text();
        assertThat(text).isEqualTo("foo");
    }

    @Test
    public void shouldRegisterPostedMessageHandler() {
        new SlackSource(session, "channel").build().subscribe();
        verify(session).addMessagePostedListener(any());
    }

}