package br.com.tasafe.utils

import android.content.Context
import android.content.Intent
import br.com.tasafe.ui.activity.MySitesActivity
import br.com.tasafe.ui.activity.NewSiteActivity

class NavigationUtil {

    companion object {
        fun goTo(context: Context, intent:Intent){
            context.startActivity(intent)
            //ContextCompat.startActivity(context,intent,null)
        }

        fun goTo(context: Context, classeDestino: Class<out Any>){
            val intent = Intent(context, classeDestino)
            context.startActivity(intent)
        }

        fun goToAndClearStack(context: Context, classeDestino: Class<out Any>){
            val intent = Intent(context, classeDestino)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

    }

}