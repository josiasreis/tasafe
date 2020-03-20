package br.com.tasafe.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.tasafe.model.dao.SiteDAO
import br.com.tasafe.model.data.Site

class SiteRepository (private val siteDAO: SiteDAO) {

    val sites: LiveData<List<Site>> = siteDAO.findAll()

    suspend fun insert(site: Site) {
        siteDAO.save(site)
    }
    suspend fun findById(id: Int):Site {
       return siteDAO.findById(id)
    }

   suspend fun delete(site: Site){
        siteDAO.delete(site)
    }
}