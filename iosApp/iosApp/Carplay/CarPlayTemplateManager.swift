//
//  CarPlayTemplateManager.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 02.12.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import CarPlay
import ComposeApp
import Kingfisher

class CarPlayTemplateManager {
    
    private enum Tab {
        case radios
        case favoriteRadios
    }
    
    private var controller: CPInterfaceController?
    private var nowPlayingManagerCloseable: Closeable?
    private var radiosBookmarksCloseable: Closeable?
    
    private let apiService = Koin.shared.apiService
    private let radioBookmarksManager = Koin.shared.radioBookmarksManager
    private let nowPlayingManager = Koin.shared.nowPlayingManager
    private var playingItem: PlayerItem? = nil {
        didSet { updatePlayableState() }
    }
    
    private let radiosTab = {
        let template = CPListTemplate(title: "Të gjitha", sections: [])
        template.tabImage = UIImage(named: "ic_cp_radio")
        template.emptyViewTitleVariants = ["Diçka shkoi keq"]
        return template
    }()
    private let favoriteRadiosTab = {
        let template = CPListTemplate(title: "Të preferuarat", sections: [])
        template.tabImage = UIImage(named: "ic_cp_favorite_radio")
        template.emptyViewTitleVariants = ["Radiot tuaja të preferuara nshfaqen këtu"]
        return template
    }()
    
    @MainActor
    func connect(_ interfaceController: CPInterfaceController) {
        controller = interfaceController
        subscribeOnUpdates()
        
        [radiosTab, favoriteRadiosTab].forEach {
            $0.updateSections([getLoadingSection()])
        }
        let tabBarTemplate = CPTabBarTemplate(templates: [radiosTab, favoriteRadiosTab])
        controller?.setRootTemplate(tabBarTemplate, animated: true, completion: nil)
        
        updateRadiosTabContent()
        updateFavoriteRadiosTabContent()
    }
    
    func disconnect() {
        nowPlayingManagerCloseable?.close()
        radiosBookmarksCloseable?.close()
    }
    
    // MARK: - Private handlers
    
    private func handleClickOnItem(_ item: CPListItem, tab: Tab,
                                   completion: @escaping () -> Void) {
        guard let playerItem = item.userInfo as? PlayerItem else { return }
        let items: [PlayerItem]
        switch tab {
        case .radios:
            items = radiosTab.getPlayerItems()
        case .favoriteRadios:
            items = favoriteRadiosTab.getPlayerItems()
        }
        
        let index = items.firstIndex(where: { $0.id == playerItem.id }) ?? 0
        nowPlayingManager.play(items: items, startIndex: Int32(index))
        
        let template = CPNowPlayingTemplate.shared
        controller?.pushTemplate(template, animated: true, completion: nil)
        completion()
    }
    
    // MARK: - Private helpers
    
    private func subscribeOnUpdates() {
        nowPlayingManagerCloseable = nowPlayingManager.watchState().watch { [weak self] state in
            self?.playingItem = state.currentItem
        }
    }
    
    private func updatePlayableState() {
        [radiosTab, favoriteRadiosTab].forEach {
            $0.updatePlayingItem(playingItem?.id)
        }
    }
    
    @MainActor
    private func updateRadiosTabContent() {
        if radiosTab.sections.isEmpty {
            radiosTab.updateSections([getLoadingSection()])
        }
        apiService.getRadioListItems { [weak self] radios, error in
            if error != nil {
                self?.radiosTab.updateSections([])
            } else if let radios {
                let items = radios.map { radio in
                    let playerItem = radio.toPlayerItem()
                    let item = playerItem.toCPListItem(playingItemId: self?.playingItem?.id)
                    item.handler = { [weak self] _, completion in
                        self?.handleClickOnItem(item, tab: .radios, completion: completion)
                    }
                    return item
                }
                let sections = CPListSection(items: items)
                self?.radiosTab.updateSections([sections])
            }
        }
    }
    
    @MainActor
    private func updateFavoriteRadiosTabContent() {
        if favoriteRadiosTab.sections.isEmpty {
            favoriteRadiosTab.updateSections([getLoadingSection()])
        }
        radiosBookmarksCloseable = radioBookmarksManager.watchState().watch { [weak self] bookmarks in
            let radios = (bookmarks as? [RadioDatabaseEntity] ?? []).map { $0.toUIModel() }
            let items = radios.map { radio in
                let playerItem = radio.toPlayerItem()
                let item = playerItem.toCPListItem(playingItemId: self?.playingItem?.id)
                item.handler = { [weak self] _, completion in
                    self?.handleClickOnItem(item, tab: .radios, completion: completion)
                }
                return item
            }
            let sections = CPListSection(items: items)
            self?.favoriteRadiosTab.updateSections([sections])
            
        }
    }
    
    private func getLoadingSection() -> CPListSection {
        let loading = CPListItem(text: "Duke u ngarkuar...", detailText: nil)
        if #available(iOS 15.0, *) {
            loading.isEnabled = false
        }
        return CPListSection(items: [loading])
    }
    
}
