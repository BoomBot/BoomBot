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
        for (int j = 0; j < content.length; j++) {
            String str = content[j];
            int count = StringUtils.countMatches("%s", str);
            Object[] arg = commandArgs.toArray();
            if (count == 1 && j == (content.length - 1)) {
                String trueArg = "";
                for (Object o : commandArgs)
                    trueArg += o + " ";
                arg = new Object[]{trueArg};
            }
            cmd.sendMessage(str, arg);
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
