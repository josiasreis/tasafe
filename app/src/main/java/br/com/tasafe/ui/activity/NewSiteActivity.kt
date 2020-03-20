package br.com.tasafe.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.tasafe.tasafe.R
import br.com.tasafe.tasafe.databinding.ActivityNewSiteBinding
import br.com.tasafe.utils.NavigationUtil
import br.com.tasafe.utils.hide
import br.com.tasafe.utils.show
import br.com.tasafe.viewmodel.NewSiteViewModel
import kotlinx.android.synthetic.main.activity_new_site.*


class NewSiteActivity : BaseActivity() {
    lateinit var viewModel: NewSiteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionbar = supportActionBar

        viewModel = ViewModelProvider(this).get(NewSiteViewModel::class.java)
        var binding: ActivityNewSiteBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_site)
        binding.viewmodelN = viewModel
        binding.lifecycleOwner = this

        actionbar?.setDisplayHomeAsUpEnabled(true)
         actionbar?.setDisplayHomeAsUpEnabled(true)

       var idSite = intent.getIntExtra("idSite",0)
        if(idSite != 0){
            actionbar?.title = getResources().getString(R.string.editarSite)
            loadViewMode(idSite)
        }else{
            actionbar?.title = getResources().getString(R.string.novoSite)
        }
        observerChangeMode()
    }

    fun observerChangeMode(){
        //TODO CRIAR UM ADAPTER BINDING PARA REMOVER ESSE CODIGO DA ACTIVITY
        var changeModOberserver = Observer<Boolean> { viewMod ->
            if(viewMod){
                btnRegister.visibility = View.GONE
            }else{
                btnRegister.visibility = View.VISIBLE
            }
        }
        viewModel.viewMod.observe(this, changeModOberserver)
    }

    fun loadViewMode(idSite:Int){
        viewModel.loadViewMode(idSite)
    }

    fun register(view: View){
        viewModel.save()
        this.onBackPressed();
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
