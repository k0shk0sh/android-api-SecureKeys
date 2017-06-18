package com.u.securekeys;

import com.u.securekeys.annotation.SecureKey;
import com.u.securekeys.annotation.SecureKeys;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.u.securekeys.SecureEnvironment.ENV_PROCESSED_MAP_METHOD;
import static com.u.securekeys.SecureEnvironment.ENV_PROCESSED_MAP_NAME;
import static org.junit.Assert.fail;

/**
 * This test suite can only be run for the moment from the IDE, since it adds all the necessary classpaths for using the
 * java.library.path when loading a JNI library. From console its still impossible to test, so when coding
 * please ensure to test the full suite from the IDE
 */
@SecureKeys({
    @SecureKey(key = "test_key_string", value = "A simple string"),
    @SecureKey(key = "test_key_long", value = "1234567890987654321"),
    @SecureKey(key = "test_key_double", value = "0.98765432123456789")
})
public class SecureEnvironmentTest {

    @Before
    public void setUp() {
        SecureEnvironment.getString("nothing");
    }

    /**
     * Check we can initialize the native library correctly
     */
    @Test
    public void test_JNIInitialized() {
        try {
            Field initializedField = SecureEnvironment.class.getDeclaredField("initialized");
            initializedField.setAccessible(true);
            Boolean initialized = initializedField.getBoolean(null);
            Assert.assertTrue(initialized);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test the processed map with the mappings exists.
     * Dont check if it has garbage or the values, that is a problem of the processor module.
     * We do have a problem if we cant find the mappings.
     */
    @Test
    public void test_ProcessedMapExists() {
        try {
            Class<?> clazz = Class.forName(ENV_PROCESSED_MAP_NAME);
            Method method = clazz.getDeclaredMethod(ENV_PROCESSED_MAP_METHOD);
            method.setAccessible(true);
            String[] mappings = (String[]) method.invoke(null);

            Assert.assertEquals("There are only 3 keys, so something went wrong", 3, mappings.length);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test the accessors to values
     */
    @Test
    public void test_GetString() {
        Assert.assertEquals("Values werent the same", "A simple string", SecureEnvironment.getString("test_key_string"));
    }

    /**
     * Test the accessors to values
     */
    @Test
    public void test_GetLong() {
        Assert.assertEquals("Values werent the same", 1234567890987654321L, SecureEnvironment.getLong("test_key_long"));
    }

    /**
     * Test the accessors to values
     */
    @Test
    public void test_GetDouble() {
        Assert.assertEquals("Values werent the same", 0.98765432123456789D, SecureEnvironment.getDouble("test_key_double"), 0.00000000000000001D);
    }

}