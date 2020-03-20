package br.com.tasafe.model.bo

import android.util.Base64
import br.com.tasafe.model.data.Site
import br.com.tasafe.model.data.User
import br.com.tasafe.model.repository.SiteRepository
import br.com.tasafe.utils.EnCryptor

class SiteBO {

    var repository: SiteRepository

    /* REGISTRA USUARIO NO ROOM E REGISTRA SENHA COMO CERTIFICADO KEY STORE */
    suspend fun saveSite(site: Site, pass:String) {
        var encryptor = EnCryptor()
        encryptor.encryptText("chavePrivadaLogins",pass)
        var chave = Base64.encodeToString(encryptor.encryption, Base64.DEFAULT)
        var iv = Base64.encodeToString(encryptor.iv, Base64.DEFAULT)
        site.iv = iv
        site.encrypted = chave
        repository.insert(site)

    }

    constructor(repository: SiteRepository){
        this.repository = repository
    }

    companion object{
        fun getInstance(repository: SiteRepository): SiteBO {
            return SiteBO(repository)
        }
    }

}