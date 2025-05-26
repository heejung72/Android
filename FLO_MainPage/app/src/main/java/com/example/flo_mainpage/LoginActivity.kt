package com.example.flo_mainpage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.flo_mainpage.databinding.ActivityLoginBinding
import com.example.flo_mainpage.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // 로그인 버튼 클릭
        binding.loginSignInBtn.setOnClickListener {
            performLogin()
        }

        // 회원가입 텍스트 클릭
        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // 닫기 버튼 클릭
        binding.loginCloseIv.setOnClickListener {
            finish()
        }

        // 패스워드 보기/숨기기
        binding.loginHidePasswordIv.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun performLogin() {
        val email = "${binding.loginIdEt.text}@${binding.loginDirectInputEt.text}"
        val password = binding.loginPasswordEt.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = authRepository.login(email, password)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.isSuccess == true) {
                        // 로그인 성공
                        val accessToken = loginResponse.result?.accessToken
                        val memberId = loginResponse.result?.memberId

                        // SharedPreferences에 토큰 저장
                        saveTokenToPreferences(accessToken, memberId)

                        Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()

                        // 메인 액티비티로 이동
                        // startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity,
                            loginResponse?.message ?: "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity,
                        "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity,
                    "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveTokenToPreferences(token: String?, memberId: Int?) {
        val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        prefs.edit().apply {
            putString("access_token", token)
            putInt("member_id", memberId ?: -1)
            apply()
        }
    }

    private fun togglePasswordVisibility() {
        val inputType = binding.loginPasswordEt.inputType
        if (inputType == 129) { // textPassword
            binding.loginPasswordEt.inputType = 1 // text
        } else {
            binding.loginPasswordEt.inputType = 129 // textPassword
        }
        binding.loginPasswordEt.setSelection(binding.loginPasswordEt.text.length)
    }
}