package com.alvarengadev.firebaseremoteconfigtest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : AppCompatActivity() {

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_default)

        (findViewById<Button>(R.id.button)).setOnClickListener {
            remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val updated = task.result
                            Log.d("TASK", "Config params updated: $updated")
                            Toast.makeText(this, "Fetch and activate succeeded",
                                    Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show()
                        }
                        showIcon(remoteConfig.getString("help_menu"))
                    }
        }

    }

    private val listenerHelp = View.OnClickListener {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()
    }

    private fun showIcon(textRemote: String) {
        when (textRemote) {
            "icon_help_menu" -> {
                setView((findViewById<ImageView>(R.id.iv_btn)))
            }
            "text_icon_help_menu" -> {
                setView((findViewById<FloatingActionButton>(R.id.float_btn)))
            }
            else -> {
                setView((findViewById<TextView>(R.id.tv_btn)))
            }
        }
    }

    private fun setView(view: View) {
        view.apply {
            visibility = View.VISIBLE
            setOnClickListener(listenerHelp)
        }
    }
}