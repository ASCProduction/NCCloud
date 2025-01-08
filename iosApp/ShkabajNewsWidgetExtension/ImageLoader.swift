//
//  ImageLoader.swift
//  ShkabajNewsWidgetExtension
//
//  Created by Andrey Karaulov on 08.10.2020.
//  Copyright © 2020 Shkabaj. All rights reserved.
//

import UIKit

class ImageLoader {
    
    private let cache = NSCache<NSURL, UIImage>()
    
    init() {
        cache.countLimit = 5
    }
    
    func loadImageFromURL(_ url: URL, completion: @escaping (UIImage?) -> Void) {
        let nsURL = url as NSURL
        if let cachedImage = cache.object(forKey: nsURL) {
            completion(cachedImage)
            return
        }
        URLSession.shared.dataTask(with: url) { [weak self] (data, _, _) in
            guard let imageData = data, let img = UIImage(data: imageData) else {
                completion(nil)
                return
            }
            self?.cache.setObject(img, forKey: nsURL, cost: imageData.count)
            completion(img)
        }.resume()
    }
    
}
