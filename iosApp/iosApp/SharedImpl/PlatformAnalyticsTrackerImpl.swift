//
//  PlatformAnalyticsTrackerImpl.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 02.12.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import ComposeApp
import FirebaseAnalytics

class PlatformAnalyticsTrackerImpl: PlatformAnalyticsTracker {
    
    func trackEvent(event: AnalyticsEvent) {
        let key = event.eventName.replacingOccurrences(of: " ", with: "")
        let shortValue = event.value.count > 100 ? String(event.value.prefix(100)) : event.value
        Analytics.logEvent("shkabaj_event", parameters: [key: shortValue])
    }
    
    func trackScreen(screen: AnalyticsScreen) {
        Analytics.logEvent(AnalyticsEventScreenView,
                           parameters: [AnalyticsParameterScreenName: screen.screenName])
    }
    
}
