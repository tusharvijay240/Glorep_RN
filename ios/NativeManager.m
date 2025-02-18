//
//  NativeManager.m
//  Glorep_RN
//
//  Created by Tushar on 17/02/25.
//

#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"
#import "React/RCTEventEmitter.h"

@interface RCT_EXTERN_MODULE(SDKBridge, RCTEventEmitter)

RCT_EXTERN_METHOD(launchGetStartedView:(NSString *)workflowId userId:(NSString *)userId)
RCT_EXTERN_METHOD(dismissNativeScreen)

@end