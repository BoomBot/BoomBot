package net.lomeli.boombot.command.other;

import com.google.common.base.Strings;

import java.util.List;
import java.util.Map;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.lib.I18n;
import net.lomeli.boombot.api.util.GuildUtil;
import net.lomeli.boombot.core.registry.CommandRegistry;

public class ListCommand implements ICommand {
    private final String COMMA = ", ";

    @Override
    public CommandResult execute(CommandData cmd) {
        I18n lang = GuildUtil.getGuildLang(cmd.getGuildData());
        String key = GuildUtil.getGuildCommandKey(cmd.getGuildData());
        StringBuilder out = new StringBuilder();
        out.append(lang.getLocalization("boombot.command.list"));
        addCommandList(out, lang, key, "BoomBot", BoomBot.commandRegistry.getListForAddon(CommandRegistry.BOOMBOT_ID));
        Map<String, List<String>> commandsMap = BoomBot.commandRegistry.getFullCommandList();
        commandsMap.entrySet().stream().filter(entry -> !Strings.isNullOrEmpty(entry.getKey())
                && !entry.getKey().equalsIgnoreCase(CommandRegistry.BOOMBOT_ID) &&
                entry.getValue() != null && !entry.getValue().isEmpty())
                .forEach(entry -> addCommandList(out, lang, key, entry.getKey(), entry.getValue()));
        return new CommandResult(out.toString()).setPrivateMessage(true);
    }

    void addCommandList(StringBuilder out, I18n lang, String key, String id, List<String> commandList) {
        if (commandList == null || commandList.isEmpty() || Strings.isNullOrEmpty(id)) return;
        String list = "```";
        StringBuilder commands = new StringBuilder();
        commandList.stream().filter(cmd -> !Strings.isNullOrEmpty(cmd))
                .forEach(cmd -> commands.append(key + cmd + COMMA));
        list += commands.toString().substring(0, commands.toString().length() - COMMA.length()) + "```";
        out.append("%n" + lang.getLocalization("boombot.command.list.commands", id, list));
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public boolean canUserExecute(CommandData cmd) {
        return true;
    }

    @Override
    public boolean canBotExecute(CommandData cmd) {
        return true;
    }

    @Override
    public CommandResult failedToExecuteMessage(CommandData cmd) {
        return null;
    }
}
