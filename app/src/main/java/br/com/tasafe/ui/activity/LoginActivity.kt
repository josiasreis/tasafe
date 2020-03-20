package br.com.tasafe.ui.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.tasafe.model.dto.KeyStoreDTO
import br.com.tasafe.tasafe.R
import br.com.tasafe.tasafe.databinding.ActivityLoginBinding
import br.com.tasafe.utils.NavigationUtil
import br.com.tasafe.utils.SecurityUtils
import br.com.tasafe.utils.hide
import br.com.tasafe.viewmodel.LoginRegisterViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.Executors


class LoginActivity : BaseActivity() {

    lateinit var viewModel: LoginRegisterViewModel
    val executor = Executors.newSingleThreadExecutor()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        verifyLoginOrRegister()
        setupListnerLoggedUser()
    }

    private fun setupListnerLoggedUser() {
        var loggedOberserver = Observer<Boolean> { logged ->
           if(logged){
               NavigationUtil.goTo(this,MySitesActivity::class.java)
           }
        }
        viewModel.logged.observe(this, loggedOberserver)
    }

    private fun verifyLoginOrRegister() {
        SecurityUtils.getEncryptedSharedPreferences(this)?.let {
            sharedPreferences = it
            val iv = sharedPreferences.getString("iv", null)
            val encrypted = sharedPreferences.getString("encrypted", null)

            if (iv != null && encrypted != null) {
                //seta usuario para registrado
                viewModel.setUserSaved(true)

                val biometricManager = BiometricManager.from(this)
                when (biometricManager.canAuthenticate()) {
                    BiometricManager.BIOMETRIC_SUCCESS -> {
                        showBiometricPrompt()
                        Log.d("BIOMETRIA", "App can authenticate using biometrics.")
                    }
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                        Log.e("BIOMETRIA", "No biometric features available on this device.")
                    }
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                        hideViewsFingerPrint()
                        Log.e("BIOMETRIA", "Biometric features are currently unavailable.")
                    }
                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                        hideViewsFingerPrint()
                        //TODO verificar se tem como cadastrar pelo app
                        Log.e(
                            "BIOMETRIA", "The user hasn't associated any biometric credentials " +
                                    "with their account."
                        )
                    }
                    else -> {

                    }
                }
            } else {
                viewModel.setUserSaved(false)
            }
        }
    }

    private fun hideViewsFingerPrint() {
        btnEntrarDigital.hide()
        lblAuth.hide()
    }

    private fun observerResultRegister() {
        var loginEncryptedObserver = Observer<KeyStoreDTO> { keyStoreDTO ->
            val sharedPreferences = SecurityUtils.getEncryptedSharedPreferences(this)
            with(sharedPreferences!!.edit()) {
                putString("encrypted", keyStoreDTO.encrypted)
                putString("iv", keyStoreDTO.iv)
                commit()
            }
        }
        viewModel.loginBase64Encrypted.observe(this, loginEncryptedObserver)
    }

    fun login(view: View) {
        val loginBase64Encrypted = sharedPreferences.getString("encrypted", "")
        val ivBase64Encrypted = sharedPreferences.getString("iv", "")
        viewModel.login(loginBase64Encrypted,ivBase64Encrypted)
    }

    fun register(view: View) {
        observerResultRegister()
        if(isFormaValid()){
            viewModel.register()
        }
    }

    fun showBiometricPrompt(view: View) {
        showBiometricPrompt()
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometria Ta Safe")
            .setSubtitle("Login usando biometria digital")
            .setNegativeButtonText("Cancelar")
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    viewModel.setUserLogged(false)

                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    val authenticatedCryptoObject: BiometricPrompt.CryptoObject? =
                        result.getCryptoObject()
                    viewModel.setUserLogged(true)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    viewModel.setUserLogged(false)
                }
            })

        // Displays the "log in" prompt.
        biometricPrompt.authenticate(promptInfo)
    }

    fun isFormaValid(): Boolean {
        var valid = viewModel.validatePass()
        var nameEmpty = viewModel.user.value!!.nome.isEmpty()
        var emailEmpty = viewModel.user.value!!.email.isEmpty()
        var isValid = true
        if(!valid){
            binding.txtPass.setError("Senha está inválida")
            isValid= false
        }
        if(nameEmpty){
            binding.txtNome.setError("Nome é obrigatório")
            isValid= false
        }
        if(emailEmpty){
            binding.txtEmail.setError("Email é obrigatório")
            isValid= false
        }
        return isValid
    }

}
