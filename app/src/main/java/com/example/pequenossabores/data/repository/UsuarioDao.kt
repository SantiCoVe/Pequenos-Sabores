package com.example.pequenossabores.data.repository

import androidx.room.*
import com.example.pequenossabores.data.model.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun obtenerPorCorreo(correo: String): UsuarioEntity?

    // Observable en tiempo real (Flow), como en la guía
    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    fun observarPorCorreo(correo: String): Flow<UsuarioEntity?>

    @Update
    suspend fun actualizar(usuario: UsuarioEntity)

    @Delete
    suspend fun eliminar(usuario: UsuarioEntity)
}