//
//  MPNowPlayingInfoArtworkLoaderImpl.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 03.12.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import ComposeApp
import UIKit
import Kingfisher

class MPNowPlayingInfoArtworkLoaderImpl: MPNowPlayingInfoArtworkLoader {
    
    func loadArtwork(imageUrl: String, completionHandler: @escaping (UIImage?, (any Error)?) -> Void) {
        guard let url = URL(string: imageUrl) else {
            completionHandler(nil, NSError.error(description: "Wrong url"))
            return
        }
        KingfisherManager.shared.retrieveImage(with: url) { result in
            switch result {
            case .success(let img):
                completionHandler(img.image, nil)
            case .failure(let error):
                completionHandler(nil, error)
            }
        }
    }

}
