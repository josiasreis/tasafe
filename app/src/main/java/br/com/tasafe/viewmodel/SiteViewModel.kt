package br.com.tasafe.viewmodel

import android.app.Application
import android.util.Base64
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.com.tasafe.model.bo.SiteBO
import br.com.tasafe.model.dao.TaSafeDataBase
import br.com.tasafe.model.data.Site
import br.com.tasafe.model.repository.SiteRepository
import br.com.tasafe.utils.DeCryptor
import kotlinx.coroutines.launch

class SiteViewModel(application: Application) : AndroidViewModel(application) {

    private var siteBO: SiteBO
    var repository: SiteRepository

    val sites: LiveData<List<Site>>

    val password = ObservableField<String>()

    init {
        val siteDAO = TaSafeDataBase.getDatabase(application).siteDAO()
        repository = SiteRepository(siteDAO)
        siteBO = SiteBO.getInstance(repository)
        sites = repository.sites
    }

    fun showPass(site: Site): String {
        val arrayBytesEncrypted = Base64.decode(site.encrypted, Base64.NO_WRAP)
        val ivBytesEncrypted = Base64.decode(site.iv, Base64.NO_WRAP)
        val decryptor = DeCryptor()
        val decrypted =
            decryptor.decryptData("chavePrivadaLogins", arrayBytesEncrypted, ivBytesEncrypted)
        return decrypted
    }

    fun delete(site: Site) = viewModelScope.launch {
        repository.delete(site)
    }


}