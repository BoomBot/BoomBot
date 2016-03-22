package net.lomeli.boombot.commands.special;

import com.google.common.collect.Maps;
import net.dv8tion.jda.Permission;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lang.LangRegistry;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;
import net.lomeli.boombot.lib.Logger;

public class OptionsCommand extends Command {
    private HashMap<String, ValueType> VALID_OPTIONS;

    public OptionsCommand() {
        super("options", "boombot.command.options");
        VALID_OPTIONS = Maps.newHashMap();
        VALID_OPTIONS.put("announceReady", ValueType.BOOOLEAN);
        VALID_OPTIONS.put("announceStopped", ValueType.BOOOLEAN);
        VALID_OPTIONS.put("disableClearChat", ValueType.BOOOLEAN);
        VALID_OPTIONS.put("secondsDelay", ValueType.INTEGER);
        VALID_OPTIONS.put("lang", ValueType.STRING);
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() == 2) {
            String name = cmd.getArgs().get(0);
            String value = cmd.getArgs().get(1);
            if (!commandExist(name)) {
                cmd.sendMessage(getContent() + ".missing.name.exist", name);
                return;
            }
            ValueType type = getExpectedType(name);
            Object trueValue = getValue(type, value);
            if (!type.matchType(trueValue)) {
                ValueType valueType = ValueType.getType(trueValue);
                String stType = valueType == null ? "null" : cmd.getGuildOptions().translate(valueType.getLocal());
                cmd.sendMessage("boombot.command.options.valuetype.mismatch", stType, cmd.getGuildOptions().translate(type.getLocal()));
                return;
            }
            setValue(cmd, name, value, trueValue);
        } else if (cmd.getArgs().size() > 2) {
            cmd.sendMessage(getContent() + ".args.toomany");
        } else if (cmd.getArgs().size() == 1) {
            String arg = cmd.getArgs().get(0);
            if (arg.equalsIgnoreCase("?") || arg.equalsIgnoreCase("help")) {

            } else
                cmd.sendMessage(getContent() + ".missing.value");
        } else
            cmd.sendMessage(getContent() + ".missing.all");
    }

    private void setValue(CommandInterface cmd, String name, String oldValue, Object value) {
        switch (name) {
            case "announceReady":
                cmd.getGuildOptions().setAnnounceReady((boolean) value);
                break;
            case "announceStopped":
                cmd.getGuildOptions().setAnnounceStopped((boolean) value);
                break;
            case "disableClearChat":
                cmd.getGuildOptions().setDisableClearChat((boolean) value);
                break;
            case "secondsDelay":
                cmd.getGuildOptions().setSecondsDelay((int) value);
                break;
            case "lang":
                cmd.getGuildOptions().setLang(oldValue);
                break;
        }
        cmd.sendMessage("boombot.command.options", name, value);
        BoomBot.configLoader.writeConfig();
    }

    private Object getValue(ValueType type, String str) {
        switch (type) {
            case BOOOLEAN:
                return parseBoolean(str);
            case INTEGER:
                return parseInt(str);
            case STRING:
                return LangRegistry.getLangName(str);
        }
        return null;
    }

    private Integer parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Logger.error("Number format exception in Options Command", e);
        }
        return null;
    }

    private Boolean parseBoolean(String str) {
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            Logger.error("Boolean format exception in Options Command", e);
        }
        return null;
    }

    private boolean commandExist(String name) {
        Iterator<String> it = VALID_OPTIONS.keySet().iterator();
        while (it.hasNext()) {
            if (it.next().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    private ValueType getExpectedType(String name) {
        for (Map.Entry<String, ValueType> entry : VALID_OPTIONS.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name))
                return entry.getValue();
        }
        return null;
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_SERVER);
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        String permissionLang = options.translate("permissions.manage.server");
        return options.translate("boombot.command.permissions.user.missing", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
    }

    enum ValueType {
        BOOOLEAN("boombot.command.options.valuetype.boolean", 0), INTEGER("boombot.command.options.valuetype.integer", 1),
        STRING("boombot.command.options.valuetype.string", 2);

        private static ValueType[] VALID_VALUES = {BOOOLEAN, INTEGER, STRING};

        private final String local;
        private final int type;

        ValueType(String local, int type) {
            this.local = local;
            this.type = type;
        }

        public String getLocal() {
            return local;
        }

        public boolean matchType(Object object) {
            switch (type) {
                case 0:
                    return object instanceof Boolean;
                case 1:
                    return object instanceof Integer;
                case 2:
                    return object instanceof String;
            }
            return false;
        }

        public static ValueType getType(Object object) {
            for (ValueType valueType : VALID_VALUES) {
                if (valueType.matchType(object))
                    return valueType;
            }
            return null;
        }
    }
}
