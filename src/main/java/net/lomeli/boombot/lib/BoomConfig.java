package net.lomeli.boombot.lib;

import com.google.common.collect.Lists;

import java.util.List;

import net.lomeli.boombot.commands.Command;

public class BoomConfig {
    public List<Command> customCommands;
    public int secondsDelay;

    public BoomConfig() {
        secondsDelay = 2;
        customCommands = Lists.newArrayList();
    }
}
