package org.script.util;

public final class Settings {

    // TODO: temporary class, will be replaced by gui

   // bank or drop items?
    public static final boolean BANK_ITEMS = true;

    // hop whenever closer rock is taken or run to further rock?
    public static final boolean COMPETE_FOR_FIRST_ROCK = false;

    // use bank chest or deposit box?
    static final boolean USE_BANK_CHEST = true;

    private Settings() {
        throw new RuntimeException();
    }
}
