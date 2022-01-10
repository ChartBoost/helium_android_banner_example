package com.chartboost.bannerexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chartboost.heliumsdk.HeliumSdk
import java.lang.Error

class MainActivity : AppCompatActivity() {
    private val testDevicesIds = listOf("C40CE9EAD372884F1ABF3FDFCD67CB2F")
    private val appId = "5a4e797538a5f00cf60738d6"
    private val appSignature = "d29d75ce6213c746ba986f464e2b4a510be40399"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSdk()
    }

    private fun initSdk() {
        HeliumSdk.setDebugMode(false)
        HeliumSdk.start(this, appId, appSignature, heliumSdkListener)
        HeliumSdk.setTestMode(true)
        HeliumSdk.setSubjectToCoppa(false)
        HeliumSdk.setSubjectToGDPR(false)
        HeliumSdk.setUserHasGivenConsent(false)
        HeliumSdk.setCCPAConsent(true)
    }
    private val heliumSdkListener = HeliumSdk.HeliumSdkListener { Log.d("HELIUM","Initialization successful") }
}