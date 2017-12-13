package com.github.hekonsek.rxjava.connector.slack;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SlackResponseCallbackTest {

    @Test
    public void shouldCreateTableWithTwoFields() {
        // Given
        SlackChannel channel = mock(SlackChannel.class);
        SlackSession session = mock(SlackSession.class);
        ArgumentCaptor<SlackAttachment> attachment = ArgumentCaptor.forClass(SlackAttachment.class);
        given(session.sendMessage(eq(channel), anyString(), attachment.capture())).willReturn(null);
        SlackTable table = new SlackTable("title", asList("foo", "bar"), asList(asList("foo1", "bar1"),asList("foo2", "bar2")));

        // When
        new SlackResponseCallback(session, channel).respond(table);

        // Then
        assertThat(attachment.getValue().getFields()).hasSize(2);
    }

    @Test
    public void shouldCreateTableWithTwoColumns() {
        // Given
        SlackChannel channel = mock(SlackChannel.class);
        SlackSession session = mock(SlackSession.class);
        ArgumentCaptor<SlackAttachment> attachment = ArgumentCaptor.forClass(SlackAttachment.class);
        given(session.sendMessage(eq(channel), anyString(), attachment.capture())).willReturn(null);
        SlackTable table = new SlackTable("title", asList("foo", "bar"), asList(asList("foo1", "bar1"),asList("foo2", "bar2")));

        // When
        new SlackResponseCallback(session, channel).respond(table);

        // Then
        assertThat(attachment.getValue().getFields().get(0).getTitle()).isEqualTo("foo");
        assertThat(attachment.getValue().getFields().get(1).getTitle()).isEqualTo("bar");
    }

}
