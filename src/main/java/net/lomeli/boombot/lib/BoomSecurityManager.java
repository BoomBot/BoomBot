package net.lomeli.boombot.lib;

import java.security.Permission;

public class BoomSecurityManager extends SecurityManager {
    @Override
    public void checkPermission(Permission perm) {
        String permName = perm.getName() != null ? perm.getName() : "missing";
        if (permName.startsWith("exitVM")) {
            Class<?>[] classContexts = getClassContext();
            String callingClass = classContexts.length > 3 ? classContexts[4].getName() : "none";
            if (!"net.lomeli.boombot.BoomBot".equals(callingClass))
                throw new ExitTrappedException();
        } else if ("setSecurityManager".equals(permName))
            throw new SecurityException("Cannot replace BoomBot's security manager");
        return;
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        this.checkPermission(perm);
    }

    public static class ExitTrappedException extends SecurityException {
        private static final long serialVersionUID = 1L;
    }
}
