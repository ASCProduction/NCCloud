package tk.shkabaj.android.shkabaj.widget.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.size.Size

class ImageLoaderManager(private val context: Context) {

    suspend fun loadImageAsBitmap(imageUrl: String): Bitmap? {
        return try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .size(Size.ORIGINAL)
                .build()
            val result = loader.execute(request) as? SuccessResult
            val image = result?.image ?: return null
            if (image is BitmapImage) {
                val bitmap = image.bitmap.copy(Bitmap.Config.ARGB_8888, true)
                return bitmap
            }
            val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            image.draw(canvas)
            Log.i("NewsWidget", "Success Loading Image: $imageUrl")
            bitmap
        } catch (e: Exception) {
            Log.e("NewsWidget", "Error loading image: $imageUrl", e)
            null
        }
    }
}