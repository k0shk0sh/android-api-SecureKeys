package com.u.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.u.securekeys.SecureEnvironment;
import com.u.securekeys.annotation.SecureKey;
import com.u.securekeys.annotation.SecureKeys;
import junit.framework.Assert;

@SecureKeys({
    @SecureKey(key = "a", value = "e"),
    @SecureKey(key = "b", value = "f"),
    @SecureKey(key = "c", value = "g"),
    @SecureKey(key = "d", value = "h"),
    @SecureKey(key = "long_from_BuildConfig", value = BuildConfig.TESTING_VALUE_1),
    @SecureKey(key = "double_from_BuildConfig", value = BuildConfig.TESTING_VALUE_2)
})
public class MainActivity extends AppCompatActivity {

    @Override
    @SecureKey(key = "client-secret", value = "aD98E2GEk23TReYds9Zs9zdSdDBi23EAsdq29fXkpsDwp0W+h")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Assert.assertEquals("aD98E2GEk23TReYds9Zs9zdSdDBi23EAsdq29fXkpsDwp0W+h", SecureEnvironment.getString("client-secret"));
        Assert.assertEquals("e", SecureEnvironment.getString("a"));
        Assert.assertEquals("f", SecureEnvironment.getString("b"));
        Assert.assertEquals("g", SecureEnvironment.getString("c"));
        Assert.assertEquals("h", SecureEnvironment.getString("d"));
        Assert.assertEquals(Long.valueOf(BuildConfig.TESTING_VALUE_1), Long.valueOf(SecureEnvironment.getLong("long_from_BuildConfig")));
        Assert.assertEquals(Double.valueOf(BuildConfig.TESTING_VALUE_2), SecureEnvironment.getDouble("double_from_BuildConfig"));

        ((TextView) findViewById(R.id.activity_main_key)).setText(SecureEnvironment.getString("client-secret"));
    }

}
