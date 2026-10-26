package com.example.pequenossabores.data.repository

import androidx.room.*
import com.example.pequenossabores.data.model.FavoritoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(favorito: FavoritoEntity)

    @Delete
    suspend fun eliminar(favorito: FavoritoEntity)

    @Query("SELECT recetaId FROM favoritos WHERE correoUsuario = :correo")
    fun observarIds(correo: String): Flow<List<Int>>
}