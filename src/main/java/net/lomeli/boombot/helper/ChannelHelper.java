package net.lomeli.boombot.helper;

import com.google.common.collect.Lists;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.impl.JDAImpl;
import org.json.JSONObject;

import java.util.List;

public class ChannelHelper {
    public static List<Message> getChannelMessages(TextChannel channel, Message message) {
        List<Message> messages = Lists.newArrayList();
        String requestURL = String.format("https://discordapp.com/api/channels/%s/messages?before=%s", channel.getId(), message.getId());
        JDAImpl api = (JDAImpl) channel.getJDA();
        try {
            JSONObject response = api.getRequester().get(requestURL);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}
