package com.example.pequenossabores.data.repository

import androidx.room.*
import com.example.pequenossabores.data.model.PequeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PequeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(peque: PequeEntity)

    @Query("SELECT * FROM peques WHERE correoUsuario = :correo")
    suspend fun obtener(correo: String): PequeEntity?

    @Query("SELECT * FROM peques WHERE correoUsuario = :correo")
    fun observar(correo: String): Flow<PequeEntity?>
}