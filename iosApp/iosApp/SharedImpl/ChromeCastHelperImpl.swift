//
//  ChromeCastHelperImpl.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 09.12.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import GoogleCast
import ComposeApp

class ChromeCastHelperImpl: NSObject, ChromeCastHelper {
    
    enum Action {
        case play
        case pause
        case stop
    }
    
    private let appId = "F87CE75A"
    
    var eventsHandler: ((ChromeCastHelperEvent) -> Void)?
    
    override init() {
        super.init()
        
        GCKLogger.sharedInstance().delegate = self
        
        let criteria = GCKDiscoveryCriteria(applicationID: appId)
        
        let options = GCKCastOptions(discoveryCriteria: criteria)
        options.physicalVolumeButtonsWillControlDeviceVolume = false
        options.disableDiscoveryAutostart = false
        
        GCKCastContext.setSharedInstanceWith(options)
        GCKCastContext.sharedInstance().useDefaultExpandedMediaControls = true
        
        let expandVC = GCKCastContext.sharedInstance().defaultExpandedMediaControlsViewController
        expandVC.hideStreamPositionControlsForLiveContent = false
        //set rewind/forward 30 sec and subtitles buttons to nil
        expandVC.setButtonType(.none, at: 0)
        expandVC.setButtonType(.none, at: 1)
        expandVC.setButtonType(.none, at: 2)
        
        GCKCastContext.sharedInstance().sessionManager.add(self)
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(presentExpandedMediaControls),
            name: NSNotification.Name.gckExpandedMediaControlsTriggered,
            object: nil
        )
        
        customizeCastViewStyle()
    }
    
    func castItem(item: PlayerItem, startImmediately: Bool) {
        let metadata = GCKMediaMetadata(metadataType: .generic)
        metadata.setString(item.title ?? "", forKey: kGCKMetadataKeyTitle)
        metadata.setString(item.subtitle ?? "", forKey: kGCKMetadataKeySubtitle)
        if let image = item.imageUrl, let imageUrl = URL(string: image) {
            metadata.addImage(.init(url: imageUrl, width: 0, height: 0))
        }

        guard let contentUrl = URL(string: item.mediaUrl) else {
            return
        }
        let builder = GCKMediaInformationBuilder(contentURL: contentUrl)
        builder.contentType = "audio/mpeg"
        builder.streamType = .live
        builder.metadata = metadata
        builder.streamDuration = .infinity

        let mediaInfo = builder.build()
        let options = GCKMediaLoadOptions()
        options.autoplay = startImmediately

        if let session = GCKCastContext.sharedInstance().sessionManager.currentCastSession {
            session.remoteMediaClient?.loadMedia(mediaInfo, with: options)
        }
    }
    
    func isCastConnected() -> Bool {
        return GCKCastContext.sharedInstance().sessionManager.hasConnectedCastSession()
    }
    
    func endCastSession() {
        GCKCastContext.sharedInstance().sessionManager.endSessionAndStopCasting(true)
    }
    
    func play() {
        sendActionToCast(.play)
    }
    
    func pause() {
        sendActionToCast(.pause)
    }
    
    func stop() {
        sendActionToCast(.stop)
    }
    
    // MARK: - Private helpers
    
    private func sendActionToCast(_ action: Action) {
        guard let remoteClient = GCKCastContext.sharedInstance().sessionManager.currentSession?.remoteMediaClient else {
            return
        }
        switch action {
        case .play:
            remoteClient.play()
        case .pause:
            remoteClient.pause()
        case .stop:
            remoteClient.stop()
        }
    }
    
    private func onStartCastSession(_ session: GCKCastSession) {
        print("ChromeCastHelperImpl onStartCastSession")
        eventsHandler?(ChromeCastHelperEvent.StartedCastSession())
        session.remoteMediaClient?.add(self)
    }
    
    @objc private func presentExpandedMediaControls() {
        print("ChromeCastHelperImpl present expanded media controls")
        GCKCastContext.sharedInstance().presentDefaultExpandedMediaControls()
    }
    
    private func customizeCastViewStyle() {
        let style = GCKUIStyle.sharedInstance()
        
        style.castViews.backgroundColor = .white
        style.castViews.deviceControl.connectionController.navigation.backgroundColor = .white
        style.castViews.deviceControl.backgroundColor = .white
        style.castViews.deviceControl.bodyTextColor = .white
        style.castViews.deviceControl.headingTextColor = .black
        style.castViews.deviceControl.iconTintColor = .black
        style.castViews.deviceControl.captionTextColor = .black
        
        // LNA interstitial dialog style
        style.castViews.bodyTextColor = .black
        style.castViews.iconTintColor = .black
        style.castViews.headingTextColor = .black
        
        // change stop image to pause image
        let pauseImage = style.castViews.deviceControl.connectionController.pauseImage
        style.castViews.deviceControl.connectionController.stopImage = pauseImage
        
        style.apply()
    }
    
}

// MARK: - GCKSessionManagerListener

extension ChromeCastHelperImpl: GCKRemoteMediaClientListener {
    
    func remoteMediaClient(_ client: GCKRemoteMediaClient, didUpdate mediaStatus: GCKMediaStatus?) {
        let playerState = mediaStatus?.playerState
        if playerState == .playing {
            eventsHandler?(ChromeCastHelperEvent.RemotePlayerPlay())
        } else {
            eventsHandler?(ChromeCastHelperEvent.RemotePlayerPause())
        }
    }

}

// MARK: - GCKSessionManagerListener

extension ChromeCastHelperImpl: GCKSessionManagerListener {
    
    func sessionManager(_ sessionManager: GCKSessionManager, didStart session: GCKCastSession) {
        onStartCastSession(session)
    }
    
    func sessionManager(_ sessionManager: GCKSessionManager, didResumeCastSession session: GCKCastSession) {
        onStartCastSession(session)
    }
    
    func sessionManager(_ sessionManager: GCKSessionManager, didEnd session: GCKCastSession, withError error: (any Error)?) {
        eventsHandler?(ChromeCastHelperEvent.EndedCastSession())
    }

}

// MARK: - GCKLoggerDelegate

extension ChromeCastHelperImpl: GCKLoggerDelegate {
    
    func logMessage(_ message: String, at level: GCKLoggerLevel, fromFunction function: String, location: String) {
        print("ChromeCastHelperImpl log \(function) \(message)")
    }
    
}
