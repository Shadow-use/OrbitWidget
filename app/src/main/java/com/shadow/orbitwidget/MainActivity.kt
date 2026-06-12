// (#) Responsibility: Запуск головного екрану додатка та відображення кастомного віджета планети
package com.shadow.orbitwidget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запускаємо нашу планету на весь екран
        val planetView = PlanetView(this)
        setContentView(planetView)
    }
}
