//
//  PushNotificationsManager.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 28.11.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import OneSignalFramework

class PushNotificationsManager: NSObject {
    
    private let appId = "cc9d7360-fbe9-4764-98c3-12585e17f694"
    
    static let shared = PushNotificationsManager()
    
    private override init() {}
    
    func initializeWithLaunchOptions(_ launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) {
        // Remove this method to stop OneSignal Debugging
//        OneSignal.Debug.setLogLevel(.LL_VERBOSE)
        
        // OneSignal initialization
        OneSignal.initialize(appId, withLaunchOptions: launchOptions)
        
        // requestPermission will show the native iOS notification permission prompt.
        OneSignal.Notifications.requestPermission({ accepted in
            print("User accepted notifications: \(accepted)")
        }, fallbackToSettings: false)
        
        OneSignal.Notifications.addForegroundLifecycleListener(self)
        OneSignal.Notifications.addClickListener(self)
    }
    
}

// MARK: - OSNotificationLifecycleListener

extension PushNotificationsManager: OSNotificationLifecycleListener {
    
    func onWillDisplay(event: OSNotificationWillDisplayEvent) {
        if event.notification.notificationId == "silent_notif" {
            event.preventDefault()
        } else {
            event.notification.display()
        }
    }
    
}

// MARK: - OSNotificationClickListener

extension PushNotificationsManager: OSNotificationClickListener {
    
    func onClick(event: OSNotificationClickEvent) {
        let isAppActive = UIApplication.shared.applicationState == .active
        if let data = event.notification.additionalData as? [String: Any] {
            Koin.shared.notificationHandler.handleClick(data: data,
                                                        title: event.notification.body,
                                                        isAppActive: isAppActive)
        }
    }
    
}
