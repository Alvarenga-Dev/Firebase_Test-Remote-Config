package com.alvarengadev.firebaseremoteconfigtest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvarengadev.firebaseremoteconfigtest.firebase.FirebaseSettings
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings


class MainActivity : AppCompatActivity() {

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private var textRemote: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_default)

        tokenInitTester()
        initButton()
    }

    private fun initButton() {
        (findViewById<Button>(R.id.button)).setOnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "click")
            FirebaseSettings.sendFirebaseAnalytics(
                    this@MainActivity,
                    bundle,
                    resources.getString(R.string.firebase_key_analytics)
            )
            remoteConfig.fetch(0)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            remoteConfig.activate()
                            val updated = task.result
                            Log.d("TASK", "Config params updated: $updated")
                            Toast.makeText(this, "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show()
                        }
                        showWelcome(remoteConfig.getString("text_welcome"))
                    }
        }
    }

    private fun tokenInitTester() {
        FirebaseInstallations.getInstance().getToken( /* forceRefresh */true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Installations", "Installation auth token: " + task.result?.token)
                    } else {
                        Log.e("Installations", "Unable to get Installation auth token")
                    }
                }
    }

    private fun showWelcome(textRemote: String) {
        val text = findViewById<TextView>(R.id.textView)
        text.text = textRemote ?: "NULO AQUI"
    }
}