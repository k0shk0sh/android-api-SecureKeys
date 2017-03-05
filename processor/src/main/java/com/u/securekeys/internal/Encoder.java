package com.u.securekeys.internal;

import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Class to encode strings with a given key
 * Created by saguilera on 3/3/17.
 */
public class Encoder {

    private static final byte initialVectorBytes[] = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04,
        0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

    // Key used for encoding
    private static final byte keyBytes[] = new byte[] { 0x60, 0x3d, (byte) 0xeb,
        0x10, 0x15, (byte) 0xca, 0x71, (byte) 0xbe, 0x2b, 0x73,
        (byte) 0xae, (byte) 0xf0, (byte) 0x85, 0x7d, 0x77, (byte) 0x81,
        0x1f, 0x35, 0x2c, 0x07, 0x3b, 0x61, 0x08, (byte) 0xd7, 0x2d,
        (byte) 0x98, 0x10, (byte) 0xa3, 0x09, 0x14, (byte) 0xdf,
        (byte) 0xf4 };

    public Encoder() {}

    public String encode(String value) {
        try {
            return DatatypeConverter.printBase64Binary(aes(value.getBytes()));
        } catch (Exception e) {
            return "";
        }
    }

    private byte[] aes(byte[] content) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final IvParameterSpec iv = new IvParameterSpec(initialVectorBytes);

            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return cipher.doFinal(content);
        } catch (InvalidKeyException e) {
            System.out.println("Please install JCE's Unlimited Strength Policies for next compilation");
            if (Restrictions.remove())
                aes(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
