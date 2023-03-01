package de.max.troegel.gmaf.app

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import de.max.troegel.gmaf.ui.fragment.HomeViewPagerFragment
import de.swa.gc.GraphCode
import gurtek.com.offlinedictionary.Dictionary
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.roundToInt

/**
 * Loads raw image data in the ImageView
 *
 * @param data The raw image data as ByteArray
 */
fun ImageView.load(data: ByteArray?) {
    Glide.with(context)
        .load(data)
        .transition(withCrossFade())
        .into(this)
}

/**
 * Loads raw image data in the ImageView, with a specific (low) resolution
 *
 * @param data The raw image data as ByteArray
 * @param sizeMultiplier The image scale factor [0..1]
 */
fun ImageView.loadWithThumbnail(data: ByteArray?, sizeMultiplier: Float = 0.25f) {
    Glide.with(context)
        .load(data)
        .thumbnail(sizeMultiplier)
        .transition(withCrossFade())
        .into(this)
}

/**
 * Returns the PrimaryNavigationFragment that can be used to navigate to another Fragment
 */
fun Fragment.getNavigationParent(): HomeViewPagerFragment? {
    return try {
        val navHostFragment = requireActivity().supportFragmentManager.fragments[0] as NavHostFragment
        navHostFragment.childFragmentManager.primaryNavigationFragment as HomeViewPagerFragment
    } catch (exception: Exception) {
        null
    }
}

/**
 * Extracts the file name from the url string
 */
fun String.extractFileNameFromUrl(): String {
    return if (indexOf("/") < length - 1) substringAfterLast("/") else this
}

/**
 * Converts this string into a single word
 */
fun String.extractWord(): String {
    // Remove line breaks
    val singleLine = this.substringBefore("\n", this)
    // remove non letters and upper case letters
    val word = singleLine
        .filter { it.isLetter() }
        .lowercase()
    // remove plural form
    return if (Dictionary.getEnglishDictionary().searchWord(word).isEmpty()) word.removeSuffix("s") else word
}

fun GraphCode.fromDict(dict: List<String>): GraphCode {
    dictionary = Vector(dict)
    return this
}

/**
 * The image quality in percent where 1 is low and 100 is high quality
 */
private const val fullQuality = 45

/**
 * Transforms the bitmap into its raw data array
 */
fun Bitmap.toData(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, fullQuality, stream)
    val byteArray: ByteArray = stream.toByteArray()
    stream.close()
    recycle()
    return byteArray
}

/**
 * The image size in pixels
 */
private const val previewSize = 280

fun Drawable.getHorizontalRatio(): Double {
    return intrinsicWidth.toDouble() / intrinsicHeight
}

fun Drawable.getVerticalRatio(): Double {
    return intrinsicHeight.toDouble() / intrinsicWidth
}

fun Drawable.getPreviewWidth(): Int {
    return if (intrinsicWidth > 0) (getHorizontalRatio() * previewSize).roundToInt() else previewSize
}

fun Drawable.getPreviewHeight(): Int {
    return if (intrinsicHeight > 0) (getVerticalRatio() * previewSize).roundToInt() else previewSize
}

@ColorInt
fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt()
    val red: Int = Color.red(this)
    val green: Int = Color.green(this)
    val blue: Int = Color.blue(this)
    return Color.argb(alpha, red, green, blue)
}