package net.lomeli.boombot.commands.special.other;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.addons.AddonContainer;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.logging.BoomLogger;

public class ViewAddonCommands extends Command {
    public ViewAddonCommands() {
        super("addon", "boombot.command.addon");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        List<AddonContainer> addonList = BoomBot.addonLoader.getLoadedAddons();
        if (addonList.isEmpty()) {
            cmd.sendMessage(getContent() + ".none");
            return;
        }
        cmd.sendMessage(getContent(), addonList.size(), BoomBot.jda.getSelfInfo().getId());
        String message = "```";
        AddonContainer addon;
        for (int i = 0; i < addonList.size(); i++) {
            addon = addonList.get(i);
            if (addon != null) {
                message += cmd.getGuildOptions().translate(getContent() + ".info", addon.getName(), addon.getId(), addon.getVersion());
                if (i < (addonList.size() - 1))
                    message += "\n";
            }
        }
        message += "```";
        addon = null;
        cmd.sendMessage(message);
    }
}
