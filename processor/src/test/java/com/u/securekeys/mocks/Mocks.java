package com.u.securekeys.mocks;

import com.u.securekeys.annotation.SecureKey;
import com.u.securekeys.annotation.SecureKeys;

/**
 * Created by saguilera on 6/18/17.
 */
public class Mocks {

    /**
     * MOCK AN EMPTY CLASS
     */

    public static final String MOCK_EMPTY = "final class EmptyClass {}";

    /**
     * MOCK A CLASS WITH A SINGLE SECURE_KEY
     */

    public static final String MOCK_SECURE_KEY = "" +
        "@com.u.securekeys.annotation.SecureKey(key = \"key\", value = \"value\")\n" +
        "final class SingleKeyClass {}";
    public static final String MOCK_SECURE_KEY_GEN_FILE = "// Method that retrieves the mapping of the values\n" +
        "package com.u.securekeys;\n" +
        "\n" +
        "import java.lang.String;\n" +
        "\n" +
        "final class ProcessedMap {\n" +
        "  public static final String[] retrieve() {\n" +
        "    String array[] = new String[1];\n" +
        "    array[0] = \"O0TenKit32Pp/dEAQ9RS6g==;;;;2/lhAK3rkMJXwau5KsBvEA==\";\n" +
        "    return array;\n" +
        "  }\n" +
        "}";

    /**
     * MOCK A CLASS WITH MORE THAN ONE SECURE_KEY
     */

    public static final String MOCK_SECURE_KEY_MULTIPLE = "" +
        "    @com.u.securekeys.annotation.SecureKey(key = \"key\", value = \"value\")\n" +
        "    final class MultipleKeyClass {\n" +
        "        @com.u.securekeys.annotation.SecureKey(key = \"another\", value = \"anothervalue\")\n" +
        "        private int field;\n" +
        "    }";
    public static final String MOCK_SECURE_KEY_MULTIPLE_GEN_FILE = "// Method that retrieves the mapping of the values\n" +
        "package com.u.securekeys;\n" +
        "\n" +
        "import java.lang.String;\n" +
        "\n" +
        "final class ProcessedMap {\n" +
        "  public static final String[] retrieve() {\n" +
        "    String array[] = new String[2];\n" +
        "    array[0] = \"O0TenKit32Pp/dEAQ9RS6g==;;;;2/lhAK3rkMJXwau5KsBvEA==\";\n" +
        "    array[1] = \"xGNS15pgulUZCvUQRntc5w==;;;;J2jHPSzh8VORCgND0L9A5g==\";\n" +
        "    return array;\n" +
        "  }\n" +
        "}";

}
