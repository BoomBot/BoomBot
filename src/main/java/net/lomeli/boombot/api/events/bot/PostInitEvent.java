package net.lomeli.boombot.api.events.bot;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import net.lomeli.boombot.api.events.Event;

public class PostInitEvent extends Event {
    private String currentGame;
    private List<String> guildIDs;

    public PostInitEvent(String currentGame, String... guilds) {
        this.currentGame = currentGame;
        this.guildIDs = Lists.newArrayList();
        if (guilds != null && guilds.length > 0) {
            for (String ids : guilds) guildIDs.add(ids);
        }
    }

    public List<String> getGuildIDs() {
        return Collections.unmodifiableList(guildIDs);
    }

    public String getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(String currentGame) {
        this.currentGame = currentGame;
    }
}
