package net.lomeli.boombot.helper;

import com.google.common.collect.Lists;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.SelfInfo;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.JDAImpl;
import net.dv8tion.jda.entities.impl.UserImpl;
import net.dv8tion.jda.exceptions.PermissionException;
import net.dv8tion.jda.exceptions.RateLimitedException;
import net.dv8tion.jda.handle.EntityBuilder;
import net.dv8tion.jda.requests.Requester;
import net.sourceforge.jaad.aac.tools.MS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import net.lomeli.boombot.lang.DeleteMessage;

public class ChannelHelper {
    public static List<Message> getChannelMessages(TextChannel channel, Message message) {
        List<Message> messages = Lists.newArrayList();
        String requestURL = String.format("https://discordapp.com/api/channels/%s/messages?before=%s", channel.getId(), message.getId());
        JDAImpl api = getChannelAPI(channel);
        try {
            Requester.Response response = api.getRequester().get(requestURL);
            if (response.isOk() && response.getArray() != null) {
                Iterator<Object> it = response.getArray().iterator();
                while (it.hasNext()) {
                    Object obj = it.next();
                    if (obj instanceof JSONObject) {
                        Message msg = jsonToMessage((JDAImpl) channel.getJDA(), (JSONObject) obj);
                        if (msg != null)
                            messages.add(msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static Message sendAttachmentMessage(Message msg, TextChannel channel) {
        SelfInfo self = channel.getJDA().getSelfInfo();
        if (!channel.checkPermission(self, Permission.MESSAGE_WRITE))
            throw new PermissionException(Permission.MESSAGE_WRITE);
        //TODO: PermissionException for Permission.MESSAGE_ATTACH_FILES maybe

        JDAImpl api = (JDAImpl) channel.getJDA();
        if (api.getMessageLimit() != null)
            throw new RateLimitedException(api.getMessageLimit() - System.currentTimeMillis());
        try {
            JSONArray attachments = attachmentsToJson(msg.getAttachments());
            JSONObject jsonmsg = new JSONObject().put("content", msg.getRawContent()).put("tts", msg.isTTS()).put("attachments", attachments);
            System.out.println(jsonmsg.toString());
            Requester.Response response = api.getRequester().post(Requester.DISCORD_API_PREFIX + "channels/" + channel.getId() + "/messages",
                    jsonmsg);
            if (response.isRateLimit()) {
                long retry_after = response.getObject().getLong("retry_after");
                api.setMessageTimeout(retry_after);
                throw new RateLimitedException(retry_after);
            }
            if (!response.isOk())
                return null;
            return new EntityBuilder(api).createMessage(response.getObject());
        } catch (JSONException ex) {
            JDAImpl.LOG.log(ex);
            //sending failed
            return null;
        }
    }

    private static JSONArray attachmentsToJson(List<Message.Attachment> attachments) {
        JSONArray jsonArray = new JSONArray();
        if (attachments != null && !attachments.isEmpty()) {
            attachments.stream().filter(a -> a != null).forEach(a -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", a.getId());
                jsonObject.put("filename", a.getFileName());
                jsonObject.put("size", a.getSize());
                jsonObject.put("url", a.getUrl());
                jsonObject.put("proxy_url", a.getProxyUrl());
                jsonObject.put("height", a.getHeight());
                jsonObject.put("width", a.getWidth());
                jsonArray.put(jsonObject);
            });
        }
        return jsonArray;
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
