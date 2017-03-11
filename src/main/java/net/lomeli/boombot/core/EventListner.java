package net.lomeli.boombot.core;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.events.bot.GuildEvent;
import net.lomeli.boombot.api.events.text.MessageEvent;
import net.lomeli.boombot.api.events.text.command.CommandEvent;
import net.lomeli.boombot.api.events.user.UserEvent;
import net.lomeli.boombot.api.lib.I18n;
import net.lomeli.boombot.api.lib.UserProxy;
import net.lomeli.boombot.api.nbt.NBTTagCompound;
import net.lomeli.boombot.api.util.GuildUtil;
import net.lomeli.boombot.command.custom.CustomCommand;
import net.lomeli.boombot.command.custom.CustomContent;
import net.lomeli.boombot.command.custom.CustomRegistry;
import net.lomeli.boombot.lib.util.MessageUtil;

public class EventListner extends ListenerAdapter {
    public boolean scheduleShutdown;

    @Override
    public void onResume(ResumedEvent event) {
        if (event.getJDA() != null) {
            List<Guild> guilds = event.getJDA().getGuilds();
            if (guilds != null && !guilds.isEmpty()) {
                guilds.stream().filter(guild -> guild != null).forEach(guild -> {
                    BoomBot.logger.debug("Adding guild %s", guild.getName());
                    BoomAPI.dataRegistry.addGuild(guild.getId());
                    GuildUtil.getGuildData(guild.getId()).setString("name", guild.getName());
                });
            }
        }
    }

    @Override
    public void onGenericGuildMessage(GenericGuildMessageEvent event) {
        if (BoomBot.jda == null || BoomBot.jda.getSelfUser() == null || event == null || event.getMessage() == null ||
                event.getGuild() == null || event.getAuthor() == null || event.getAuthor().isBot() ||
                (BoomAPI.debugMode && !event.getGuild().getId().equals(BoomBot.debugGuildID) && !event.getChannel().getName().equalsIgnoreCase("test-channel")))
            return;
        Message msg = event.getMessage();
        if (msg.isEdited()) return;
        Guild guild = event.getGuild();
        NBTTagCompound guildData = GuildUtil.getGuildData(guild.getId());
        I18n lang = GuildUtil.getGuildLang(guildData);
        Member member = guild.getMember(event.getAuthor());
        UserProxy userProxy = new UserProxy(event.getAuthor().getId(), event.getAuthor().getName(), member.getNickname());
        String key = GuildUtil.getGuildCommandKey(guildData);
        if (msg.getRawContent().startsWith(key)) {
            String raw = msg.getRawContent();
            if (Strings.isNullOrEmpty(raw)) return;
            String[] splitContent = raw.split(" ");
            if (splitContent == null || splitContent.length < 1) return;
            String commandName = splitContent[0].substring(key.length());
            ICommand command = BoomBot.commandRegistry.getCommand(commandName);
            String message = "";
            if (commandName.length() + key.length() < raw.length())
                message = raw.substring(commandName.length() + key.length());
            CommandData data = new CommandData(userProxy, guild.getId(),
                    event.getChannel().getId(), message, userToIDList(msg.getMentionedUsers()), Strings.isNullOrEmpty(message) ? null : message.split(" "));
            if (command != null) {
                handleCommand(command, data, lang, event, guildData);
            } else {
                CustomContent custom = CustomRegistry.INSTANCE.getGuildCommand(event.getGuild().getId(), commandName);
                if (custom != null) {
                    if (BoomAPI.eventRegistry.post(new CommandEvent(new CustomCommand(custom), data))) return;
                    String out = formatMessage(custom.getCommandContent(), msg.getAuthor(), guild, guildData, data.getArgs());
                    event.getChannel().sendMessage(out).queue();
                } else if (BoomAPI.eventRegistry.post(new MessageEvent(userProxy, msg.getChannel().getId(), guild.getId(), msg.getRawContent(), msg.getContent())))
                    msg.delete().queue();
            }
        } else if (BoomAPI.eventRegistry.post(new MessageEvent(userProxy, msg.getChannel().getId(), guild.getId(), msg.getRawContent(), msg.getContent())))
            msg.delete().queue();
    }

    List<String> userToIDList(List<User> initList) {
        List<String> list = Lists.newArrayList();
        if (initList != null)
            initList.stream().filter(user -> user != null).forEach(user -> list.add(user.getId()));
        return list;
    }

    void handleCommand(ICommand command, CommandData data, I18n lang, GenericGuildMessageEvent event, NBTTagCompound guildData) {
        CommandResult result;
        if (!command.canBotExecute(data) || !command.canUserExecute(data))
            result = command.failedToExecuteMessage(data);
        else {
            if (BoomAPI.eventRegistry.post(new CommandEvent(command, data))) return;
            result = command.execute(data);
        }
        if (result != null) outputResult(result, lang, event, guildData);
    }

    void outputResult(CommandResult result, I18n lang, GenericGuildMessageEvent event, NBTTagCompound guildData) {
        String out = lang.getLocalization(result.getResult());
        out = formatMessage(out, event.getAuthor(), event.getGuild(), guildData, result.getArgs());
        if (!Strings.isNullOrEmpty(out)) {
            if (result.isPrivateMessage()) {
                if (!event.getMember().getUser().hasPrivateChannel())
                    event.getMember().getUser().openPrivateChannel().queue();
                event.getMember().getUser().getPrivateChannel().sendMessage(out).queue();
            } else event.getChannel().sendMessage(out).submit();
        }
        if (scheduleShutdown) event.getJDA().shutdown();
    }

    String formatMessage(String content, User user, Guild guild, NBTTagCompound data, Object... args) {
        String out = content;
        if (out.contains("%n")) out = out.replaceAll("%n", "\n");
        boolean allowMention = GuildUtil.guildAllowBotMention(data);
        String userName = allowMention ? String.format("<@%s>", user.getId()) : user.getName();
        out = out.replaceAll("%u", userName).replaceAll("%U", userName.toUpperCase());
        if (args != null && args.length > 0)
            out = String.format(out, args);
        if (!allowMention)
            out = MessageUtil.stripMentions(out, guild);
        if (!GuildUtil.guildAllowEveryoneMention(data))
            out = MessageUtil.stripEveryoneMention(out);
        if (!GuildUtil.guildAllowHereMention(data))
            out = MessageUtil.stripHere(out);
        if (!GuildUtil.guildAllowBotTTS(data))
            out = MessageUtil.stripTTS(out);
        return out;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        BoomAPI.eventRegistry.post(new UserEvent.UserJoinedEvent(
                new UserProxy(event.getMember().getUser().getId(), event.getMember().getUser().getName()), event.getGuild().getId()));
        NBTTagCompound guildData = GuildUtil.getGuildData(event.getGuild().getId());
        NBTTagCompound memberData = GuildUtil.getGuildMemberData(guildData, event.getMember().getUser().getId());
        GuildUtil.setGuildMemberData(guildData, memberData);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        BoomAPI.eventRegistry.post(new UserEvent.UserLeaveEvent(
                new UserProxy(event.getMember().getUser().getId(), event.getMember().getUser().getName()), event.getGuild().getId()));
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (event.getGuild() != null) {
            List<String> guildIDs = Lists.newArrayList();
            BoomBot.jda.getGuilds().stream().filter(guild -> guild != null).forEach(guild -> guildIDs.add(guild.getId()));
            if (!guildIDs.contains(event.getGuild().getId())) guildIDs.add(event.getGuild().getId());
            BoomAPI.eventRegistry.post(new GuildEvent.JoinedGuildEvent(guildIDs));
            BoomBot.logger.debug("Adding guild %s", event.getGuild().getName());
            if (!BoomAPI.dataRegistry.guildHasData(event.getGuild().getId())) {
                BoomAPI.dataRegistry.addGuild(event.getGuild().getId());
                GuildUtil.getGuildData(event.getGuild().getId()).setString("name", event.getGuild().getName());
            }

        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        if (event.getGuild() != null) {
            List<String> guildIDs = Lists.newArrayList();
            BoomBot.jda.getGuilds().stream().filter(guild -> guild != null).forEach(guild -> guildIDs.add(guild.getId()));
            if (guildIDs.contains(event.getGuild().getId())) guildIDs.remove(event.getGuild().getId());
            BoomAPI.eventRegistry.post(new GuildEvent.LeaveGuildEvent(guildIDs));
        }
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        BoomAPI.dataRegistry.writeData();
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        BoomBot.logger.info("BoomBot shutting down!");
        BoomAPI.dataRegistry.writeData();
        BoomBot.logger.info("BoomBot has finished shutting down!");
        System.exit(0);
    }
}
