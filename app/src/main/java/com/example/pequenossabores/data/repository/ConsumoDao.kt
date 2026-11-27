package com.example.pequenossabores.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pequenossabores.data.model.ConsumoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumoDao {

    @Insert
    suspend fun insertar(consumo: ConsumoEntity): Long

    @Query("""
        SELECT * FROM consumos
        WHERE correoUsuario = :correo
          AND fecha BETWEEN :inicio AND :fin
        ORDER BY fecha DESC
    """)
    fun observarPorPeriodo(
        correo: String,
        inicio: String,
        fin: String
    ): Flow<List<ConsumoEntity>>

    @Query("""
        SELECT * FROM consumos
        WHERE correoUsuario = :correo
          AND fecha = :fecha
        ORDER BY id DESC
    """)
    suspend fun obtenerPorFecha(
        correo: String,
        fecha: String
    ): List<ConsumoEntity>
}