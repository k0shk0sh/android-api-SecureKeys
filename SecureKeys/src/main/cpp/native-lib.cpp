#include <jni.h>
#include <string>
#include <map>

/**
 * Sensible data: This is all subject to change
 * - Class with obfuscated constants is called MapImpl
 * - Method that returns a string array inside the class is called getMaps()
 * - String array has children as: "key;;;;value"
 *
 */

std::map<std::string , std::string> mapVals;

static const int B64index[256] = { 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                                   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
                                   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 62, 63, 62, 62, 63, 52, 53, 54, 55,
                                   56, 57, 58, 59, 60, 61,  0,  0,  0,  0,  0,  0,  0,  0,  1,  2,  3,  4,  5,  6,
                                   7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  0,
                                   0,  0,  0, 63,  0, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                                   41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };

std::string decode(const void* data, const size_t len) {
    unsigned char* dataPointer = (unsigned char*) data;
    int pad = len > 0 && (len % 4 || dataPointer[len - 1] == '=');
    const size_t length = ((len + 3) / 4 - pad) * 4;
    std::string str(length / 4 * 3 + pad, '\0');

    for (size_t i = 0, j = 0; i < length; i += 4) {
        int n = B64index[dataPointer[i]] << 18 | B64index[dataPointer[i + 1]] << 12 | B64index[dataPointer[i + 2]] << 6 | B64index[dataPointer[i + 3]];
        str[j++] = n >> 16;
        str[j++] = n >> 8 & 0xFF;
        str[j++] = n & 0xFF;
    }

    if (pad) {
        int n = B64index[dataPointer[length]] << 18 | B64index[dataPointer[length + 1]] << 12;
        str[str.size() - 1] = n >> 16;

        if (len > length + 2 && dataPointer[length + 2] != '=') {
            n |= B64index[dataPointer[length + 2]] << 6;
            str.push_back(n >> 8 & 0xFF);
        }
    }

    return str;
}

/**
 * Init:
 * This gets all the map values and stores it inside here, to avoid
 * repeating this same code everytime we will look for a constant.
 *
 * The map most probably has both key/value crypted, so this map will contain "garbage"
 * to avoid an attacker doing a heap dump post this init and seeing all the sensible
 * values decrypted.
 */
JNIEXPORT void JNICALL Java_com_u_securekeys_internal_JNIHandler_init(JNIEnv *env, jobject instance) {
    jclass mapClass = (env)->FindClass("com/u/securekeys/MapImpl"); // TODO decidir el nombre de la clase que va a tener los strings obfuscaods
    jmethodID mapConstructor = (env)->GetMethodID(mapClass, "<init>", "()V");
    jobject mapInstance = (env)->NewObject(mapClass, mapConstructor);

    jmethodID getMapsMethod = (env)->GetMethodID(mapClass, "getMaps", "()[Ljava/lang/String");
    jobjectArray getMapsReturnValue = (jobjectArray)(env)->CallObjectMethod(mapInstance, getMapsMethod);

    int stringCount = env->GetArrayLength(getMapsReturnValue);

    for (int i = 0; i < stringCount; i++) {
        jstring string = (jstring) (env->GetObjectArrayElement(getMapsReturnValue, i));
        const char *rawString = env->GetStringUTFChars(string, 0);

        std::string keyval(rawString);
        unsigned long separator = keyval.find(";;;;");
        if (separator != std::string::npos) {
            mapVals[keyval.substr(0, separator)] = keyval.substr(separator + 4);
        }

        (env)->ReleaseStringUTFChars(string, 0);
    }

    // Release created objects
    (env)->DeleteLocalRef(getMapsReturnValue);
    (env)->DeleteLocalRef(mapInstance);
    (env)->DeleteLocalRef(mapClass);
}

JNIEXPORT jstring JNICALL Java_com_u_securekeys_internal_JNIHandler_getString(JNIEnv *env, jobject instance, jstring key) {
    std::string paramKey(env->GetStringUTFChars(key, 0));
    for(std::pair<std::string, std::string> const &pair : mapVals) {
        if (paramKey.compare(decode(&pair.first, pair.first.length())) == 0) {
            return (env)->NewStringUTF(decode(&pair.second, pair.second.length()).c_str());
        }
    }

    return (env)->NewStringUTF("");
}