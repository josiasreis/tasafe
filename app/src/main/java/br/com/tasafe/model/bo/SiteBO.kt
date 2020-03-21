package br.com.tasafe.model.bo

import android.util.Base64.*
import br.com.tasafe.model.data.Site
import br.com.tasafe.model.repository.SiteRepository
import br.com.tasafe.utils.EnCryptor

class SiteBO(private var repository: SiteRepository) {

    /* REGISTRA USUARIO NO ROOM E REGISTRA SENHA COMO CERTIFICADO KEY STORE */
    suspend fun saveSite(site: Site, pass:String) {
        val encryptor = EnCryptor()
        encryptor.encryptText("chavePrivadaLogins", pass)
        val chave = encodeToString(encryptor.encryption, DEFAULT)
        val iv = encodeToString(encryptor.iv, DEFAULT)
        site.iv = iv
        site.encrypted = chave
        repository.insert(site)

    }

    companion object{
        fun getInstance(repository: SiteRepository): SiteBO {
            return SiteBO(repository)
        }
    }

}