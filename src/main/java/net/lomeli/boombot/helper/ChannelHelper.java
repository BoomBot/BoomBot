package net.lomeli.boombot.helper;

import com.google.common.collect.Lists;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.JDAImpl;
import net.dv8tion.jda.entities.impl.UserImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import net.lomeli.boombot.lib.DeleteMessage;

public class ChannelHelper {
    public static List<Message> getChannelMessages(TextChannel channel, Message message) {
        List<Message> messages = Lists.newArrayList();
        String requestURL = String.format("https://discordapp.com/api/channels/%s/messages?before=%s", channel.getId(), message.getId());
        JDAImpl api = getChannelAPI(channel);
        try {
            JSONArray response = api.getRequester().getA(requestURL);
            Iterator<Object> it = response.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof JSONObject) {
                    Message msg = jsonToMessage((JDAImpl) channel.getJDA(), (JSONObject) obj);
                    if (msg != null)
                        messages.add(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static JDAImpl getChannelAPI(TextChannel channel) {
        if (channel == null) return null;
        JDA jda = channel.getJDA();
        if (jda instanceof JDAImpl)
            return (JDAImpl) jda;
        return null;
    }

    private static Message jsonToMessage(JDAImpl api, JSONObject json) {
        String id = json.getString("id");
        String channelID = json.getString("channel_id");
        JSONObject author = json.getJSONObject("author");
        User user = new UserImpl(author.getString("id"), api);
        return new DeleteMessage(api, id, channelID, user);
    }
}
