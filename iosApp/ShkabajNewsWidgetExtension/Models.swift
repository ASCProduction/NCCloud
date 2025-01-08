//
//  Models.swift
//  ShkabajNewsWidgetExtension
//
//  Created by Andrey Karaulov on 07.10.2020.
//  Copyright © 2020 Shkabaj. All rights reserved.
//

import WidgetKit
import UIKit

struct NewsEntry: Codable {
    var title: String
    var imageURI: String?
    var uiImage: UIImage?
    
    private enum CodingKeys: String, CodingKey {
        case title, imageURI
    }
}

extension NewsEntry {
    
    var imageURL: URL? {
        if let uri = imageURI, !uri.isEmpty {
            return URL(string: "https://www.shkabaj.net/news/updates/\(uri)")
        }
        return nil
    }
    
    static func newsFromArray(_ array: [[String: Any]]) -> [NewsEntry] {
        guard let data = try? JSONSerialization.data(withJSONObject: array, options: .prettyPrinted) else { return [] }
        return (try? JSONDecoder().decode([NewsEntry].self, from: data)) ?? []
    }
    
}

struct TopNewsListEntry: TimelineEntry {
    var date = Date()
    var news = [NewsEntry]()
}

extension TopNewsListEntry {
    
    static func placeholderForWidgeWithItemsCount(_ itemsCount: Int) -> TopNewsListEntry {
        let newsPlaceholders = [NewsEntry](repeating: NewsEntry(title: String(repeating: "-", count: 100), imageURI: nil), count: itemsCount)
        return TopNewsListEntry(date: Date(), news: newsPlaceholders)
    }

}
