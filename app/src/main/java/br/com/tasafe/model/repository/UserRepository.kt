package br.com.tasafe.model.repository

import br.com.tasafe.model.dao.UserDAO
import br.com.tasafe.model.data.User

class UserRepository(private val userDAO: UserDAO) {

    suspend fun insert(user: User) {
        userDAO.save(user)
    }

}