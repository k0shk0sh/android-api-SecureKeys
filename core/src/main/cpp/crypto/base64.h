//
// Created by Santiago Aguilera on 3/5/17.
//

#ifndef SECUREKEYS_BASE64_H
#define SECUREKEYS_BASE64_H

#include <string>

static const std::string base64_available_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    "abcdefghijklmnopqrstuvwxyz"
    "0123456789+/";

std::string base64_decode(std::string &encoded_string);

#endif //SECUREKEYS_BASE64_H
