package com.u.securekeys;

import android.support.annotation.Keep;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by saguilera on 3/3/17.
 */
@Keep
@SuppressWarnings("unused")
public class MapImpl {

    private static HashMap<String, String> map;

    static {
        map = new HashMap<>();
        map.put("Eo³\u0006Ŝ¬}\b", "@0£@ŵ\u008C\u0015O"); // Key1-Value1 / "a2V5MQ==", "dmFsdWUx"
    }

    public static String[] getMaps() {
        String[] array = new String[map.size()];

        int i = 0;
        for (Map.Entry<String, String> pair : map.entrySet()) {
            array[i] = (pair.getKey() + ";;;;" + pair.getValue());
            i++;
        }

        return array;
    }

}
