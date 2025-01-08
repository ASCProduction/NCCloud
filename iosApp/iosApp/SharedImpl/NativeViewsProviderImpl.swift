//
//  NativeViewsProviderImpl.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 09.12.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import ComposeApp
import GoogleCast

class NativeViewsProviderImpl: NativeViewsProvider {
    
    func getChromecastButtonView() -> UIView {
        let castButton = GCKUICastButton(frame: .zero)
        castButton.tintColor = UIColor(named: "AccentColor")
        castButton.backgroundColor = .clear
        
        let wrapper = UIView()
        wrapper.addSubview(castButton)
        castButton.translatesAutoresizingMaskIntoConstraints = false
        castButton.topAnchor.constraint(equalTo: wrapper.topAnchor).isActive = true
        castButton.bottomAnchor.constraint(equalTo: wrapper.bottomAnchor).isActive = true
        castButton.leadingAnchor.constraint(equalTo: wrapper.leadingAnchor).isActive = true
        castButton.trailingAnchor.constraint(equalTo: wrapper.trailingAnchor).isActive = true
        return wrapper
    }
    
}
