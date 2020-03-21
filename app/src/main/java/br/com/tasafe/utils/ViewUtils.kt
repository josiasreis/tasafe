package br.com.tasafe.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.widget.EditText

fun View.hide() {
    this.visibility = View.GONE
}

/*fun View.show() = {
    this.visibility = View.VISIBLE
}*/

fun convertDpToPixel(dp: Float, context: Context?): Float {
    return if (context != null) {
        val resources = context.resources
        val metrics = resources.displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    } else {
        val metrics = Resources.getSystem().displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}

fun EditText.isEmpty() =  this.text.toString().isEmpty()

fun Context.generateDashedLineDrawable(
    backgroundColor: Int = Color.WHITE,
    strokeColor: Int = Color.BLACK
): Drawable {
    val dip1 = convertDpToPixel(1f, this)

    // create a bitmap to draw our dash
    val bitmap = Bitmap.createBitmap((5f * dip1).toInt(), dip1.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint()

    // fill the bitmap with the background colour of the list items
    canvas.drawColor(backgroundColor)

    // create a dash effect dash with = 10 dip, dash gap = 5 dip
    paint.pathEffect = DashPathEffect(floatArrayOf(1f * dip1, 3f * dip1), 0f)

    // draw a single pixel wide line across the bitmap
    paint.strokeWidth = dip1
    paint.color = strokeColor
    paint.style = Paint.Style.STROKE
    canvas.drawLine(0f, dip1 / 2f, 5f * dip1, dip1 / 2f, paint)

    // now create a tiled drawable using the bitmap
    val drawable = BitmapDrawable(resources, bitmap)
    drawable.tileModeX = Shader.TileMode.REPEAT

    return drawable
}

