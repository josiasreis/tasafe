package br.com.tasafe.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.tasafe.model.data.Site

@Dao
interface SiteDAO {

    @Insert
    suspend fun save(site: Site)

    @Update
    suspend fun update(site: Site)

    @Query("DELETE FROM TB_SITE")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(site: Site)

    @Query("DELETE FROM TB_SITE where idSite = :idSite")
    suspend fun deleteById(idSite:Int)

    @Query("SELECT * from TB_SITE ORDER BY nomeSite ASC")
    fun findAll(): LiveData<List<Site>>

    @Query("SELECT * FROM TB_SITE where idSite = :idSite")
    suspend fun findById(idSite:Int):Site

}