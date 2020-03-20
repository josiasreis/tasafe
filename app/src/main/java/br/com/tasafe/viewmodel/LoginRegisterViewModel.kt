package br.com.tasafe.viewmodel

import android.app.Application
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.util.StringUtil
import br.com.tasafe.model.bo.LoginBO
import br.com.tasafe.model.dao.TaSafeDataBase
import br.com.tasafe.model.data.User
import br.com.tasafe.model.dto.KeyStoreDTO
import br.com.tasafe.model.repository.UserRepository
import br.com.tasafe.tasafe.R
import br.com.tasafe.utils.DeCryptor
import br.com.tasafe.utils.SecurityUtils
import br.com.tasafe.utils.isEmailValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private var loginBO: LoginBO
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private var _loginBase64Encrypted = MutableLiveData<KeyStoreDTO>()
    val loginBase64Encrypted: LiveData<KeyStoreDTO> = _loginBase64Encrypted

/*    private var _IVBase64Encrypted = MutableLiveData<String>()
    val IVBase64Encrypted: LiveData<String> = _IVBase64Encrypted*/

    private val _logged = MutableLiveData<Boolean>(false)
    var logged: LiveData<Boolean> = _logged

    private val _userSaved = MutableLiveData<Boolean>(false)
    var userSaved: LiveData<Boolean> = _userSaved

    val password = ObservableField<String>()

    init {
        _user.value = User(null,"","")
        val uiScope = CoroutineScope(Dispatchers.Main)
        val userDao = TaSafeDataBase.getDatabase(application,uiScope).userDAO()
        repository = UserRepository(userDao)
        loginBO = LoginBO.getInstance(repository)
    }

    fun register() = viewModelScope.launch {
       var encryptor = loginBO.registerUser(_user.value!!,password.get().toString())
       var chave = Base64.encodeToString(encryptor.encryption, Base64.DEFAULT)
        var iv = Base64.encodeToString(encryptor.iv, Base64.DEFAULT)
       var dto= KeyStoreDTO(iv,chave)
        _loginBase64Encrypted.value = dto
        _userSaved.value = true
    }

    fun login(loginBase64Encrypted:String?,ivBase64Encrypted:String?) {
        val arrayBytesEncrypted = Base64.decode(loginBase64Encrypted, Base64.NO_WRAP)
        val ivBytesEncrypted = Base64.decode(ivBase64Encrypted, Base64.NO_WRAP)
        var decryptor = DeCryptor()
       var decrypted = decryptor.decryptData("login",arrayBytesEncrypted,ivBytesEncrypted)
        val pass: String = password.get().toString()

        if(pass.equals(decrypted)){
            _logged.value = true
        }
        /*var lastLoggedIn: String? = null
        decrypted?.let {
            _logged.value = true
            lastLoggedIn = String(it, Charsets.UTF_8)
            Log.d("ULTIMO_LOGIN",lastLoggedIn)
        }*/
    }

    fun setUserSaved(saved:Boolean){
        _userSaved.value = saved
    }

    fun setUserLogged(saved:Boolean){
        _logged.postValue(saved)
    }

    fun validatePass():Boolean{
       return loginBO.isValidPass(password.get().toString())
    }

    fun isFormaValid(): String? {
        var valid = loginBO.isValidPass(password.get().toString())
        var nameEmpty = _user.value!!.nome.isEmpty()
        var emailEmpty = _user.value!!.email.isEmpty()
        var validEmail = _user.value!!.email.isEmailValid()
        if(!valid){
            return "Senha inválida"
        }else if(nameEmpty){
            return "Nome é obrigatório"
        }else if(emailEmpty){
            return "Email é obrigatório"
        }else if(validEmail){
            return "Email invalido"
        }else{
            return null
        }
    }
}

