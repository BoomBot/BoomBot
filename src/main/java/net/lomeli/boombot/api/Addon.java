package net.lomeli.boombot.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Addon {
    /**
     * ID for the addon. MUST be all lowercase and have no spaces! Will be ignored otherwise!
     */
    String addonID();

    /**
     * Name of the addon
     */
    String name() default "";

    /**
     * Version of the addon. It's recommended that use
     * <a href="http://semver.org/">Semantic Versioning</a>
     */
    String version() default "";

    String acceptedBoomBotVersion() default "*";

    /**
     * Annotation for addons to have a instance of themselves.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Instance {
    }

    /**
     * Used to mark load events for addons (Pre, Init, and Post)
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Event {
    }
}
