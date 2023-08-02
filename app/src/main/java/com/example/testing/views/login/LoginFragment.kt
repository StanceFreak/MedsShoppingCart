package com.example.testing.views.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.util.PatternsCompat
import androidx.navigation.fragment.findNavController
import com.example.testing.R
import com.example.testing.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val inputValidation = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.apply {
                when(view?.id) {
                    R.id.et_login_email -> {
                        validateEmail()
                    }
                    R.id.et_login_pass -> {
                        validatePass()
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun setupViews() {
        binding.apply {
            inputValidation(etLoginEmail)
            inputValidation(etLoginPass)

            tvLoginRegister.setOnClickListener {
                findNavController().navigate(R.id.login_to_regis)
            }
        }
    }

    private fun validateEmail(): Boolean {
        binding.apply {
            if (etLoginEmail.text.toString().trim().isEmpty()) {
                tilLoginEmail.isErrorEnabled = true
                tilLoginEmail.error = "Email tidak boleh kosong!"
                etLoginEmail.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            if (PatternsCompat.EMAIL_ADDRESS.matcher(etLoginEmail.text.toString().trim()).matches().not()) {
                tilLoginEmail.isErrorEnabled = true
                tilLoginEmail.error = "Format Email tidak valid, silahkan masukkan Email anda!"
                etLoginEmail.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            else {
                tilLoginEmail.isErrorEnabled = false
                etLoginEmail.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_rounded)
            }
            return true
        }
    }

    private fun validatePass(): Boolean {
        binding.apply {
            if (etLoginPass.text.toString().trim().isEmpty()) {
                tilLoginPass.isErrorEnabled = true
                tilLoginPass.error = "Kata sandi tidak boleh kosong!"
                etLoginPass.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
                //register validation
//            if (etLoginPass.text.toString().trim().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[`!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~])(?=\\S+$).{4,}$".toRegex())) {
//                tilLoginPass.isErrorEnabled = true
//                tilLoginPass.error = "Kata sandi tidak boleh kosong!"
//                etLoginPass.background =
//                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
//                return false
//            }
            else {
                tilLoginPass.isErrorEnabled = false
                etLoginPass.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_rounded)
            }
            return true
        }
    }

    private fun inputValidation(et: EditText) {
        binding.apply {
            et.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val email = etLoginEmail.text.toString().trim()
                    val pass = etLoginPass.text.toString().trim()

                    if (email.isNotEmpty() && pass.isNotEmpty()) {
                        with(btnLogin) {
                            isEnabled = true
                            setTextColor(resources.getColor(R.color.french_gray))
                            setBackgroundColor(resources.getColor(R.color.midnight_green))
                        }
                    }
                    else {
                        with(btnLogin) {
                            isEnabled = false
                            setTextColor(resources.getColor(R.color.midnight_green))
                            setBackgroundColor(resources.getColor(R.color.french_gray))
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (et == etLoginEmail) {
                        validateEmail()
                    }
                    else if (et == etLoginPass) {
                        validatePass()
                    }
                }
            })
        }
    }

}