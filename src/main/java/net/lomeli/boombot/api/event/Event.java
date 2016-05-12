package net.lomeli.boombot.api.event;

public abstract class Event {
    private boolean canceled;

    public boolean cancelable() {
        return false;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
