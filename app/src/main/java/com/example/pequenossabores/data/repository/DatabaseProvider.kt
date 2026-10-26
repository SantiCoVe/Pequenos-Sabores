package com.example.pequenossabores.data.repository

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "pequenos_sabores_db"
            )
                .fallbackToDestructiveMigration() // al subir de versión recrea las tablas (solo para desarrollo)
                .build()
            INSTANCE = instance
            instance
        }
    }
}