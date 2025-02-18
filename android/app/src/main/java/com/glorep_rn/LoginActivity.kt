package com.glorep_rn

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.glorepsdk.CMSSdk
import com.glorepsdk.PreferencesManager
class LoginActivity : AppCompatActivity() {
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("LoginActivity", "Result Code: ${result.resultCode}")

            val reactContext = (application as MainApplication).reactNativeHost.reactInstanceManager.currentReactContext

            fun sendEventToReact(eventName: String, status: Boolean) {
                reactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    ?.emit(eventName, status)
            }

            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val value = data?.getStringExtra("data")

                if (value != null) {
                   // Log.d("LoginActivity", "Received data: $value")
                    sendEventToReact("LoginScreenStarted", true)
                   // Toast.makeText(this, "Result: $value", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("LoginActivity", "No data received in the result")
                    sendEventToReact("LoginScreenStarted", false)
                }
            } else {
                Log.d("LoginActivity", "Activity result was not OK. Result code: ${result.resultCode}")
                sendEventToReact("LoginScreenStarted", false)
            }

            finish()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val workflowId = intent.getStringExtra("WORKFLOW_ID")
        val userId = intent.getStringExtra("USER_ID") ?: ""

        Log.d("LoginActivity", "Received Workflow ID: $workflowId")
        Log.d("LoginActivity", "Received User ID: $userId")


        if (workflowId != null) {
            CMSSdk().sdkInitialize(this, userId, workflowId, startForResult)
        } else {
            Log.d("LoginActivity", "Workflow id is null")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        PreferencesManager(this).clear(this)

        finish()
    }
}
