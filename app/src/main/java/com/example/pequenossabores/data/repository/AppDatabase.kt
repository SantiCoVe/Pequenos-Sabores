package com.example.pequenossabores.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pequenossabores.data.model.PequeEntity
import com.example.pequenossabores.data.model.UsuarioEntity
import com.example.pequenossabores.data.model.FavoritoEntity

@Database(
    entities = [UsuarioEntity::class, PequeEntity::class, FavoritoEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pequeDao(): PequeDao
    abstract fun favoritoDao(): FavoritoDao
}