//
// Created by Santiago Aguilera on 3/5/17.
//
#include "base64.h"
#include "aes.h"
#include "crypto_wrapper.h"

std::string & crypto_wrapper_decode(JNIEnv *env, std::string encoded_string) {
    std::string base64_decode_string = base64_decode(encoded_string);

    // Prepare data for aes
    unsigned int len = base64_decode_string.length();
    unsigned int src_len = len;

    // Copy data input to ashmem buffer
    char *data = &base64_decode_string[0];
    unsigned char *input = (unsigned char *) malloc(src_len);
    memset(input, 0, src_len);
    memcpy(input, data, len);

    unsigned char * buff = (unsigned char*) malloc(src_len);
    if (!buff) {
        free(input);
        throw "Couldnt assign memmory for buffer inside decode";
    }
    memset(buff, src_len, 0);

    // Set key and iv
    unsigned int key_schedule[AES_BLOCK_SIZE * 4] = { 0 };
    aes_key_setup(CRYPTO_WRAPPER_AES_KEY, key_schedule, CRYPTO_WRAPPER_AES_KEY_SIZE);

    // Decrypt
    aes_decrypt_cbc(input, src_len, buff, key_schedule, CRYPTO_WRAPPER_AES_KEY_SIZE, CRYPTO_WRAPPER_AES_IV);

    // Read padding assigned from last byte, remove it if exist.
    unsigned char *ptr = buff;
    ptr += (src_len - 1);
    unsigned int padding_len = (unsigned int) *ptr;
    if (padding_len > 0 && padding_len <= AES_BLOCK_SIZE) {
        src_len -= padding_len;
    }

    // Create byte array from buffer
    jbyteArray bytes = (env)->NewByteArray(src_len);
    (env)->SetByteArrayRegion(bytes, 0, src_len, (jbyte*) buff);

    // Interpret it as a string
    std::string result(reinterpret_cast<char const*>(buff), len);

    // Release ashmem
    free(input);
    free(buff);

    // Trim spaces / carriage returns / etc from string
    return aes_remove_padding(result);
}