//
//  Koin.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 28.11.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import ComposeApp

class Koin {
    
    static let shared = Koin()
    
    private var koin: Koin_coreKoin!
    
    private(set) lazy var notificationHandler = {
        getClass(NotificationClickHandler.self)
    }()
    private(set) lazy var navigationHelper = {
        getClass(NavigationHelper.self)
    }()
    private(set) lazy var apiService = {
        getClass(ShkabajApiService.self)
    }()
    private(set) lazy var radioBookmarksManager = {
        getClass(RadioBookmarksManager.self)
    }()
    private(set) lazy var nowPlayingManager = {
        getClass(NowPlayingManager.self)
    }()
    
    func startKoin() {
        let koinApplication = Koin_iosKt.doInitKoinIos(
            analyticsTracker: PlatformAnalyticsTrackerImpl(),
            artworkLoader: MPNowPlayingInfoArtworkLoaderImpl(),
            nativeViewsProvider: NativeViewsProviderImpl(),
            chromeCastHelper: ChromeCastHelperImpl()
        )
        koin = koinApplication.koin
    }
    
    private func getClass<T: AnyObject>(_ type: T.Type, qualifier: Koin_coreQualifier? = nil) -> T {
        return koin.get(objCClass: type, qualifier: qualifier) as! T
    }
    
    private func getInterface(_ type: Protocol, qualifier: Koin_coreQualifier? = nil) -> Any {
        return koin.get(objCProtocol: type, qualifier: qualifier)
    }
    
}
