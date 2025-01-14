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

    override fun shareNews(newsTitle: String) {
        shareContent(
            defaultText = "Check out the news: \"$newsTitle\" - ",
            emailHtmlText = "<html>Check out the news: \"$newsTitle\" -<a href=\"\">App Store</a>>"
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