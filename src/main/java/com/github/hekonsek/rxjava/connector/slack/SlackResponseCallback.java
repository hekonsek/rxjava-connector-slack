package com.github.hekonsek.rxjava.connector.slack;

import com.github.hekonsek.rxjava.event.ResponseCallback;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;

import static org.apache.commons.lang3.StringUtils.join;

public class SlackResponseCallback implements ResponseCallback {

    private final SlackSession session;

    private final SlackChannel channel;

    public SlackResponseCallback(SlackSession session, SlackChannel channel) {
        this.session = session;
        this.channel = channel;
    }

    @Override public void respond(Object response) {
        if(response instanceof SlackTable) {
            SlackTable table = (SlackTable) response;
            SlackAttachment tableAttachment = new SlackAttachment();
            tableAttachment.setFallback(table.getTitle());
            for (int i = 0; i < table.getColumns().size(); i++) {
                int finalIndex = i;
                String fieldValue = join(table.getRows().stream().map(row -> row.get(finalIndex)).toArray(), "\n");
                tableAttachment.addField(table.getColumns().get(i), fieldValue, true);
            }
            session.sendMessage(channel, table.getTitle(), tableAttachment);
        } else {
            session.sendMessage(channel, response.toString());
        }
    }

}