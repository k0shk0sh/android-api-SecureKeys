package com.u.securekeys.internal;

import javax.xml.bind.DatatypeConverter;

/**
 * Class to encode strings with a given key
 * Created by saguilera on 3/3/17.
 */
public class Encoder {

    public Encoder() {}

    public String encode(String value) {
        try {
            return DatatypeConverter.printBase64Binary(value.getBytes("UTF-8"));
        } catch (Exception e) {
            return "";
        }
    }

}
