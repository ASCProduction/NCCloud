import SwiftUI
import ComposeApp
import FirebaseCore
import OneSignalFramework

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    onOpenURL(url)
                }
        }
    }
    
    private func onOpenURL(_ url: URL) {
        if url == Constants.widgetURL {
            Koin.shared.navigationHelper.openTab(tab: .news)
        }
    }
    
}

class AppDelegate: NSObject, UIApplicationDelegate {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        Koin.shared.startKoin()
        MigrationManager.shared.migrateIfNeeded()
        AudioSessionHelper.shared.configureSession()
        PushNotificationsManager.shared.initializeWithLaunchOptions(launchOptions)
        return true
    }
    
}
