package br.com.tasafe.viewmodel

import android.app.Application
import android.util.Base64
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.tasafe.model.bo.LoginBO
import br.com.tasafe.model.dao.TaSafeDataBase
import br.com.tasafe.model.data.User
import br.com.tasafe.model.dto.KeyStoreDTO
import br.com.tasafe.model.repository.UserRepository
import br.com.tasafe.utils.DeCryptor
import kotlinx.coroutines.launch


class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private var loginBO: LoginBO
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private var _loginBase64Encrypted = MutableLiveData<KeyStoreDTO>()
    val loginBase64Encrypted: LiveData<KeyStoreDTO> = _loginBase64Encrypted

    private val _logged = MutableLiveData<Boolean>(false)
    var logged: LiveData<Boolean> = _logged

    private val _userSaved = MutableLiveData<Boolean>(false)
    var userSaved: LiveData<Boolean> = _userSaved

    val password = ObservableField<String>()

    init {
        _user.value = User(null,"","")
        val userDao = TaSafeDataBase.getDatabase(application).userDAO()
        repository = UserRepository(userDao)
        loginBO = LoginBO.getInstance(repository)
    }

    fun register() = viewModelScope.launch {
       val encryptor = loginBO.registerUser(_user.value!!,password.get().toString())
       val chave = Base64.encodeToString(encryptor.encryption, Base64.DEFAULT)
        val iv = Base64.encodeToString(encryptor.iv, Base64.DEFAULT)
       val dto= KeyStoreDTO(iv,chave)
        _loginBase64Encrypted.value = dto
        //_userSaved.value = true
    }

    fun login(loginBase64Encrypted:String?,ivBase64Encrypted:String?) {
        val arrayBytesEncrypted = Base64.decode(loginBase64Encrypted, Base64.NO_WRAP)
        val ivBytesEncrypted = Base64.decode(ivBase64Encrypted, Base64.NO_WRAP)
        val decryptor = DeCryptor()
       val decrypted = decryptor.decryptData("login",arrayBytesEncrypted,ivBytesEncrypted)
        val pass: String = password.get().toString()

        if(pass == decrypted){
            _logged.value = true
        }
    }

    fun setUserSaved(saved:Boolean){
        _userSaved.value = saved
    }

    fun setUserLogged(saved:Boolean){
        _logged.postValue(saved)
    }

    fun validatePass():Boolean{
       return LoginBO.isValidPass(password.get().toString())
    }
}

