//
//  CarPlaySceneDelegate.swift
//  shkabaj-kmm-ios
//
//  Created by Andrey K on 02.12.2024.
//  Copyright © 2024 orgName. All rights reserved.
//


import CarPlay
import UIKit

class CarPlaySceneDelegate: NSObject, CPTemplateApplicationSceneDelegate {
    
    private let templateManager = CarPlayTemplateManager()
    
    func templateApplicationScene(_ templateApplicationScene: CPTemplateApplicationScene, didConnect interfaceController: CPInterfaceController) {
        templateManager.connect(interfaceController)
    }
    
    func templateApplicationScene(_ templateApplicationScene: CPTemplateApplicationScene,
                                  didDisconnectInterfaceController interfaceController: CPInterfaceController) {
        templateManager.disconnect()
    }
    
}
