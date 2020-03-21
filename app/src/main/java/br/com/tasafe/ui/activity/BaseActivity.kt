package br.com.tasafe.ui.activity

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.tasafe.tasafe.R
import kotlinx.android.synthetic.main.loading.*

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

    fun showLoading(){
        clFullLoading.visibility = View.VISIBLE
    }

    fun hideLoading(){
        clFullLoading.visibility = View.GONE
    }
}
