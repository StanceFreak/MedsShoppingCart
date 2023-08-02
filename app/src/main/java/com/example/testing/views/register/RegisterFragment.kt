package com.example.testing.views.register

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
import com.example.testing.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            inputValidation(etRegisterName)
            inputValidation(etRegisterEmail)
            inputValidation(etRegisterPass)
            inputValidation(etRegisterConfPass)

            tvRegisterLogin.setOnClickListener {
                findNavController().navigate(R.id.regis_to_login)
            }
        }
    }

    private fun validateName(): Boolean {
        binding.apply {
            if (etRegisterName.text.toString().trim().isEmpty()) {
                tilRegisterName.isErrorEnabled = true
                tilRegisterName.error = "Nama tidak boleh kosong!"
                etRegisterName.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            if (etRegisterName.text.toString().trim().matches("^[A-Za-z\\s]*\$".toRegex()).not()) {
                tilRegisterName.isErrorEnabled = true
                tilRegisterName.error = "Nama tidak boleh ada angka ataupun simbol khusus!"
                etRegisterName.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            if (etRegisterName.text.toString().trim().length > 30) {
                tilRegisterName.isErrorEnabled = true
                tilRegisterName.error = "Nama tidak boleh lebih dari 30 karakter!"
                etRegisterName.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            else {
                tilRegisterName.isErrorEnabled = false
                etRegisterName.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_rounded)
            }
            return true
        }
    }

    private fun validateEmail(): Boolean {
        binding.apply {
            if (etRegisterEmail.text.toString().trim().isEmpty()) {
                tilRegisterEmail.isErrorEnabled = true
                tilRegisterEmail.error = "Email tidak boleh kosong!"
                etRegisterEmail.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            if (PatternsCompat.EMAIL_ADDRESS.matcher(etRegisterEmail.text.toString().trim()).matches().not()) {
                tilRegisterEmail.isErrorEnabled = true
                tilRegisterEmail.error = "Format Email tidak valid, silahkan masukkan Email anda!"
                etRegisterEmail.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            else {
                tilRegisterEmail.isErrorEnabled = false
                etRegisterEmail.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_rounded)
            }
            return true
        }
    }

    private fun validatePass(): Boolean {
        binding.apply {
            if (etRegisterPass.text.toString().trim().isEmpty()) {
                tilRegisterPass.isErrorEnabled = true
                tilRegisterPass.error = "Kata sandi tidak boleh kosong!"
                etRegisterPass.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            if (etRegisterPass.text.toString().trim().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[`!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~])(?=\\S+$).{4,}$".toRegex()).not()) {
                tilRegisterPass.isErrorEnabled = true
                tilRegisterPass.error = "Kata sandi harus berisi kombinasi huruf besar dan kecil, angka, dan karakter khusus (!\$@%)!"
                etRegisterPass.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            if (etRegisterPass.text.toString().trim().length < 6) {
                tilRegisterPass.isErrorEnabled = true
                tilRegisterPass.error = "Kata sandi tidak boleh kurang dari 6 karakter!"
                etRegisterPass.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            else {
                tilRegisterPass.isErrorEnabled = false
                etRegisterPass.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_rounded)
            }
            return true
        }
    }

    private fun validateConfPass(): Boolean {
        binding.apply {
            if (etRegisterConfPass.text.toString().trim().isEmpty()) {
                tilRegisterConfPass.isErrorEnabled = true
                tilRegisterConfPass.error = "Masukkan ulang kata sandi anda!"
                etRegisterConfPass.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            if (etRegisterConfPass.text.toString().trim() != etRegisterPass.text.toString().trim()) {
                tilRegisterConfPass.isErrorEnabled = true
                tilRegisterConfPass.error = "Kata sandi tidak sama, silahkan cek kembali inputan anda!"
                etRegisterConfPass.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_et_error)
                return false
            }
            else {
                tilRegisterConfPass.isErrorEnabled = false
                etRegisterConfPass.background =
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
                    val name = etRegisterName.text.toString().trim()
                    val email = etRegisterEmail.text.toString().trim()
                    val pass = etRegisterPass.text.toString().trim()
                    val confPass = etRegisterConfPass.text.toString().trim()

                    if (name.isNotEmpty() && email.isNotEmpty() &&
                        pass.isNotEmpty() && confPass.isNotEmpty()) {
                        with(btnRegister) {
                            isEnabled = true
                            setTextColor(resources.getColor(R.color.french_gray))
                            setBackgroundColor(resources.getColor(R.color.midnight_green))
                        }
                    }
                    else {
                        with(btnRegister) {
                            isEnabled = false
                            setTextColor(resources.getColor(R.color.midnight_green))
                            setBackgroundColor(resources.getColor(R.color.french_gray))
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    when (et) {
                        etRegisterName -> {
                            validateName()
                        }
                        etRegisterEmail -> {
                            validateEmail()
                        }
                        etRegisterPass -> {
                            validatePass()
                        }
                        etRegisterConfPass -> {
                            validateConfPass()
                        }
                    }
                }
            })
        }
    }

}