package br.com.tasafe.model.bo

import android.util.Base64
import br.com.tasafe.model.data.User
import br.com.tasafe.model.repository.UserRepository
import br.com.tasafe.utils.*
import br.com.tasafe.utils.EnCryptor
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap


/* CLASSE DE NEGOCIO RELACIONADA AO LOGIN : BUSINESS OBJECT PATTERN */
class LoginBO {

    var repository: UserRepository

    constructor(repository:UserRepository){
        this.repository = repository
    }

    fun isValidPass(pass:String):Boolean{
        if(pass.isEmpty()){
            return false
        }else if(pass.length < 8){
            return false
        }else if(! pass.containsNumber()){
            return false
        }else if(! pass.containsLetter()){
            return false
        }else if(! pass.containsDigit()){
            return false
        }else{
            return true
        }
    }

    /* REGISTRA USUARIO NO ROOM E REGISTRA SENHA COMO CERTIFICADO KEY STORE */
    suspend fun registerUser(user: User,pass:String): EnCryptor {
        repository.insert(user)
        var encryptor = EnCryptor()
        var arrayBytesChave = encryptor.encryptText("login",pass)
        return encryptor
        //return Base64.encodeToString(arrayBytesChave, Base64.DEFAULT)
    }

    companion object{
        fun getInstance(repository: UserRepository): LoginBO {
            return LoginBO(repository)
        }
    }

}