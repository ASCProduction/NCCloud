//
//  WidgetNewsLoader.swift
//  shkabaj
//
//  Created by Andrey Karaulov on 07.10.2020.
//  Copyright © 2020 Shkabaj. All rights reserved.
//

import XMLDictionary

@objc class WidgetNewsLoader: NSObject {
    
    private let newsURL = URL(string: "https://www.shkabaj.net/news/updates/shkabaj.xml")!
    private let session: URLSession
    private var currentTask: URLSessionDataTask?
    private var completions = [([[String: Any]]) -> Void]()
    
    override init() {
        let config = URLSessionConfiguration.default
        config.requestCachePolicy = .reloadIgnoringCacheData
        config.timeoutIntervalForRequest = 30
        config.urlCache = nil
        self.session = URLSession(configuration: config)
        super.init()
    }
    
    @objc func loadNewsWithCompletion(_ completion: @escaping ([[String: Any]]) -> Void) {
        /*
            we save all the completions to prevent multiple requests when there are several
            widgets of different sizes on the screen (each call this request at the same time,
            for example, on the screen for adding/selecting a widget size)
        */
        completions.append(completion)
        guard currentTask == nil else { return }

        currentTask = session.dataTask(with: newsURL) { [weak self] (data, response, error) in
            self?.currentTask = nil
            guard let newsData = data, let newsList = XMLDictionaryParser().dictionary(with: newsData) else {
                self?.callCompletionsWithData([])
                return
            }
            var newsListForWidget = [[String: Any]]()
            var news = (newsList["news"] as? [[String: Any]]) ?? [[:]]
            if news.count > 0 {
                newsListForWidget = [news[0]]
                news.remove(at: 0)
                let filtered = news.filter { (item) -> Bool in
                    return item["_ioswidget"] as? String == "true"
                }
                newsListForWidget.append(contentsOf: filtered)
            }
            self?.callCompletionsWithData(newsListForWidget)
        }
        currentTask?.resume()
    }
    
    private func callCompletionsWithData(_ data: [[String: Any]]) {
        completions.forEach({ $0(data) })
        completions = []
    }
    
}
