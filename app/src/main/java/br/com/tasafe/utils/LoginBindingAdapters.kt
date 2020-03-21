package br.com.tasafe.utils

import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import br.com.tasafe.viewmodel.LoginRegisterViewModel

/* BIND ADAPTERS VALIDATIONS */
@BindingAdapter("app:requiredEditText")
fun requiredEditText(view: EditText, message: String) {
    view.onFocusChangeListener = object : View.OnFocusChangeListener {
        override fun onFocusChange(view: View, hasFocus: Boolean) {
            if (!hasFocus) {
                if (view is EditText) {
                    val editText = view
                    if (editText.isEmpty()) {
                        editText.error = message
                    } else {
                        editText.error = null
                    }
                }
            }
        }
    }
}

@BindingAdapter(value = ["app:emailValid", "app:required", "app:requiredMessage"], requireAll = true)
fun emailValid(view: EditText, message: String, required: Boolean, requiredMessage:String) {
    view.onFocusChangeListener = object : View.OnFocusChangeListener {
        override fun onFocusChange(view: View, hasFocus: Boolean) {
            if (!hasFocus) {
                if (view is EditText) {
                    val editText = view
                    if (required && editText.isEmpty()) {
                        editText.error = requiredMessage
                    } else if(! editText.text.toString().isEmailValid()){
                        editText.error = message
                    }else {
                        editText.error = null
                    }
                }
            }
        }
    }
}

@BindingAdapter(value = ["app:invalidPass","app:viewmodel"], requireAll = true)
fun invalidPass(view: EditText, message: String, viewModel:LoginRegisterViewModel) {
    view.onFocusChangeListener = object : View.OnFocusChangeListener {
        override fun onFocusChange(view: View, hasFocus: Boolean) {
            if (!hasFocus) {
                val valid:Boolean = viewModel.validatePass()
                if(!valid){
                    if (view is EditText) {
                        val editText = view
                        editText.error = message
                    }
                }
            }
        }
    }
}

@BindingAdapter(value = ["app:showIfSaved"])
fun showIfSaved(view: View, saved:Boolean) {
    if(saved){
        view.visibility = View.VISIBLE
    }else{
        view.visibility = View.GONE
    }
}

@BindingAdapter(value = ["app:hideIfSaved"])
fun hideIfUserSave(view: View, saved:Boolean) {
    if(saved){
        view.visibility = View.GONE
    }else{
        view.visibility = View.VISIBLE
    }
}






