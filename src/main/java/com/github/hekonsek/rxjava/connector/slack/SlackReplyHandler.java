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

import com.github.hekonsek.rxjava.event.ReplyHandler;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import io.reactivex.Completable;

import static io.reactivex.Completable.complete;
import static org.apache.commons.lang3.StringUtils.join;

public class SlackReplyHandler implements ReplyHandler {

    private final SlackSession session;

    private final SlackChannel channel;

    public SlackReplyHandler(SlackSession session, SlackChannel channel) {
        this.session = session;
        this.channel = channel;
    }

    @Override public Completable reply(Object response) {
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
        } else if(response instanceof SlackTableList) {
            SlackTableList tableList = (SlackTableList) response;
            SlackAttachment tableAttachment = new SlackAttachment();
            tableAttachment.setFallback(tableList.title());
            tableList.items().entrySet().forEach(entry -> {
                String fieldValue = join(entry.getValue().stream().map(Object::toString).toArray(), "\n");
                tableAttachment.addField(entry.getKey(), fieldValue, false);
            });
            session.sendMessage(channel, tableList.title(), tableAttachment);
        } else {
            session.sendMessage(channel, response.toString());
        }
        return complete();
    }

}