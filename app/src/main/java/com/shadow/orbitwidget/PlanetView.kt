// (#) Responsibility: Відображення позиції планети відповідно до поточного часу доби (синхронізація з годинником)
package com.shadow.orbitwidget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import java.util.Calendar

class PlanetView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var scaledBitmap: Bitmap? = null
    private var isInitialized = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.BLACK)

        val width = width.toFloat()
        val height = height.toFloat()
        if (width == 0f || height == 0f) return

        val radius = width.coerceAtMost(height) / 2f

        if (!isInitialized) {
            val resId = resources.getIdentifier("earth", "drawable", context.packageName)
            if (resId != 0) {
                val originalBitmap = BitmapFactory.decodeResource(resources, resId)
                if (originalBitmap != null) {
                    val targetHeight = (radius * 2).toInt()
                    val targetWidth = ((targetHeight.toFloat() / originalBitmap.height) * originalBitmap.width).toInt()
                    scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true)
                }
            }
            isInitialized = true
        }

        val bitmap = scaledBitmap ?: return

        // 1. Рахуємо поточний час: скільки хвилин пройшло з початку доби
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val totalMinutesPassed = (hour * 60) + minute

        // 2. Обчислюємо відсоток доби (від 0.0 до 1.0)
        val dayPercent = totalMinutesPassed.toFloat() / 1440f

        // 3. Зсув карти залежить від часу доби. Повний оберт (bitmap.width) за 24 години
        val xOffset = -(bitmap.width * dayPercent)

        // Малюємо кругле вікно
        val clipPath = Path().apply {
            addCircle(width / 2, height / 2, radius, Path.Direction.CW)
        }
        canvas.save()
        canvas.clipPath(clipPath)

        val yOffset = (height / 2) - radius

        // Малюємо основну карту
        canvas.drawBitmap(bitmap, xOffset, yOffset, null)
        
        // Малюємо дубль карти справа, щоб не було порожнечі при зсуві
        if (xOffset + bitmap.width < width) {
            canvas.drawBitmap(bitmap, xOffset + bitmap.width, yOffset, null)
        }

        canvas.restore()
    }
}
