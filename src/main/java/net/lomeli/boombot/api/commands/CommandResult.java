package net.lomeli.boombot.api.commands;

public class CommandResult {
    private String out;
    private Object[] args;
    private boolean privateMessage;

    public CommandResult(String out, Object... args) {
        this.out = out;
        this.args = new Object[0];
        if (args != null && args.length > 0)
            this.args = args;
    }

    public CommandResult(String out) {
        this.out = out;
        this.args = new Object[0];
    }

    public CommandResult setPrivateMessage(boolean value) {
        this.privateMessage = value;
        return this;
    }

    public boolean isPrivateMessage() {
        return privateMessage;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getResult() {
        return out;
    }
}
