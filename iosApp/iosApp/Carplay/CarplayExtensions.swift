//
//  CarplayExtensions.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 03.12.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import ComposeApp
import CarPlay

extension CPListTemplateItem {
    
    var playerItem: PlayerItem? {
        return userInfo as? PlayerItem
    }
    
}

extension PlayerItem {
    
    @MainActor
    func toCPListItem(playingItemId: String?) -> CPListItem {
        let item = CPListItem(text: title, detailText: subtitle)
        if let imageUrl {
            item.kf.setImage(with: URL(string: imageUrl))
        }
        item.userInfo = self
        item.isPlaying = id == playingItemId
        return item
    }
    
}

extension CPListTemplate {
    
    func updatePlayingItem(_ playingItemId: String?) {
        sections.first?.items.forEach {
            let listItem = ($0 as? CPListItem)
            listItem?.isPlaying = playingItemId == listItem?.playerItem?.id
        }
    }
    
    func getPlayerItems() -> [PlayerItem] {
        return sections.first?.items.compactMap { $0.playerItem } ?? []
    }
    
}
