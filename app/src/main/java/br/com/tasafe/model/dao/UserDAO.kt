package br.com.tasafe.model.dao

import androidx.room.*
import br.com.tasafe.model.data.User

@Dao
interface UserDAO {

    @Insert
    suspend fun save(local: User)

    @Update
    suspend fun update(local: User)

    @Query("DELETE FROM TB_USER")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM TB_USER where idUser = :idUser")
    suspend fun deleteById(idUser:Int)


}