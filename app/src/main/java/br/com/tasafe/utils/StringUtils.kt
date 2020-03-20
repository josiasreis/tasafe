package br.com.tasafe.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.containsNumber() = Pattern.compile("[0-9]").matcher(this).find()

fun String.containsLetter() = Pattern.compile("[a-zA-Z]").matcher(this).find()

fun String.containsDigit() : Boolean{
    val p =
        Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
    val m1: Matcher = p.matcher(this)
    val flag: Boolean = m1.find()
    return flag
}

fun String.isEmailValid() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()