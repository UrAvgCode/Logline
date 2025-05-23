package com.patch4code.logline

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.patch4code.logline.features.navigation.presentation.screen_navigation.Navigation
import com.patch4code.logline.room_database.LoglineDatabase
import com.patch4code.logline.ui.theme.LoglineTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * MainActivity - Entry point of the App.
 *
 * @author Patch4Code
 */
class MainActivity : ComponentActivity() {

    // Initializes the database instance
    private val db by lazy {
        Room.databaseBuilder(
            context = applicationContext,
            klass = LoglineDatabase::class.java,
            name = "loglineData.db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    db.runInTransaction { } // Empty transaction to initialize database
                }
            } catch (e: Exception) {
                Log.e("DatabaseInit", "Error initializing database", e)
            }
        }

        enableEdgeToEdge()
        setContent {
            LoglineTheme {
                Navigation(db)
            }
        }
    }
}
