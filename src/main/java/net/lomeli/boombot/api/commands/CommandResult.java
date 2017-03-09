package net.lomeli.boombot.api.commands;

public class CommandResult {
    private String out;
    private Object[] args;

    public CommandResult(String out, Object...args) {
        this.out = out;
        this.args = new Object[0];
        if (args != null && args.length > 0)
            this.args = args;
    }

    public CommandResult(String out){
        this.out = out;
        this.args = new Object[0];
    }

    public Object[] getArgs() {
        return args;
    }

    public String getResult() {
        return out;
    }
}
