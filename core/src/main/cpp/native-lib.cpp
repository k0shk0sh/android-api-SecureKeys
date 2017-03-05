#include <jni.h>
#include <string>
#include <map>
#include <algorithm>
#include <locale>
#include "aes.h"

#define _separator ";;;;"
#define _default_response ""

std::map<std::string , std::string> mapVals;

static const std::string available_chars =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        "abcdefghijklmnopqrstuvwxyz"
        "0123456789+/";

#define AES_KEY_SIZE 256

static unsigned char AES_IV[16] = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                                    0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
static unsigned char AES_KEY[32] = { 0x60, 0x3d, 0xeb, 0x10, 0x15, 0xca, 0x71,
                                     0xbe, 0x2b, 0x73, 0xae, 0xf0, 0x85, 0x7d, 0x77, 0x81, 0x1f, 0x35, 0x2c,
                                     0x07, 0x3b, 0x61, 0x08, 0xd7, 0x2d, 0x98, 0x10, 0xa3, 0x09, 0x14, 0xdf,
                                     0xf4 };

static inline bool is_decodable(unsigned char c) {
    return (isalnum(c) || (c == '+') || (c == '/'));
}

static inline std::string &left_trim(std::string &str) {
    str.erase(str.begin(), std::find_if(str.begin(), str.end(), std::not1(std::ptr_fun<int, int>(std::isspace))));
    return str;
}

static inline std::string &right_trime(std::string &str) {
    str.erase(std::find_if(str.rbegin(), str.rend(), std::not1(std::ptr_fun<int, int>(std::isspace))).base(), str.end());
    return str;
}

static inline std::string &trim(std::string &str) {
    return left_trim(right_trime(str));
}

std::string decode(JNIEnv *env, std::string encoded_string) {
    int in_len = encoded_string.size();
    int i = 0;
    int j = 0;
    int in_ = 0;
    unsigned char char_array_4[4], char_array_3[3];
    std::string ret;

    while (in_len-- && (encoded_string[in_] != '=') && is_decodable(encoded_string[in_])) {
        char_array_4[i++] = encoded_string[in_]; in_++;
        if (i ==4) {
            for (i = 0; i <4; i++)
                char_array_4[i] = available_chars.find(char_array_4[i]);

            char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
            char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
            char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];

            for (i = 0; (i < 3); i++)
                ret += char_array_3[i];
            i = 0;
        }
    }

    if (i) {
        for (j = i; j <4; j++)
            char_array_4[j] = 0;

        for (j = 0; j <4; j++)
            char_array_4[j] = available_chars.find(char_array_4[j]);

        char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
        char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
        char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];

        for (j = 0; (j < i - 1); j++) ret += char_array_3[j];
    }

    unsigned int len = ret.length();
    unsigned int src_len = len;

    char *data = &ret[0];
    unsigned char *input = (unsigned char *) malloc(src_len);
    memset(input, 0, src_len);
    memcpy(input, data, len);

    unsigned char * buff = (unsigned char*) malloc(src_len);
    if (!buff) {
        free(input);
        return NULL;
    }
    memset(buff, src_len, 0);

    //set key and iv
    unsigned int key_schedule[AES_BLOCK_SIZE * 4] = { 0 };
    aes_key_setup(AES_KEY, key_schedule, AES_KEY_SIZE);

    aes_decrypt_cbc(input, src_len, buff, key_schedule, AES_KEY_SIZE, AES_IV);

    unsigned char * ptr = buff;
    ptr += (src_len - 1);
    unsigned int padding_len = (unsigned int) *ptr;
    if (padding_len > 0 && padding_len <= AES_BLOCK_SIZE) {
        src_len -= padding_len;
    }

    jbyteArray bytes = (env)->NewByteArray(src_len);
    (env)->SetByteArrayRegion(bytes, 0, src_len, (jbyte*) buff);

    std::string result(reinterpret_cast<char const*>(buff), len);

    free(input);
    free(buff);

    return trim(result);
}

extern "C" {
    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved);
    JNIEXPORT jstring JNICALL Java_com_u_securekeys_SecureKeys_nativeGetString(JNIEnv *env, jclass instance, jstring key);
    JNIEXPORT void JNICALL Java_com_u_securekeys_SecureKeys_nativeInit(JNIEnv *env, jclass instance, jobjectArray array);
};


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL Java_com_u_securekeys_SecureKeys_nativeInit
        (JNIEnv *env, jclass instance, jobjectArray array) {
    int stringCount = env->GetArrayLength(array);

    for (int i = 0; i < stringCount; i++) {
        jstring string = (jstring) (env->GetObjectArrayElement(array, i));
        const char *rawString = env->GetStringUTFChars(string, 0);

        std::string keyval(rawString);
        unsigned long separator = keyval.find(_separator);
        if (separator != std::string::npos) {
            mapVals[keyval.substr(0, separator)] = keyval.substr(separator + 4);
        }

        if (string != NULL) {
            (env)->ReleaseStringUTFChars(string, rawString);
        }
    }
}

// TODO. This method shouldnt go to production.
std::string toString(JNIEnv *env) {
    std::string toString = "Map: \n";
    for(std::pair<std::string, std::string> const &pair : mapVals) {
        toString += "- " + decode(env,pair.first) + " : " + decode(env, pair.second) + ".\n";
    }
    toString += "\nNon Decoded Map: \n";
    for(std::pair<std::string, std::string> const &pair : mapVals) {
        toString += "- " + (pair.first) + " : " + (pair.second) + ".\n";
    }

    return toString;
}

JNIEXPORT jstring JNICALL Java_com_u_securekeys_SecureKeys_nativeGetString
        (JNIEnv *env, jclass instance, jstring key) {
    const char *rawString = env->GetStringUTFChars(key, 0);
    std::string paramKey(rawString);
    for(std::pair<std::string, std::string> const &pair : mapVals) {
        if (paramKey.compare(decode(env, pair.first)) == 0) {
            env->ReleaseStringUTFChars(key, rawString);
            return (env)->NewStringUTF(decode(env, pair.second).c_str());
        }
    }

    env->ReleaseStringUTFChars(key, rawString);

    return (env)->NewStringUTF(toString(env).c_str());
}