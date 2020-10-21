package com.alvarengadev.firebaseremoteconfigtest.firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseSettings {
    companion object {
        fun sendFirebaseAnalytics(context: Context, param: Bundle?, eventName: String){
            FirebaseAnalytics.getInstance(context).logEvent(eventName, param)
        }
    }
}