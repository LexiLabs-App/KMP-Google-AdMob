@file:Suppress("AssignedValueIsNeverRead")

package app.lexilabs.example.basic.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.lexilabs.basic.ads.AdState
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.DependsOnGoogleUserMessagingPlatform
import app.lexilabs.basic.ads.composable.BannerAd
import app.lexilabs.basic.ads.composable.ConsentPopup
import app.lexilabs.basic.ads.composable.InterstitialAd
import app.lexilabs.basic.ads.composable.NativeAd
import app.lexilabs.basic.ads.composable.RewardedAd
import app.lexilabs.basic.ads.composable.RewardedInterstitialAd
import app.lexilabs.basic.ads.composable.rememberBannerAd
import app.lexilabs.basic.ads.composable.rememberConsent
import app.lexilabs.basic.ads.composable.rememberInterstitialAd
import app.lexilabs.basic.ads.composable.rememberNativeAd
import app.lexilabs.basic.ads.composable.rememberRewardedAd
import app.lexilabs.basic.ads.composable.rememberRewardedInterstitialAd
import app.lexilabs.basic.logging.Log
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(DependsOnGoogleMobileAds::class, DependsOnGoogleUserMessagingPlatform::class)
@Composable
@Preview
fun App() {
    MaterialTheme {

        // remember Ads and their States
        val consent by rememberConsent()
        val topBannerAd by rememberBannerAd()
        val bottomBannerAd by rememberBannerAd()
        val rewardedAd by rememberRewardedAd()
        val interstitialAd by rememberInterstitialAd()
        val rewardedInterstitialAd by rememberRewardedInterstitialAd()
        val nativeAd by rememberNativeAd()

        // remember when to show Ads
        var showBannerAds by remember { mutableStateOf(false) }
        val bannerEnabled = when (topBannerAd.state) {
            AdState.NONE,
            AdState.LOADING,
            AdState.FAILING -> { false }
            AdState.READY,
            AdState.SHOWING,
            AdState.SHOWN,
            AdState.DISMISSED -> { true }
        }
        var showInterstitialAd by remember { mutableStateOf(false) }
        var showRewardedAd by remember { mutableStateOf(false) }
        var showRewardedInterstitialAd by remember { mutableStateOf(false) }
        var showNativeAd by remember { mutableStateOf(false) }
        var showPrivacyOptions by remember { mutableStateOf(false) }

        // remember Reward state
        var rewardCount by remember { mutableStateOf(0) }

        // Try to show a consent popup
        ConsentPopup(
            consent = consent,
            onFailure = { Log.e("App", "failure:${it.message}")}
        )

        Surface(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = { showBannerAds = !showBannerAds },
                    enabled = bannerEnabled
                ) {
                    Text(
                        if (showBannerAds && bannerEnabled) {
                            "Hide Banner Ads"
                        } else {
                            "Show Banner Ads"
                        }
                    )
                }

                Button(
                    onClick = { showInterstitialAd = true },
                    enabled = interstitialAd.state == AdState.READY
                ) { Text("Show Interstitial Ad") }

                Text("Reward Count: $rewardCount")
                Button(
                    onClick = { showRewardedAd = true },
                    enabled = rewardedAd.state == AdState.READY
                ) { Text("Show Rewarded Ad") }

                Button(
                    onClick = { showRewardedInterstitialAd = true },
                    enabled = rewardedInterstitialAd.state == AdState.READY
                ) { Text("Show Rewarded Interstitial Ad") }

                Button(
                    onClick = { showNativeAd = true },
                    enabled = nativeAd.state == AdState.READY
                ) { Text("Show Native Ad") }

                Button(
                    onClick = { showPrivacyOptions = true },
                ) { Text("Show Privacy Options") }
            }

            if (showBannerAds && consent.canRequestAds) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    BannerAd(topBannerAd)
                    BannerAd(bottomBannerAd)
                }
            }
            if (showInterstitialAd && consent.canRequestAds) {
                InterstitialAd(
                    interstitialAd,
                    onDismissed = { showInterstitialAd = false }
                )
            }
            if (showRewardedAd && consent.canRequestAds){
                RewardedAd(
                    rewardedAd,
                    onDismissed = { showRewardedAd = false},
                    onRewardEarned = { rewardCount += 1}
                )
            }
            if (showRewardedInterstitialAd && consent.canRequestAds){
                RewardedInterstitialAd(
                    rewardedInterstitialAd,
                    onDismissed = { showRewardedInterstitialAd = false },
                    onRewardEarned = { rewardCount += 1 }
                )
            }
            if(showNativeAd && consent.canRequestAds) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.White)
                ){
                    NativeAd(
                        loadedAd = nativeAd,
                    )
                    /** THIS IS A FAST BUT IMPROPER IMPLEMENTATION OF AD DISMISSAL **/
                    Button(
                        onClick = { showNativeAd = false },
                    ) { Text("Hide Ad") }
                }
            }
            if(showPrivacyOptions) {
                consent.showPrivacyOptionsForm(
                    onDismissed = {showPrivacyOptions = false},
                )
            }
        }
    }
}