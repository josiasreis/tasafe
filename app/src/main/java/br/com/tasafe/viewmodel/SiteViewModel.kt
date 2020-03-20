package br.com.tasafe.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.tasafe.model.bo.SiteBO
import br.com.tasafe.model.dao.TaSafeDataBase
import br.com.tasafe.model.data.Site
import br.com.tasafe.model.data.User
import br.com.tasafe.model.dto.KeyStoreDTO
import br.com.tasafe.model.repository.SiteRepository
import br.com.tasafe.model.repository.UserRepository
import br.com.tasafe.ui.adapter.SiteAdapter
import br.com.tasafe.utils.DeCryptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SiteViewModel(application: Application) : AndroidViewModel(application) {

    private var siteBO: SiteBO
    var  repository: SiteRepository

    val sites: LiveData<List<Site>>

    val password = ObservableField<String>()
    private var myClipboard: ClipboardManager? = null
    init {
        val uiScope = CoroutineScope(Dispatchers.Main)
        val siteDAO = TaSafeDataBase.getDatabase(application,uiScope).siteDAO()
        repository = SiteRepository(siteDAO)
        siteBO = SiteBO.getInstance(repository)
       sites = repository.sites
    }

    fun showPass(site: Site) :String{
        val arrayBytesEncrypted = Base64.decode(site.encrypted, Base64.NO_WRAP)
        val ivBytesEncrypted = Base64.decode(site.iv, Base64.NO_WRAP)
        var decryptor = DeCryptor()
        var decrypted = decryptor.decryptData("chavePrivadaLogins",arrayBytesEncrypted,ivBytesEncrypted)
        return decrypted
       /* val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", decrypted)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()

        val myClip = ClipData.newPlainText("text",decrypted);
        val primaryClip = myClipboard?.setPrimaryClip(myClip);
        Toast.makeText(context, "Senha copiada para área de transferência", Toast.LENGTH_LONG).show();*/
       // adapter.showPass(position,decrypted)
    }

    fun delete(site:Site) = viewModelScope.launch {
        repository.delete(site)
    }


}