package com.example.pequenossabores.data.repository

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `consumos` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `correoUsuario` TEXT NOT NULL,
                    `recetaId` INTEGER NOT NULL,
                    `nombreReceta` TEXT NOT NULL,
                    `fecha` TEXT NOT NULL,
                    `gruposConsumidos` TEXT NOT NULL
                )
            """.trimIndent())

            db.execSQL("""
                CREATE INDEX IF NOT EXISTS `index_consumos_correoUsuario_fecha`
                ON `consumos` (`correoUsuario`, `fecha`)
            """.trimIndent())
        }
    }

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "pequenos_sabores_db"
            )
                .addMigrations(MIGRATION_2_3)
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}