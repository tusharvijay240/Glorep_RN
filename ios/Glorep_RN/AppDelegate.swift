import UIKit
import React
import React_RCTAppDelegate
import ReactAppDependencyProvider
import GloRep_SDK

@main
class AppDelegate: RCTAppDelegate {
  override func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    self.moduleName = "Glorep_RN"
    self.dependencyProvider = RCTAppDependencyProvider()
    APPConstants.shared.initializeGooglePlaces(apiKey: "AIzaSyDvoaaaR6TIyv_N0hJVLs_g887sU1VUakE")
    APPConstants.shared.initializeIQKeyboardManager()
    // You can add your custom initial props in the dictionary below.
    // They will be passed down to the ViewController used by React Native.
    self.initialProps = [:]

    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }

  override func sourceURL(for bridge: RCTBridge) -> URL? {
    self.bundleURL()
  }

  override func bundleURL() -> URL? {
#if DEBUG
    RCTBundleURLProvider.sharedSettings().jsBundleURL(forBundleRoot: "index")
#else
    Bundle.main.url(forResource: "main", withExtension: "jsbundle")
#endif
  }
}
