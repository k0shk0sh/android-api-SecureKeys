package com.u.securekeys.internal;

import javax.xml.bind.DatatypeConverter;

/**
 * Class to encode strings with a given key
 * Created by saguilera on 3/3/17.
 */
public class Encoder {

    private String key;

    public Encoder(String key) {
        this.key = key;
    }

    /**
     * Encoder is nothing else than a base64 first to give a higher entropy to the string +
     * a simple XOR crypto with (for the attacker) an unknown pattern. This way:
     * - Higher entropy, less chances of brute force.
     * - Base64 changes our string size, this way it disorients + it isnt a simple XOR encode.
     * - Key of XOR can contain hex values (which should) and in this way if attacker plans to hexdump the heap
     * they wont see clearly where the keys and values are since they are mapped with the whole hex stuff.
     *
     * Possible issues:
     * - The attacker can read the .so code and find the crypto key to undo the system?
     *
     * @return encoded string
     */
    public String encode(String value) {
        String encoded64 = DatatypeConverter.printBase64Binary(value.getBytes());

        char encoded[] = encoded64.toCharArray();
        for (int i = 0 ; i < encoded.length ; i++) {
            encoded[i] ^= key.charAt(i % key.length());
        }

        return new String(encoded);
    }

}
