package br.com.tasafe.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.tasafe.tasafe.R
import br.com.tasafe.tasafe.databinding.ActivityNewSiteBinding
import br.com.tasafe.viewmodel.NewSiteViewModel
import kotlinx.android.synthetic.main.activity_new_site.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NewSiteActivity : BaseActivity() {
    private lateinit var viewModel: NewSiteViewModel
    private lateinit var binding: ActivityNewSiteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionbar = supportActionBar

        viewModel = ViewModelProvider(this).get(NewSiteViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_site)
        binding.viewmodelN = viewModel
        binding.lifecycleOwner = this

        actionbar?.setDisplayHomeAsUpEnabled(true)
         actionbar?.setDisplayHomeAsUpEnabled(true)

       val idSite = intent.getIntExtra("idSite",0)
        if(idSite != 0){
            actionbar?.title = resources.getString(R.string.editarSite)
            loadViewMode(idSite)
        }else{
            actionbar?.title = resources.getString(R.string.novoSite)
        }
        observerChangeMode()
    }

    fun observerChangeMode(){
        /* TODO CRIAR UM ADAPTER BINDING PARA REMOVER ESSE CODIGO DA ACTIVITY */
        var changeModOberserver = Observer<Boolean> { viewMod ->
            if(viewMod){
                btnRegister.visibility = View.GONE
            }else{
                btnRegister.visibility = View.VISIBLE
            }
        }
        viewModel.viewMod.observe(this, changeModOberserver)
    }

    private fun loadViewMode(idSite:Int){
        viewModel.loadViewMode(idSite)
    }

    fun register(view: View){
        if(isFormaValid()){
            viewModel.save()
            this.onBackPressed()
        }
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

    fun isFormaValid(): Boolean {
        val nameEmpty = viewModel.site.value!!.nomeSite.isEmpty()
        val siteEmpty = viewModel.site.value!!.urlSite.isEmpty()
        val usuarioEmpty = viewModel.site.value!!.usuario.isEmpty()
        val passEmpty = viewModel.password.get()!!.isEmpty()
        var isValid = true
        if(nameEmpty){
            binding.txtNomeSite.error = resources.getString(R.string.nomeRequired)
            isValid= false
        }
        if(siteEmpty){
            binding.txtSite.error = resources.getString(R.string.siteRequired)
            isValid= false
        }
        if(usuarioEmpty){
            binding.txtUsuario.error = resources.getString(R.string.usuarioRequired)
            isValid= false
        }
        if(passEmpty){
            binding.txtPassSite.error = resources.getString(R.string.senhaRequired)
            isValid= false
        }
        return isValid
    }
}
