package com.u.securekeys.internal;

import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by saguilera on 6/18/17.
 */

public class RestictionsTest {

    // Initial vector for AES cipher
    private static final byte initialVectorBytes[] = new byte[] { 0x04, 0x06, 0x02, 0x01, 0x01,
        0x05, 0x06, 0x0f, 0x0a, 0x09, 0x03, 0x03, 0x03, 0x04, 0x02, 0x0a };

    // Key used for AES cypher
    private static final byte keyBytes[] = new byte[] { 0x60, 0x3d, (byte) 0xe5,
        0x1a, 0x5c, (byte) 0x1a, 0x11, (byte) 0xde, 0x2b, 0x74,
        (byte) 0xae, (byte) 0xf0, (byte) 0x8a, 0x7d, 0x77, (byte) 0x83,
        0x13, 0x3d, 0x2c, 0x07, 0x3b, 0x61, 0x08, (byte) 0xda, 0x22,
        (byte) 0x9a, 0x11, (byte) 0xa3, 0x09, 0x14, (byte) 0xdf,
        (byte) 0xf1 };

    @Test
    public void test_RestrictionsAreRemoved() {
        boolean removed = Restrictions.remove();

        Assert.assertTrue(removed);

        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final IvParameterSpec iv = new IvParameterSpec(initialVectorBytes);

            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] data = cipher.doFinal("test_message".getBytes());

            Assert.assertNotNull(data);
            Assert.assertTrue(data.length > 0);
        } catch (InvalidKeyException ex) {
            Assert.fail("Shouldnt reach here.");
        } catch (Exception ex) {
            Assert.fail("Shouldnt reach here.");
        }
    }

}
