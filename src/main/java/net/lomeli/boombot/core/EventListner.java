package net.lomeli.boombot.core;

import com.google.common.base.Strings;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.DisconnectEvent;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.ShutdownEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.events.message.priv.GenericPrivateMessageEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.commands.CommandInterface;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.data.GuildData;
import net.lomeli.boombot.api.util.BasicGuildUtil;
import net.lomeli.boombot.command.custom.CustomContent;
import net.lomeli.boombot.command.custom.CustomRegistry;
import net.lomeli.boombot.lib.util.MessageUtil;

public class EventListner extends ListenerAdapter {
    public boolean scheduleShutdown;

    @Override
    public void onReady(ReadyEvent event) {
        if (event.getJDA() != null) {
            List<Guild> guilds = event.getJDA().getGuilds();
            if (guilds != null && !guilds.isEmpty()) {
                for (Guild guild : guilds) {
                    BoomBot.logger.debug("Adding guild %s", guild.getName());
                    BoomAPI.dataRegistry.addGuild(guild.getId());
                    BoomAPI.dataRegistry.getDataForGuild(guild.getId()).getGuildData().setString("name", guild.getName());
                }
            }
        }
    }

    @Override
    public void onGenericGuildMessage(GenericGuildMessageEvent event) {
        if (BoomBot.jda == null || BoomBot.jda.getSelfInfo() == null || event == null || event.getMessage() == null ||
                event.getGuild() == null || event.getAuthor() == null || event.getAuthor().isBot() ||
                (BoomAPI.debugMode && !event.getGuild().getId().equals(BoomBot.debugGuildID) && !event.getChannel().getName().equalsIgnoreCase("test-channel")))
            return;
        Message msg = event.getMessage();
        if (msg.isEdited()) return;
        Guild guild = event.getGuild();
        GuildData data = BoomAPI.dataRegistry.getDataForGuild(guild.getId());
        String key = data.getGuildData().getString("commandKey");
        if (msg.getRawContent().startsWith(key)) {
            String rawContent = msg.getRawContent();
            if (Strings.isNullOrEmpty(rawContent)) return;
            String[] splitContent = rawContent.split(" ");
            if (splitContent != null && splitContent.length > 0) {
                String commandName = splitContent[0].substring(1);
                ICommand cmd = BoomAPI.commandRegistry.getCommand(commandName);
                String message = "";
                if (commandName.length() + 1 < rawContent.length())
                    message = rawContent.substring(commandName.length() + 2);
                CommandInterface cmdInterface = new CommandInterface(event.getAuthor().getId(), guild.getId(),
                        event.getChannel().getId(), message, Strings.isNullOrEmpty(message) ? null : message.split(" "));
                if (cmd != null) {
                    String result = formatMessage(cmd.execute(cmdInterface), msg.getAuthor(), guild, data, cmdInterface.getArgs());
                    if (!Strings.isNullOrEmpty(result)) event.getChannel().sendMessage(result);
                    if (scheduleShutdown) event.getJDA().shutdown();
                } else {
                    CustomContent custom = CustomRegistry.INSTANCE.getGuildCommand(event.getGuild().getId(), commandName);
                    if (custom != null) {
                        String out = formatMessage(custom.getCommandContent(), msg.getAuthor(), guild, data, cmdInterface.getArgs());
                        event.getChannel().sendMessage(out);
                    }
                }
            }
        }
    }

    String formatMessage(String content, User user, Guild guild, GuildData data, Object... args) {
        String out = content;
        if (out.contains("%n")) out = out.replaceAll("%n", "\n");
        boolean allowMention = BasicGuildUtil.guildAllowBotMention(data);
        String userName = allowMention ? String.format("<@%s>", user.getId()) : user.getUsername();
        out = out.replaceAll("%u", userName).replaceAll("%U", userName.toUpperCase());
        out = String.format(out, args);
        if (!allowMention)
            out = MessageUtil.stripMentions(out, guild);
        if (!BasicGuildUtil.guildAllowEveryoneMention(data))
            out = MessageUtil.stripEveryoneMention(out);
        if (!BasicGuildUtil.guildAllowHereMention(data))
            out = MessageUtil.stripHere(out);
        if (!BasicGuildUtil.guildAllowBotTTS(data))
            out = MessageUtil.stripTTS(out);
        return out;
    }

    @Override
    public void onGenericPrivateMessage(GenericPrivateMessageEvent event) {
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (event.getGuild() != null) {
            BoomBot.logger.debug("Adding guild %s", event.getGuild().getName());
            if (!BoomAPI.dataRegistry.guildHasData(event.getGuild().getId())) {
                BoomAPI.dataRegistry.addGuild(event.getGuild().getId());
                BoomAPI.dataRegistry.getDataForGuild(event.getGuild().getId()).getGuildData().setString("name", event.getGuild().getName());
            }
        }
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        BoomAPI.dataRegistry.writeGuildData();
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        BoomBot.logger.info("BoomBot shutting down!");
        BoomAPI.dataRegistry.writeGuildData();
    }
}
