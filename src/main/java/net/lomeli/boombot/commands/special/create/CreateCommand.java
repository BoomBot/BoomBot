package net.lomeli.boombot.commands.special.create;

import com.google.common.base.Strings;
import net.dv8tion.jda.Permission;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class CreateCommand extends Command {
    public static Pattern imagePattern = Pattern.compile("https?:\\/\\/(?:[a-z\\-]+\\.)+[a-z]{2,6}(?:\\/[^\\/#?]+)+\\.(?:jpe?g|gif|png)");

    public CreateCommand() {
        super("mkcom", "boombot.command.createcommand");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 1) {
            String name = cmd.getArgs().get(0);
            String content = "";
            for (int i = 1; i < cmd.getArgs().size(); i++) {
                String st = cmd.getArgs().get(i);
                content += st + " ";
            }
            if (Strings.isNullOrEmpty(name)) {
                cmd.sendMessage(getContent() + ".name.empty");
                return;
            }
            if (Strings.isNullOrEmpty(content)) {
                cmd.sendMessage(getContent() + ".content.empty");
                return;
            }
            String safeName = name.replaceAll("%s", "% s").replaceAll("%S", "% S").replaceAll("%u", "<User>").replaceAll("%U", "<USER>");
            String safeContent = content.replaceAll("%s", "(Blank)").replaceAll("%S", "(Blank)").replaceAll("%u", "<User>").replaceAll("%U", "<USER>");
            if (BoomBot.config.addGuildCommand(cmd.getGuildOptions(), new Command(name, content.replace("\\n", "\n"))))
                cmd.sendMessage(getContent(), safeName, safeContent);
            else
                cmd.sendMessage(getContent() + ".name.exists", safeName);
        } else
            cmd.sendMessage(getContent() + ".missing");
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL);
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        String permissionLang = options.translate("permissions.manage.channel");
        return options.translate("boombot.command.permissions.user.missing", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
    }

    private boolean imgLink(String content) {
        Matcher matcher = imagePattern.matcher(content);
        return matcher.find();
    }
}
