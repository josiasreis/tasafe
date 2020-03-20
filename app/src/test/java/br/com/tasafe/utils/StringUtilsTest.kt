package br.com.tasafe.utils

import androidx.room.util.StringUtil
import org.junit.Test

import org.junit.Assert.*

class StringUtilsTest {

    @Test
    fun `when contains number`() {
       val result = "10c&".containsNumber()
        assertTrue(result)
    }

    @Test
    fun `when not contains number`() {
        val result = "ceacployhmzcbvsuw%$#@c&".containsNumber()
        assertFalse(result)
    }

    @Test
    fun `when contains letter`() {
        val result = "10c&".containsLetter()
        assertTrue(result)
    }

    @Test
    fun `when not contains letter`() {
        val result = "11&*()$%#12432".containsLetter()
        assertFalse(result)
    }

    @Test
    fun `when contains letter 2`() {
        val result = "11&*()a$%#12432".containsLetter()
        assertTrue(result)
    }

    @Test
    fun `when contains digit`() {
        val result = "11&*()a$%#12432".containsDigit()
        assertTrue(result)
    }

    @Test
    fun `when not contains digit`() {
        val result = "123456789abcdefghijlmnopqrstuvxz".containsDigit()
        assertFalse(result)
    }
}