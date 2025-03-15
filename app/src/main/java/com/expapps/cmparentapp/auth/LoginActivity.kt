package com.expapps.cmparentapp.auth

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.expapps.cmparentapp.Constants
import com.expapps.cmparentapp.FirebaseSource
import com.expapps.cmparentapp.MainActivity
import com.expapps.cmparentapp.databinding.ActivityLoginBinding
import com.expapps.cmparentapp.models.User
import com.expapps.cmparentapp.Utils
import com.expapps.cmparentapp.Utils.openActivity
import com.expapps.cmparentapp.Utils.showToast
import com.expapps.cmparentapp.sharedprefs.KSharedPreference
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseSource: FirebaseSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setLoginClick()
    }


    private fun init() {
        firebaseSource = FirebaseSource()
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setTitle("CMS")
        progressDialog.setMessage("Logging in for monitoring...")
    }

    private fun setLoginClick() {
        binding.registerBtn.setOnClickListener {
            val email = binding.emailEt.editText?.text.toString().trim()
            val mcode = binding.mCodeEt.editText?.text.toString().trim()

            if (!Utils.checkEmptyOrNullString(email, mcode)) {
                showToast("Error: Please fill proper data")
                return@setOnClickListener
            }

            if (mcode.length != 6) {
                showToast("Error: MCode should be 6 letters")
                return@setOnClickListener
            }

            val user = User(
                email = email,
                mcode = mcode,
            )

            login(user)
        }
    }

    private fun login(user: User) {
        showProgress()
        firebaseSource.getMCodeByEmail(user.email ?: "").observe(this) {
            it?.let { (str, str2) ->
                if (str?.length == 6 && str.lowercase() == user.mcode?.lowercase()) {
                    dismissProgress()
                    KSharedPreference.setString(this, Constants.USER_ID, str2 ?: "")
                    showToast("Login successful")
                    openActivity(MainActivity::class.java, finishPrev = true)
                } else {
                    dismissProgress()
                    showToast("Login failed, please check credentials")
                }
            } ?: run {
                dismissProgress()
                showToast("Login failed, please check credentials")
            }
        }
    }


    private fun showProgress() {
        progressDialog.show()
    }

    private fun dismissProgress() {
        progressDialog.dismiss()
    }

}