package com.chartboost.bannerexample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chartboost.heliumsdk.HeliumSdk
import com.chartboost.heliumsdk.ad.HeliumAdError
import com.chartboost.heliumsdk.ad.HeliumBannerAd
import com.chartboost.heliumsdk.ad.HeliumBannerAdListener
import java.util.*

class MainActivity : AppCompatActivity() {
    private val appId = "5ce82fbffde3570afb4647bc"
    private val appSignature = "fd8002155dcc6f161e41a62bce49250141b1876b"
    private var bannerAd: HeliumBannerAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bannerAd = findViewById(R.id.bannerAd)
        setUpSdk(
            onSuccess = {
                configureSdk()
                setUpBanner()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
                }
            },
            onError = { error ->
                runOnUiThread {
                    val errorMessage = "Helium SDK failed to initialize. Reason: " + error.message
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun configureSdk() {
        HeliumSdk.setTestMode(false)
        HeliumSdk.setDebugMode(true)
        HeliumSdk.setSubjectToCoppa(true)
        HeliumSdk.setSubjectToGDPR(true)
        HeliumSdk.setUserHasGivenConsent(true)
        HeliumSdk.setCCPAConsent(true)
    }

    private fun setUpSdk(onSuccess: () -> Unit, onError: (Error) -> Unit) {
        HeliumSdk.start(this, appId, appSignature) { error: Error? ->
            if (error != null) {
                onError(error)
            } else {
                onSuccess()
            }
        }
    }

    private fun setUpBanner() {
        bannerAd?.let { banner ->
            banner.setAdListener(
                createBannerAdListener(onLoaded = {
                    if (banner.readyToShow()) {
                        banner.show()
                    }
                })
            )
            banner.load()
        }
    }

    private fun createBannerAdListener(
        onLoaded: () -> Unit
    ): HeliumBannerAdListener {
        val bannerListener = object : HeliumBannerAdListener {
            override fun didReceiveWinningBid(
                placementName: String?,
                bidInfo: HashMap<String?, String?>?
            ) {
                addToLogView("$placementName (HeliumBannerAd) didReceiveWinningBid")
                addToLogView(bidInfo.toString())
            }

            override fun didCache(placementName: String?, error: HeliumAdError?) {
                if (error != null) {
                    addToLogView(placementName + " (HeliumBannerAd) didCache failed with heliumError: " + error.message)
                } else {
                    onLoaded()
                    addToLogView("$placementName (HeliumBannerAd) didCache")
                }
            }

            override fun didShow(placementName: String?, error: HeliumAdError?) {
                if (error != null) {
                    addToLogView(placementName + " (HeliumBannerAd) didShow failed with heliumError: " + error.message)
                } else {
                    addToLogView("$placementName (HeliumBannerAd) didShow")
                }
            }

            override fun didClose(placementName: String?, error: HeliumAdError?) {
                if (error != null) {
                    addToLogView(placementName + " (HeliumBannerAd) didClose failed with heliumError: " + error.message)
                } else {
                    addToLogView("$placementName (HeliumBannerAd) didClose")
                }
            }
        }
        return bannerListener
    }

    private fun addToLogView(msg: String) {
        Log.d("BANNER_EXAMPLE", msg)
    }
}