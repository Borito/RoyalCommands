package org.royaldev.royalcommands;

import java.lang.reflect.Field;

public final class TestHelpers {

    public static void clearInstance() throws NoSuchFieldException, IllegalAccessException {
        TestHelpers.setInstance(null);
    }

    public static void setInstance(final RoyalCommands instance) throws NoSuchFieldException, IllegalAccessException {
        final Field f = RoyalCommands.class.getDeclaredField("instance");
        final boolean wasAccessible = f.isAccessible();
        f.setAccessible(true);
        f.set(null, instance);
        f.setAccessible(wasAccessible);
    }

}
