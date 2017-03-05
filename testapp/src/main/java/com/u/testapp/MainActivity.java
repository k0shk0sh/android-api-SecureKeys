package com.u.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.u.securekeys.SecureKeys;
import com.u.securekeys.annotation.SecureKey;
import junit.framework.Assert;

@SecureKey(key = "client-secret", value = "aD98E2GEk23TReYds9Zs9zdSdDBi23EAsdq29fXkpsDwp0W+h")
public class MainActivity extends AppCompatActivity {

    @Override
    @SecureKey(key = "key22", value = "value2")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Assert.assertEquals(SecureKeys.getString("client-secret"), "aD98E2GEk23TReYds9Zs9zdSdDBi23EAsdq29fXkpsDwp0W+h");
        Assert.assertEquals(SecureKeys.getString("key22"), "value2");

        ((TextView) findViewById(R.id.activity_main_key)).setText(SecureKeys.getString("client-secret"));
    }

}
