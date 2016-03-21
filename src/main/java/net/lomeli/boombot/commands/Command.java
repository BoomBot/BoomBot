package net.lomeli.boombot.commands;

import com.google.common.collect.Lists;
import net.dv8tion.jda.entities.User;
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
            int count = StringUtils.countMatches(str, "%s");
            Object[] arg = new Object[count];
            if (count == 1 && j == (content.length - 1)) {
                String trueArg = "";
                for (Object o : commandArgs)
                    trueArg += o + " ";
                trueArg = trueArg.substring(0, trueArg.length() - 1);
                arg = new Object[]{trueArg};
            }
            cmd.sendMessage(str.replaceAll("%u", cmd.getUser().getUsername()).replaceAll("%U", cmd.getUser().getUsername().toUpperCase()), arg);
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

    public boolean canUserExecute(CommandInterface cmd) {
        return true;
    }

    public boolean canBoomBotExecute(CommandInterface cmd) {
        return true;
    }

    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        return "";
    }

    public enum UserType {
        USER(false), BOOMBOT(true);

        private final boolean boomBot;

        UserType(boolean boomBot) {
            this.boomBot = boomBot;
        }

        public boolean isBoomBot() {
            return boomBot;
        }
    }
}
