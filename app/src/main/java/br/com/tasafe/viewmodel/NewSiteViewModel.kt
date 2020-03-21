package br.com.tasafe.viewmodel

import android.app.Application
import android.util.Base64
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.tasafe.model.bo.SiteBO
import br.com.tasafe.model.dao.TaSafeDataBase
import br.com.tasafe.model.data.Site
import br.com.tasafe.model.repository.SiteRepository
import br.com.tasafe.utils.DeCryptor
import kotlinx.coroutines.launch

class NewSiteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SiteRepository
    private var siteBO: SiteBO
    private val _site = MutableLiveData<Site>()
    val site: LiveData<Site> = _site

    private val _viewMod = MutableLiveData<Boolean>()
    val viewMod: LiveData<Boolean> = _viewMod

    val password = ObservableField<String>("")

    init {
        val site = Site(null, "", "", "", "", "", "", "")
        _site.value = site
        val siteDao = TaSafeDataBase.getDatabase(application).siteDAO()
        repository = SiteRepository(siteDao)
        siteBO = SiteBO.getInstance(repository)
    }

    fun save() = viewModelScope.launch {
        try {
            siteBO.saveSite(site.value!!, password.get().toString())
        } catch (e: Exception) {
            //TODO REMOVER DA KEYSTORE E LANCAR MENSAGEM NA TELA
        }
    }

    fun loadViewMode(idSite: Int) = viewModelScope.launch {
        try {
            val site = repository.findById(idSite)
            _site.value = site
            val arrayBytesEncrypted = Base64.decode(site.encrypted, Base64.NO_WRAP)
            val ivBytesEncrypted = Base64.decode(site.iv, Base64.NO_WRAP)
            val decryptor = DeCryptor()
            val decrypted =
                decryptor.decryptData("chavePrivadaLogins", arrayBytesEncrypted, ivBytesEncrypted)
            password.set(decrypted)
            _viewMod.postValue(true)
        } catch (e: Exception) {
            //TODO LANCAR MENSAGEM NA TELA
        }
    }

}