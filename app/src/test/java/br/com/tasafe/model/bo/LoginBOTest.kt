package br.com.tasafe.model.bo

import org.junit.Test

import org.junit.Assert.*

class LoginBOTest {

    val loginBO = LoginBO()

    @Test
    fun isValidPass() {
        val result = loginBO.isValidPass("Abcd1efg")
        assertTrue(result)
    }
}