package com.rodrigovalverde.sistema5027.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class], version = 2) // <-- CAMBIAR A 2
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // <-- AÑADIR ESTO
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
