package net.lomeli.boombot.lang;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.*;
import net.dv8tion.jda.entities.impl.JDAImpl;
import net.dv8tion.jda.exceptions.PermissionException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

public class DeleteMessage implements Message {
    private JDAImpl api;
    private String id;
    private String channelId;
    private User author;

    public DeleteMessage(JDAImpl api, String id, String channelId, User author) {
        this.api = api;
        this.id = id;
        this.channelId = channelId;
        this.author = author;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<User> getMentionedUsers() {
        return null;
    }

    @Override
    public List<TextChannel> getMentionedChannels() {
        return null;
    }

    @Override
    public boolean mentionsEveryone() {
        return false;
    }

    @Override
    public OffsetDateTime getTime() {
        return null;
    }

    @Override
    public boolean isEdited() {
        return false;
    }

    @Override
    public OffsetDateTime getEditedTimestamp() {
        return null;
    }

    @Override
    public User getAuthor() {
        return author;
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public String getRawContent() {
        return null;
    }

    @Override
    public String getStrippedContent() {
        return null;
    }

    @Override
    public boolean isPrivate() {
        return false;
    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public List<Attachment> getAttachments() {
        return null;
    }

    @Override
    public List<MessageEmbed> getEmbeds() {
        return null;
    }

    @Override
    public boolean isTTS() {
        return false;
    }

    @Override
    public Message updateMessage(String newContent) {
        return null;
    }

    @Override
    public void deleteMessage() {
        if(!api.getSelfInfo().getId().equals(getAuthor().getId()))
        {
            if (isPrivate())
                throw new PermissionException("Cannot delete another User's messages in a PrivateChannel.");
            else if (!api.getTextChannelById(getChannelId()).checkPermission(api.getSelfInfo(), Permission.MESSAGE_MANAGE))
                throw new PermissionException(Permission.MESSAGE_MANAGE);
        }
        api.getRequester().delete("https://discordapp.com/api/channels/" + channelId + "/messages/" + getId());
    }

    @Override
    public JDA getJDA() {
        return null;
    }

    @Override
    public MessageChannel getChannel() {
        return null;
    }

    @Override
    public void updateMessageAsync(String newContent, Consumer<Message> callback) {
    }
}
