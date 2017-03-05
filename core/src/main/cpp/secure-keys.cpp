#include <jni.h>

#include <string>
#include <map>

#include "crypto/crypto_wrapper.h"

#define _separator ";;;;"
#define _default_response ""

#define decode crypto_wrapper_decode

std::map<std::string , std::string> mapVals;

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

    return (env)->NewStringUTF(_default_response);
}