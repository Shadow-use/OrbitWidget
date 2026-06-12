// (#) Responsibility: Оптимізоване малювання планети earth.png на чорному тлі зі зменшеною швидкістю
package com.shadow.orbitwidget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.view.View

class PlanetView(context: Context) : View(context) {

    private var scaledBitmap: Bitmap? = null
    private var xOffset = 0f
    private val speed = 1.5f // Зменшили швидкість у 2 рази
    private var isInitialized = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1. Фарбуємо фон у чорний космічний колір
        canvas.drawColor(Color.BLACK)

        val width = width.toFloat()
        val height = height.toFloat()
        if (width == 0f || height == 0f) return

        // Визначаємо розмір нашої планети (круглого вікна)
        val radius = width.coerceAtMost(height) / 3f // Трохи зменшили радіус, щоб планета акуратно вписалася

        // 2. Ініціалізація текстури (робимо ОДИН раз для уникнення ривків)
        if (!isInitialized) {
            val resId = resources.getIdentifier("earth", "drawable", context.packageName)
            if (resId != 0) {
                val originalBitmap = BitmapFactory.decodeResource(resources, resId)
                if (originalBitmap != null) {
                    // Масштабуємо карту так, щоб її висота дорівнювала діаметру нашого кола (2 * radius)
                    val targetHeight = (radius * 2).toInt()
                    val targetWidth = ((targetHeight.toFloat() / originalBitmap.height) * originalBitmap.width).toInt()
                    scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true)
                }
            }
            isInitialized = true
        }

        val bitmap = scaledBitmap ?: return

        // 3. Створюємо кругле вікно-маску по центру екрана
        val clipPath = Path().apply {
            addCircle(width / 2, height / 2, radius, Path.Direction.CW)
        }
        
        canvas.save()
        canvas.clipPath(clipPath) // Обрізаємо все за межами кола

        // Обчислюємо Y-координату, щоб відцентрувати карту по вертикалі всередині кола
        val yOffset = (height / 2) - radius

        // 4. Малюємо карту із поточним зсувом xOffset
        canvas.drawBitmap(bitmap, xOffset, yOffset, null)
        
        // Малюємо копію справа для безшовного стику
        if (xOffset + bitmap.width < width) {
            canvas.drawBitmap(bitmap, xOffset + bitmap.width, yOffset, null)
        }

        canvas.restore()

        // 5. Рухаємо карту для ефекту обертання
        xOffset -= speed
        if (Math.abs(xOffset) >= bitmap.width) {
            xOffset = 0f
        }

        // Запускаємо перемальовування наступного кадру (анімація)
        invalidate()
    }
}
