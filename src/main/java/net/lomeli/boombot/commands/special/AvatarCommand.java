package net.lomeli.boombot.commands.special;

import com.google.common.base.Strings;
import net.dv8tion.jda.entities.User;

import java.util.List;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class AvatarCommand extends Command {
    public AvatarCommand() {
        super("avatar", "boombot.command.avatar");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        List<User> mentioned = cmd.getMessage().getMentionedUsers();
        if (!cmd.getArgs().isEmpty() && mentioned.isEmpty()) {
            cmd.sendMessage(getContent() + ".extraargs");
            return;
        }
        User user = cmd.getUser();
        if (mentioned.size() > 0) {
            user = mentioned.get(0);
            if (mentioned.size() > 1)
                cmd.sendMessage(getContent() + ".one");
        }
        displayAvatar(user, cmd);
    }

    private void displayAvatar(User user, CommandInterface cmd) {
        String content = user.getAvatarUrl();
        if (Strings.isNullOrEmpty(content))
            cmd.getGuildOptions().translate(getContent() + ".missing", "<@" + user.getId() + ">");
        cmd.sendMessage(content);
    }
}
