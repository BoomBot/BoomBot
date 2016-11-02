package net.lomeli.boombot.command.admin;

import com.google.common.base.Strings;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.commands.CommandInterface;
import net.lomeli.boombot.api.data.EntityData;
import net.lomeli.boombot.core.registry.DataRegistry;

public class ShutdownCommand implements ICommand {
    @Override
    public String execute(CommandInterface cmd) {
        EntityData boomBotData = ((DataRegistry)BoomAPI.dataRegistry).getBoomBotData();
        if (boomBotData == null) return "";
        String[] adminIDs = boomBotData.getStringArray("adminIDs");
        if (adminIDs == null || adminIDs.length <= 0) return "";
        boolean isAdmin = false;
        for (String id : adminIDs) {
            if (!Strings.isNullOrEmpty(id) && id.equalsIgnoreCase(cmd.getUserID())) {
                isAdmin = true;
                break;
            }
        }
        if (!isAdmin) return "";
        BoomBot.mainListener.scheduleShutdown = true;
        return "%u is shutting down BoomBot";
    }

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return true;
    }

    @Override
    public boolean canBotExecute(CommandInterface cmd) {
        return true;
    }

    @Override
    public String failedToExecuteMessage(CommandInterface cmd) {
        return null;
    }
}
