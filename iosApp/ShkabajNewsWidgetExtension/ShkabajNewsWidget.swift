//
//  ShkabajNewsWidget.swift
//  ShkabajNewsWidget
//
//  Created by Andrey Karaulov on 07.10.2020.
//  Copyright © 2020 Shkabaj. All rights reserved.
//

import WidgetKit
import SwiftUI

struct NewsProvider: TimelineProvider {
    
    private let updatesInterval = 30 // minutes
    private let newsLoader = WidgetNewsLoader()
    private let imageLoader = ImageLoader()
    
    func placeholder(in context: Context) -> TopNewsListEntry {
        return TopNewsListEntry.placeholderForWidgeWithItemsCount(newsCountForWidgetFamily(context.family))
    }

    func getSnapshot(in context: Context, completion: @escaping (TopNewsListEntry) -> ()) {
        newsLoader.loadNewsWithCompletion({ newsData in
            createEntryWithNews(newsData, newsCount: newsCountForWidgetFamily(context.family)) { (entry) in
                completion(entry)
            }
        })
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<TopNewsListEntry>) -> ()) {
        newsLoader.loadNewsWithCompletion({ newsData in
            createEntryWithNews(newsData, newsCount: newsCountForWidgetFamily(context.family)) { (entry) in
                let nextUpdateDate = Calendar.current.date(byAdding: .minute, value: updatesInterval, to: Date())!
                completion(Timeline(entries: [entry], policy: .after(nextUpdateDate)))
            }
        })
    }
    
    private func createEntryWithNews(_ newsData: [[String: Any]], newsCount: Int, completion: @escaping (TopNewsListEntry) -> Void) {
        let news = Array(NewsEntry.newsFromArray(newsData).prefix(newsCount))
        var entry = TopNewsListEntry(date: Date(), news: news)
        
        let imageRequestGroup = DispatchGroup()
        for (index, item) in entry.news.enumerated() {
            guard let imageURL = item.imageURL else { continue }
            imageRequestGroup.enter()
            imageLoader.loadImageFromURL(imageURL) { (img) in
                entry.news[index].uiImage = img
                imageRequestGroup.leave()
            }
        }
        imageRequestGroup.notify(queue: .main) {
            completion(entry)
        }
    }
    
    private func newsCountForWidgetFamily(_ family: WidgetFamily) -> Int {
        return family == .systemLarge ? 5 : 2
    }
    
}

@main
struct ShkabajNewsWidget: Widget {
    let kind: String = "ShkabajNewsWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: NewsProvider()) { entry in
            ShkabajNewsWidgetView(newsListEntry: entry)
        }
        .supportedFamilies([.systemMedium, .systemLarge])
        .configurationDisplayName("Lajmet Kryesore")
        .description("Përzgjedhje e lajmeve kryesore të ditës")
        .contentMarginsDisabledIfAvailable()
    }
}

struct ShkabajNewsWidget_Previews: PreviewProvider {
    static var previews: some View {
        ShkabajNewsWidgetView(newsListEntry: TopNewsListEntry.placeholderForWidgeWithItemsCount(5))
            .previewContext(WidgetPreviewContext(family: .systemLarge))
    }
}

extension WidgetConfiguration {
    
    func contentMarginsDisabledIfAvailable() -> some WidgetConfiguration {
        if #available(iOSApplicationExtension 17.0, *) {
            return contentMarginsDisabled()
        }
        return self
    }
    
}
