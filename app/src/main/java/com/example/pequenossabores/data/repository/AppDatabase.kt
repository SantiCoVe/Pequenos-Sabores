package com.example.pequenossabores.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pequenossabores.data.model.ConsumoEntity
import com.example.pequenossabores.data.model.FavoritoEntity
import com.example.pequenossabores.data.model.PequeEntity
import com.example.pequenossabores.data.model.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        PequeEntity::class,
        FavoritoEntity::class,
        ConsumoEntity::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun pequeDao(): PequeDao

    abstract fun favoritoDao(): FavoritoDao

    abstract fun consumoDao(): ConsumoDao
}