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
    @SecureKey(key = "d", value = "h")
})
public class MainActivity extends AppCompatActivity {

    @Override
    @SecureKey(key = "client-secret", value = "aD98E2GEk23TReYds9Zs9zdSdDBi23EAsdq29fXkpsDwp0W+h")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Assert.assertEquals(SecureEnvironment.getString("client-secret"), "aD98E2GEk23TReYds9Zs9zdSdDBi23EAsdq29fXkpsDwp0W+h");
        Assert.assertEquals(SecureEnvironment.getString("a"), "e");
        Assert.assertEquals(SecureEnvironment.getString("b"), "f");
        Assert.assertEquals(SecureEnvironment.getString("c"), "g");
        Assert.assertEquals(SecureEnvironment.getString("d"), "h");

        ((TextView) findViewById(R.id.activity_main_key)).setText(SecureEnvironment.getString("client-secret"));
    }

}
