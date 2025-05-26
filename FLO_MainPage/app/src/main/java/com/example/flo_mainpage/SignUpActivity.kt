package com.example.flo_mainpage

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.flo_mainpage.databinding.ActivitySignupBinding
import com.example.flo_mainpage.repository.AuthRepository
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.signUpSignUpBtn.setOnClickListener {
            performSignup()
        }

        binding.signUpHidePasswordIv.setOnClickListener {
            togglePasswordVisibility(binding.signUpPasswordEt)
        }

        binding.signUpHidePasswordCheckIv.setOnClickListener {
            togglePasswordVisibility(binding.signUpPasswordCheckEt)
        }
    }

    private fun performSignup() {
        val name = binding.signUpName.text.toString()
        val email = "${binding.signUpIdEt.text}@${binding.signUpDirectInputEt.text}"
        val password = binding.signUpPasswordEt.text.toString()
        val passwordCheck = binding.signUpPasswordCheckEt.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordCheck) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = authRepository.join(name, email, password)

                if (response.isSuccessful) {
                    val joinResponse = response.body()
                    if (joinResponse?.isSuccess == true) {
                        Toast.makeText(this@SignupActivity, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@SignupActivity,
                            joinResponse?.message ?: "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignupActivity,
                        "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SignupActivity,
                    "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun togglePasswordVisibility(editText: android.widget.EditText) {
        val inputType = editText.inputType
        if (inputType == 129) { // textPassword
            editText.inputType = 1 // text
        } else {
            editText.inputType = 129 // textPassword
        }
        editText.setSelection(editText.text.length)
    }
}