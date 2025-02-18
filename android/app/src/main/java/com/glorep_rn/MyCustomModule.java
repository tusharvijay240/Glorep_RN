package com.glorep_rn;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class MyCustomModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public MyCustomModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "XYZ";
    }

    @ReactMethod
    public void showSDKLauncher(String workflowId, String userId) {
        UiThreadUtil.runOnUiThread(() -> {
            try {
                if (getCurrentActivity() != null) {
                    Intent intent = new Intent(reactContext, LoginActivity.class);
                    intent.putExtra("WORKFLOW_ID", workflowId);
                    intent.putExtra("USER_ID", userId);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getCurrentActivity().startActivity(intent);
                    sendEventToReactNative("LoginScreenStarted", true);
                } else {
                    // Handle the case when activity is null
                    android.util.Log.e("ReactNative", "Activity is null");
                    sendEventToReactNative("LoginScreenStarted", false);
                }
            } catch (Exception e) {
                android.util.Log.e("ReactNative", "Error launching LoginActivity", e);
                sendEventToReactNative("LoginScreenStarted", false);
            }
        });
    }
    @ReactMethod
    public void sendEventToReactNative(String eventName, boolean status) {
        if (reactContext != null) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, status);
        }
    }

}

