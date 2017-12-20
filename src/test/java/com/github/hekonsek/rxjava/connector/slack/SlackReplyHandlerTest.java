package com.github.hekonsek.rxjava.connector.slack;

import com.google.common.collect.ImmutableMap;
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

public class SlackReplyHandlerTest {

    SlackChannel channel = mock(SlackChannel.class);

    SlackSession session = mock(SlackSession.class);

    @Test
    public void shouldCreateTableWithTwoFields() {
        // Given
        ArgumentCaptor<SlackAttachment> attachment = ArgumentCaptor.forClass(SlackAttachment.class);
        given(session.sendMessage(eq(channel), anyString(), attachment.capture())).willReturn(null);
        SlackTable table = new SlackTable("title", asList("foo", "bar"), asList(asList("foo1", "bar1"),asList("foo2", "bar2")));

        // When
        new SlackReplyHandler(session, channel).reply(table);

        // Then
        assertThat(attachment.getValue().getFields()).hasSize(2);
    }

    @Test
    public void shouldCreateTableWithTwoColumns() {
        // Given
        ArgumentCaptor<SlackAttachment> attachment = ArgumentCaptor.forClass(SlackAttachment.class);
        given(session.sendMessage(eq(channel), anyString(), attachment.capture())).willReturn(null);
        SlackTable table = new SlackTable("title", asList("foo", "bar"), asList(asList("foo1", "bar1"),asList("foo2", "bar2")));

        // When
        new SlackReplyHandler(session, channel).reply(table);

        // Then
        assertThat(attachment.getValue().getFields().get(0).getTitle()).isEqualTo("foo");
        assertThat(attachment.getValue().getFields().get(1).getTitle()).isEqualTo("bar");
    }

    @Test
    public void shouldCreateTableListWithTwoItems() {
        // Given
        ArgumentCaptor<SlackAttachment> attachment = ArgumentCaptor.forClass(SlackAttachment.class);
        given(session.sendMessage(eq(channel), anyString(), attachment.capture())).willReturn(null);
        SlackTableList tableList = new SlackTableList("title", ImmutableMap.of(
                "item1", asList("foo", "bar"),
                "item2", asList("baz")
        ));

        // When
        new SlackReplyHandler(session, channel).reply(tableList);

        // Then
        assertThat(attachment.getValue().getFields().get(0).getTitle()).isEqualTo("item1");
        assertThat(attachment.getValue().getFields().get(1).getTitle()).isEqualTo("item2");
    }

}
