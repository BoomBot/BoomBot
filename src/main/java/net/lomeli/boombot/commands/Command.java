package net.lomeli.boombot.commands;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import net.lomeli.boombot.lib.CommandInterface;

public class Command {
    private String name;
    private String content;

    public Command(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public void executeCommand(CommandInterface cmd) {
        String fullContent = getContent().replaceAll("%n", "\n");
        String[] content = fullContent.split("\n");
        List<String> commandArgs = Lists.newArrayList(cmd.getArgs());
        for (String str : content) {
            int count = StringUtils.countMatches("%s", str);
            cmd.sendMessage(str, commandArgs.toArray());
            for (int i = 0; i < count; i++)
                commandArgs.remove(0);
        }
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public boolean canExecuteCommand(CommandInterface cmd) {
        return true;
    }
}
