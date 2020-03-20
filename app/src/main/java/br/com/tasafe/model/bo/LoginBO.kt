package br.com.tasafe.model.bo

import br.com.tasafe.model.data.User
import br.com.tasafe.model.repository.UserRepository
import br.com.tasafe.utils.*
import br.com.tasafe.utils.EnCryptor


/* CLASSE DE NEGOCIO RELACIONADA AO LOGIN : BUSINESS OBJECT PATTERN */
class LoginBO(private var repository: UserRepository) {

    /* REGISTRA USUARIO NO ROOM E REGISTRA SENHA COMO CERTIFICADO KEY STORE */
    suspend fun registerUser(user: User,pass:String): EnCryptor {
        repository.run { insert(user) }
        val encrypt = EnCryptor()
        encrypt.run {
            encryptText("login", pass)
        }
        return encrypt
    }

    companion object{
        fun getInstance(repository: UserRepository): LoginBO {
            return LoginBO(repository)
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
    }

}