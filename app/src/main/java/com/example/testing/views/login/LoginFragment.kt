package com.example.testing.views.login

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.util.PatternsCompat
import androidx.navigation.fragment.findNavController
import com.example.testing.BuildConfig
import com.example.testing.R
import com.example.testing.data.api.model.request.UserRegisterRequest
import com.example.testing.data.api.repository.AppRepository
import com.example.testing.databinding.FragmentLoginBinding
import com.example.testing.databinding.ItemDialogLoadingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private val viewModel: LoginViewModel by inject()

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
        db = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL)
        ref = db.getReference("users")
        binding.apply {
            inputValidation(etLoginEmail)
            inputValidation(etLoginPass)

            tvLoginRegister.setOnClickListener {
                findNavController().navigate(R.id.login_to_regis)
            }
            btnLogin.setOnClickListener {
                val email = etLoginEmail.text.toString().trim()
                val pass = etLoginPass.text.toString().trim()

                if (!validateEmail() && !validatePass()) {
                    Toast.makeText(
                        requireContext(),
                        "Silahkan cek kembali inputan anda!",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                else {
                    if (!isOnline(requireContext())) {
                        Toast.makeText(
                            requireContext(),
                            "Tidak dapat terhubung ke server, silahkan cek koneksi anda!",
                            Toast.LENGTH_LONG
                        ).show()
                        return@setOnClickListener
                    }
                    else {
                        val loadingDialog =
                            ItemDialogLoadingBinding.inflate(LayoutInflater.from(requireContext()))
                        val loadingDialogBuilder = AlertDialog.Builder(requireContext())
                            .setView(loadingDialog.root)
                        val showDialog = loadingDialogBuilder.create()
                        showDialog.show()
                        ref.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(parentSnap: DataSnapshot) {
                                for (user in parentSnap.children) {
                                    val uid = user.key
                                    if (uid != null) {
                                        ref.child(uid).orderByChild("email").equalTo(email).addListenerForSingleValueEvent(
                                            object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    showDialog.dismiss()
                                                    if (snapshot.exists()) {
                                                        for (uData in snapshot.children) {
                                                            val userData = uData.getValue<UserRegisterRequest>()
                                                            if (userData != null) {
                                                                if (userData.pass == pass) {
                                                                    viewModel.apply {
                                                                        storeLoginState(true)
                                                                        storeUserData(uid)
                                                                    }
                                                                    findNavController().navigate(R.id.login_to_home)
                                                                } else {
                                                                    Toast.makeText(
                                                                        requireContext(),
                                                                        "Kata sandi salah, silahkan coba lagi!",
                                                                        Toast.LENGTH_LONG
                                                                    ).show()
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "Akun tidak ditemukan, silahkan daftar terlebih dahulu!",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {
                                                    Log.d("onCancelledChild", error.toException().toString())
                                                }

                                            })
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("onCancelledParent", error.toException().toString())
                            }

                        })
                    }
                }
            }
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            if (connectivityManager != null) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
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