package com.example.vlad.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

/**
 * Launcher activity. Responsible for logging in into VK profile with the needed scope
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
//        String pass = "";
//        for(int i = 0; i < fingerprints.length; i++) {
//            pass += fingerprints[i];
//        }
//        Log.e("FINGERPRINTS", pass);
        String[] scope = new String[]{VKScope.OFFLINE, VKScope.WALL};
        VKSdk.login(this, scope);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(final VKAccessToken res) {
                startFeedActivity();
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * launches FeedActivity in order to get news feed
     */
    private void startFeedActivity() {
        Intent intent = new Intent(this, FeedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
