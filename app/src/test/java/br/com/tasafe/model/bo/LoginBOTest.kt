package br.com.tasafe.model.bo

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import br.com.tasafe.model.dao.TaSafeDataBase
import br.com.tasafe.model.dao.UserDAO
import br.com.tasafe.model.data.User
import br.com.tasafe.model.repository.UserRepository
import br.com.tasafe.utils.EnCryptor
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class LoginBOTest {

    @Mock
    private lateinit var mockContext: Context
    lateinit var userDao : UserDAO
    lateinit var repository: UserRepository
    lateinit var loginBO : LoginBO

    @Before
    fun onCreateDB() {
        var db = Room.inMemoryDatabaseBuilder(mockContext, TaSafeDataBase::class.java).build()
        userDao = db!!.userDAO()
        repository = UserRepository(userDao)
        loginBO = LoginBO.getInstance(repository)
    }

    @Test
    fun isInValidPass() {
        val result = LoginBO.isValidPass("Abcd1efg")
        assertFalse(result)
    }

    @Test
    fun isValidPass() {
        val result = LoginBO.isValidPass("Abcd1efg#$")
        assertTrue(result)
    }
}