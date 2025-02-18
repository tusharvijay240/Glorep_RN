//
//  SDKBridge.swift
//  Glorep_RN
//
//  Created by Tushar on 17/02/25.
//

import Foundation
import UIKit
import GloRep_SDK
import React

@objc(SDKBridge)
class SDKBridge: RCTEventEmitter, GetstartedViewDelegate {

  override init() {
    super.init()
  }

  @objc func launchGetStartedView(_ workflowId: String, userId: String) {
    DispatchQueue.main.async {
      if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
         let rootVC = scene.windows.first?.rootViewController {
        // Launch SDK with delegate
        SDKManager.launchGetstartedView(
          from: rootVC,
          workflowId: workflowId,
          userId: userId,
          delegate: self,
          title: "GloRep-SDK"
        )
      }
    }
  }

  // MARK: - GetstartedViewDelegate method
  func didWorkFlowCompleted(_ success: String) {
    DispatchQueue.main.async {
      if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
         let rootVC = scene.windows.first?.rootViewController {
        
        let alert = UIAlertController(title: "GloRep", message: success, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "OK", style: .default) { _ in
          self.dismissNativeScreen() // Call dismiss function
        }
        alert.addAction(okAction)
        rootVC.present(alert, animated: true)
      }
    }
    
    // Send event to React Native
    sendEvent(withName: "onWorkflowCompleted", body: ["success": success])
  }

  // MARK: - Dismiss SDK Screen from React Native
  @objc func dismissNativeScreen() {
    DispatchQueue.main.async {
      if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
         let rootVC = scene.windows.first?.rootViewController {
        rootVC.dismiss(animated: true, completion: nil)
      }
    }
  }

  // MARK: - React Native Event Emitter
  override func supportedEvents() -> [String]! {
    return ["onWorkflowCompleted"]
  }

  @objc static override func requiresMainQueueSetup() -> Bool {
    return true
  }
}

