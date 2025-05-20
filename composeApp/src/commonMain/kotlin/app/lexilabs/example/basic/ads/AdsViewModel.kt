package app.lexilabs.example.basic.ads

import androidx.lifecycle.ViewModel
import app.lexilabs.basic.ads.AdUnitId
import app.lexilabs.basic.ads.Consent
import app.lexilabs.basic.ads.ConsentDebugSettings
import app.lexilabs.basic.ads.ConsentRequestParameters
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.DependsOnGoogleUserMessagingPlatform
import app.lexilabs.basic.ads.ExperimentalBasicAds
import app.lexilabs.basic.ads.InterstitialAd
import app.lexilabs.basic.ads.RewardedAd
import app.lexilabs.basic.ads.RewardedInterstitialAd
import app.lexilabs.basic.logging.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(DependsOnGoogleMobileAds::class, DependsOnGoogleUserMessagingPlatform::class, ExperimentalBasicAds::class)
class AdsViewModel(contextFactory: ContextFactory): ViewModel() {

    private val tag = "AdsViewModel"
    private val rewarded = RewardedAd(contextFactory.getActivity())
    private val interstitial = InterstitialAd(contextFactory.getActivity())
    private val rewardedInterstitial = RewardedInterstitialAd(contextFactory.getActivity())

    private val _userConsented = MutableStateFlow(false)
    val userConsented: StateFlow<Boolean> = _userConsented.asStateFlow()

    private val _enableRewardedAd = MutableStateFlow(false)
    val enableRewardedAd: StateFlow<Boolean> = _enableRewardedAd.asStateFlow()

    private val _enableInterstitialAd = MutableStateFlow(false)
    val enableInterstitialAd: StateFlow<Boolean> = _enableInterstitialAd.asStateFlow()

    private val _enableRewardedInterstitialAd = MutableStateFlow(false)
    val enableRewardedInterstitialAd: StateFlow<Boolean> = _enableRewardedInterstitialAd.asStateFlow()

    private val consent = Consent(contextFactory.getActivity())
    private val debugSettings = ConsentDebugSettings.Builder(contextFactory.getActivity()).build()
    private val params = ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).build()

    init {
        checkForConsent()
        loadAllAds()
    }

    private fun checkForConsent(){
        consent.requestConsentInfoUpdate(params){
            Log.e(tag, "requestConsentInfoUpdate:failure: $it")
        }
        _userConsented.value = consent.canRequestAds()
    }

    private fun loadAllAds(){
        loadRewardedAd()
        loadInterstitialAd()
        loadRewardedInterstitialAd()
    }

    private fun loadRewardedAd(){
        rewarded.load(
            adUnitId = AdUnitId.REWARDED_DEFAULT,
            onLoad = {
                _enableRewardedAd.value = true
                setRewardedAdListeners()
                     },
            onFailure = { Log.e(tag, "${it.message}") }
        )
    }

    private fun loadInterstitialAd() {
        interstitial.load(
            adUnitId = AdUnitId.INTERSTITIAL_DEFAULT,
            onLoad = {
                _enableInterstitialAd.value = true
                setInterstitialAdListeners() },
            onFailure = { Log.e(tag, "${it.message}")}
        )
    }

    private fun loadRewardedInterstitialAd(){
        rewardedInterstitial.load(
            adUnitId = AdUnitId.REWARDED_INTERSTITIAL_DEFAULT,
            onLoad = {
                _enableRewardedInterstitialAd.value = true
                setRewardedInterstitialAdListeners()
                     },
            onFailure = { Log.e(tag, "${it.message}")}
        )
    }

    private fun setRewardedAdListeners(){
        rewarded.setListeners(
            onFailure = { Log.e(tag, "${it.message}") },
            onDismissed = { loadRewardedAd() }
        )
    }

    private fun setInterstitialAdListeners(){
        interstitial.setListeners(
            onFailure = { Log.e(tag, "${it.message}") },
            onDismissed = { loadInterstitialAd() }
        )
    }

    private fun setRewardedInterstitialAdListeners(){
        rewardedInterstitial.setListeners(
            onFailure = { Log.e(tag, "${it.message}") },
            onDismissed = { loadRewardedInterstitialAd() }
        )
    }

    fun showRewardedAd(onRewardEarned: ()-> Unit) {
        if (userConsented.value) {
            _enableRewardedAd.value = false
            rewarded.show { onRewardEarned() }
        } else {
            checkForConsent()
        }
    }

    fun showInterstitialAd() {
        if (userConsented.value) {
            _enableInterstitialAd.value = false
            interstitial.show()
        } else {
            checkForConsent()
        }
    }

    fun showRewardedInterstitialAd(onRewardEarned: ()-> Unit) {
        if (userConsented.value) {
            _enableRewardedInterstitialAd.value = false
            rewardedInterstitial.show { onRewardEarned() }
        } else {
            checkForConsent()
        }
    }
}