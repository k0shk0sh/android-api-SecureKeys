package com.u.securekeys;

import android.support.annotation.NonNull;

/**
 * Bridge between native and java.
 * Created by saguilera on 3/3/17.
 */
public final class SecureKeys {

    private static final long NAN_LONG = -1;
    private static final String NAN_STRING = "";

    static {
        System.loadLibrary("native-lib");
        nativeInit(MapImpl.getMaps());
    }

    public static @NonNull String getString(@NonNull String key) {
        if (key.isEmpty()) {
            return NAN_STRING;
        }

        return nativeGetString(key);
    }

    public static long getLong(@NonNull String key) {
        String value = getString(key);

        if (value.isEmpty()) {
            return NAN_LONG;
        }

        return Long.valueOf(value);
    }

    public static double getDouble(@NonNull String key) {
        String value = getString(key);

        if (value.isEmpty()) {
            return NAN_LONG;
        }

        return Double.valueOf(value);
    }

    private static native String nativeGetString(String key);
    private static native void nativeInit(String[] array);

}
