package net.lomeli.boombot.api.event;

public abstract class Event {
    private boolean canceled;

    public boolean cancelable() {
        return false;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
