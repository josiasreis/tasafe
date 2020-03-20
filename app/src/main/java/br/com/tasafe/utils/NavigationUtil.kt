package br.com.tasafe.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class NavigationUtil {

    companion object {
        fun goTo(context: Context, intent:Intent){
            ContextCompat.startActivity(context,intent,null)
        }

        fun goTo(context: Context, classeDestino: Class<out Any>){
            var intent = Intent(context, classeDestino)
            ContextCompat.startActivity(context,intent,null)
        }

        fun goToAndClearStack(context: Context, classeDestino: Class<out Any>){
            var intent = Intent(context, classeDestino)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            ContextCompat.startActivity(context,intent,null)
        }



    }

}