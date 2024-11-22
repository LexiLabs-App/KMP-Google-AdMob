# Kotlin Multiplatform AdMob

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7f52ff.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-iOS](http://img.shields.io/badge/platform-iOS-6ED88D.svg?style=flat)

A Kotlin Multiplatform template for apps implementing [Basic-Ads](https://github.com/LexiLabs-App/basic) with Compose for Multiplatform. This approach follows the [how-to guide for composable Ads](https://medium.com/@robert.jamison/composable-ads-f8795924aa0d) and the [context factory](https://medium.com/@robert.jamison/passing-android-context-in-kmp-jetpack-compose-8de5b5de7bdd) techniques by [Robert Jamison](https://medium.com/@robert.jamison).

<img src="./ad.png" width="720">

## Composable Types:
* Banner Ads
* Interstitial Ads
* Rewarded Ads
* Rewarded Interstitial Ads __(beta)__

<img src="screenshots.png" width="720">

## Dependencies
- 🧩 [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform); for shared UI
- ♨️ [LexiLabs Basic-Ads](https://basic.lexilabs.app); for porting AdMob into KMP composables
- 💰 [Google AdMob](https://admob.google.com); for Google AdMob functionality
