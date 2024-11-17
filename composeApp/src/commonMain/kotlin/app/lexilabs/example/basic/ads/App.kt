package app.lexilabs.example.basic.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import app.lexilabs.basic.ads.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(DependsOnGoogleMobileAds::class)
@Composable
@Preview
fun App(platformContext: ContextFactory) {
    MaterialTheme {
        var showBanner by remember { mutableStateOf(false) }
        var showInterstitial by remember { mutableStateOf(false) }
        var showRewarded by remember { mutableStateOf(false) }
        var showRewardedInterstitial by remember { mutableStateOf(false) }
        var rewardCount by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { showBanner = !showBanner },
            ) {
                Text( if (showBanner) { "Hide Banner Ads" } else { "Show Banner Ads" } )
            }

            Button(
                onClick = { showInterstitial = true },
            ){ Text("Show Interstitial Ad") }

            Text("Reward Count: $rewardCount")
            Button(
                onClick = { showRewarded = true },
            ){ Text("Show Rewarded Ad") }

            Button(
                onClick = {showRewardedInterstitial = true },
            ){ Text("Show Rewarded Interstitial Ads") }
        }

        if (showBanner) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                BannerAd()
                BannerAd()
            }
        }
        if (showInterstitial) {
            InterstitialAd(platformContext.getActivity(), onDismissed = { showInterstitial = false } )
        }
        if (showRewarded) {
            RewardedAd(
                platformContext.getActivity(),
                onDismissed = { showRewarded = false },
                onRewardEarned = { rewardCount += 1 }
            )
        }
        if (showRewardedInterstitial) {
            RewardedInterstitialAd(
                platformContext.getActivity(),
                onDismissed = { showRewardedInterstitial = false },
                onRewardEarned = { rewardCount += 1 }
            )
        }
    }
}