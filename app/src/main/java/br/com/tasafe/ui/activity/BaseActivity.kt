package br.com.tasafe.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.tasafe.tasafe.R

open class BaseActivity : AppCompatActivity() {

    override fun finish() {
        super.finish()
            overridePendingTransition(R.anim.slide_nothing, R.anim.slide_out_right)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_nothing)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_nothing)
    }
}
