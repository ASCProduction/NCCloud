//
//  NSError+Convenience.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 03.12.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

extension NSError {
    
    class func error(description: String, code: Int = -1) -> NSError {
        return NSError(domain: String(describing: self), code: code,
                       userInfo: [NSLocalizedDescriptionKey : description])
    }
    
}
