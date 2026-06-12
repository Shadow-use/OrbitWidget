// (#) Responsibility: Малювання ілюзії обертання планети за допомогою зсуву текстури earth.png
package com.shadow.orbitwidget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Path
import android.view.View

class PlanetView(context: Context) : View(context) {

    private var planetTexture: Bitmap? = null
    private var xOffset = 0f
    private val speed = 3f // Швидкість обертання Землі

    init {
        // Намагаємося завантажити картинку з ресурсів
        val resId = resources.getIdentifier("earth", "drawable", context.packageName)
        if (resId != 0) {
            planetTexture = BitmapFactory.decodeResource(resources, resId)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val texture = planetTexture ?: return // Якщо картинки немає, нічого не малюємо

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = width.coerceAtMost(height) / 2f

        // 1. Створюємо кругле вікно-маску по центру екрана
        val clipPath = Path().apply {
            addCircle(width / 2, height / 2, radius, Path.Direction.CW)
        }
        
        canvas.save()
        canvas.clipPath(clipPath) // Обрізаємо все, що виходить за межі кола

        // Масштабуємо текстуру під висоту нашого екрана, щоб вона не була занадто маленькою
        val scaledWidth = (height / texture.height) * texture.width

        // 2. Малюємо карту із поточним зсувом xOffset
        val srcBitmap = Bitmap.createScaledBitmap(texture, scaledWidth.toInt(), height.toInt(), true)
        canvas.drawBitmap(srcBitmap, xOffset, 0f, null)
        
        // Якщо карта посунулася і оголила лівий край, малюємо копію справа
        if (xOffset + srcBitmap.width < width) {
            canvas.drawBitmap(srcBitmap, xOffset + srcBitmap.width, 0f, null)
        }

        canvas.restore()

        // 3. Рухаємо карту для ефекту обертання
        xOffset -= speed
        if (Math.abs(xOffset) >= srcBitmap.width) {
            xOffset = 0f
        }

        // Запускаємо перемальовування наступного кадру
        invalidate()
    }
}
