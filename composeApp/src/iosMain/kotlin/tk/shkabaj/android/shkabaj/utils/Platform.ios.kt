package tk.shkabaj.android.shkabaj.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSURLCache
import platform.Foundation.NSUserDomainMask
import platform.MessageUI.MFMailComposeResult
import platform.MessageUI.MFMailComposeViewController
import platform.MessageUI.MFMailComposeViewControllerDelegateProtocol
import platform.SafariServices.SFSafariViewController
import platform.UIKit.UIActivityItemSourceProtocol
import platform.UIKit.UIActivityType
import platform.UIKit.UIActivityTypeMail
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UIViewController
import platform.UIKit.childViewControllers
import platform.darwin.NSObject
import tk.shkabaj.android.shkabaj.extensions.toUIColor
import tk.shkabaj.android.shkabaj.ui.theme.MainBGDark
import tk.shkabaj.android.shkabaj.ui.theme.MainBGLight

actual val platformType = PlatformType.IOS

internal class IosPlatform : Platform {

    private val mfDelegate = MFMailComposeDelegate()

    override fun getAppVersion(): String {
        return NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "Unknown"
    }

    override fun sendSupportEmail() {
        if (MFMailComposeViewController.canSendMail().not()) return
        val subject = "Shkabaj App ${getAppVersion()} – iOS"
        val recipient = Constants.SHARE_EMAIL_ADDRESS

        val mailVC = MFMailComposeViewController()
        mailVC.mailComposeDelegate = mfDelegate
        mailVC.setSubject(subject)
        mailVC.setToRecipients(listOf(recipient))
        getRootViewController().presentViewController(mailVC, animated = true, completion = null)
    }

    override fun shareApp() {
        shareContent(
            defaultText = "Shkarkoni aplikacionin Shkabaj për iPhone dhe iPad falas në App Store ose përmes: ${Constants.SHARE_APP_LINK}",
            emailHtmlText = "<html>Më pëlqeu ky aplikacion nga Shkabaj për iPhone dhe iPad dhe mendova se ju do të pëlqeni gjithashtu! Shkarkoni këtë aplikacion falas në <a href=\"${Constants.APP_STORE_LINK}\">App Store</a> ose përmes ${Constants.SHARE_APP_LINK}>"
        )
    }

    override fun shareNews(newsTitle: String) {
        shareContent(
            defaultText = "Po i shikoj lajmet \"$newsTitle\" në aplikacionin Shkabaj për iPhone dhe iPad! Shkarkoni këtë aplikacion falas në App Store ose përmes: ${Constants.SHARE_APP_LINK}",
            emailHtmlText = "<html>Po i shikoj lajmet \"$newsTitle\" në aplikacionin Shkabaj për iPhone dhe iPad! Shkarkoni këtë aplikacion falas në <a href=\"${Constants.APP_STORE_LINK}\">App Store</a> ose përmes ${Constants.SHARE_APP_LINK}>"
        )
    }

    override fun shareRadio(radioName: String) {
        shareContent(
            defaultText = "Jam duke dëgjuar $radioName në aplikacionin Shkabaj për iPhone dhe iPad! Shkarkoni këtë aplikacion falas në App Store ose përmes: ${Constants.SHARE_APP_LINK}",
            emailHtmlText = "<html>Jam duke dëgjuar $radioName në aplikacionin Shkabaj për iPhone dhe iPad! Shkarkoni këtë aplikacion falas në <a href=\"${Constants.APP_STORE_LINK}\">App Store</a> ose përmes ${Constants.SHARE_APP_LINK}>"
        )
    }

    private fun shareContent(defaultText: String, emailHtmlText: String) {
        val activityVC = UIActivityViewController(
            activityItems = listOf(ShareItem(defaultText, emailHtmlText)),
            applicationActivities = null
        )
        getRootViewController().presentViewController(activityVC, animated = true, completion = null)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun clearCache() {
        NSURLCache.sharedURLCache.removeAllCachedResponses()

        val fileManager = NSFileManager.defaultManager
        val cacheUrls = NSSearchPathForDirectoriesInDomains(
            directory = platform.Foundation.NSCachesDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        )

        cacheUrls.firstOrNull()?.let { cacheDirPath ->
            val cacheDirUrl = NSURL.fileURLWithPath(cacheDirPath.toString())
            val cacheContents = fileManager.contentsOfDirectoryAtURL(
                cacheDirUrl,
                includingPropertiesForKeys = null,
                options = 0u,
                error = null
            )

            cacheContents?.forEach { fileUrl ->
                fileManager.removeItemAtURL(fileUrl as NSURL, null)
            }
        }
    }

    override fun openUrl(url: String) {
        val safariVC = SFSafariViewController(NSURL(string = url))
        safariVC.modalPresentationStyle = UIModalPresentationFullScreen
        getRootViewController().presentViewController(safariVC, animated = true, completion = null)
    }

    override fun changeThemeBars(isDark: Boolean) {
        val root = getRootViewController()
        val color = if (isDark) {
            MainBGDark.toUIColor()
        } else {
            MainBGLight.toUIColor()
        }
        getRootViewController().view.backgroundColor = color
        root.childViewControllers.forEach {
            (it as? UIViewController)?.view?.backgroundColor = color
        }
    }

    private fun getRootViewController(): UIViewController {
        return UIApplication.sharedApplication.keyWindow?.rootViewController ?: UIViewController()
    }

}

class ShareItem(
    private val defaultText: String,
    private val emailHtmlText: String
) : NSObject(), UIActivityItemSourceProtocol {

    override fun activityViewController(
        activityViewController: UIActivityViewController,
        itemForActivityType: UIActivityType
    ): String {
        return if (itemForActivityType == UIActivityTypeMail) {
            emailHtmlText
        } else {
            defaultText
        }
    }

    override fun activityViewControllerPlaceholderItem(activityViewController: UIActivityViewController): Any {
        return defaultText
    }
}

class MFMailComposeDelegate: NSObject(), MFMailComposeViewControllerDelegateProtocol {

    override fun mailComposeController(
        controller: MFMailComposeViewController,
        didFinishWithResult: MFMailComposeResult,
        error: NSError?
    ) {
        controller.dismissModalViewControllerAnimated(true)
    }

}