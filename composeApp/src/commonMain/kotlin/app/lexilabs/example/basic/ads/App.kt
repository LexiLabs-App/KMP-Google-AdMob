package app.lexilabs.example.basic.ads

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.lexilabs.basic.ads.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(DependsOnGoogleMobileAds::class, ExperimentalBasicAds::class)
@Composable
@Preview
fun App(platformContext: ContextFactory) {
    MaterialTheme {
        val viewModel = remember { AdsViewModel(platformContext) }
        val userConsented by viewModel.userConsented.collectAsState()
        val enableRewardedButton by viewModel.enableRewardedAd.collectAsState()
        val enableInterstitialButton by viewModel.enableInterstitialAd.collectAsState()
        val enableRewardedInterstitialButton by viewModel.enableRewardedInterstitialAd.collectAsState()

        var showBanner by remember { mutableStateOf(false) }
        var rewardCount by remember { mutableStateOf(0) }

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
                    onClick = { showBanner = !showBanner },
                ) { Text(if (showBanner) { "Hide Banner Ads" } else { "Show Banner Ads" }) }

                Button(
                    onClick = { viewModel.showInterstitialAd() },
                    enabled = enableInterstitialButton
                ) { Text("Show Interstitial Ad") }

                Text("Reward Count: $rewardCount")
                Button(
                    onClick = { viewModel.showRewardedAd { rewardCount += 1 } },
                    enabled = enableRewardedButton
                ) { Text("Show Rewarded Ad") }

                Button(
                    onClick = { viewModel.showRewardedInterstitialAd{ rewardCount += 1 } },
                    enabled = enableRewardedInterstitialButton
                ) { Text("Show Rewarded Interstitial Ads") }
            }

            if (showBanner && userConsented) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    BannerAd()
                    BannerAd()
                }
            }
        }
    }
}