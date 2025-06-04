package com.example.flo_mainpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.flo_mainpage.databinding.ActivityLoginBinding
import com.example.flo_mainpage.repository.AuthRepository
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
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

        // 카카오 로그인 버튼 클릭
        binding.loginKakakoLoginIv.setOnClickListener {
            performKakaoLogin()
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
                        saveTokenToPreferences(accessToken, memberId, "normal")

                        Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
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

    private fun performKakaoLogin() {
        // 카카오계정으로 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "카카오계정으로 로그인 실패", error)
                when {
                    error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                        Toast.makeText(this, "로그인이 취소되었습니다", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (token != null) {
                Log.i("KakaoLogin", "카카오계정으로 로그인 성공 ${token.accessToken}")

                // 사용자 정보 요청
                getUserInfoFromKakao()
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("KakaoLogin", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i("KakaoLogin", "카카오톡으로 로그인 성공 ${token.accessToken}")

                    // 사용자 정보 요청
                    getUserInfoFromKakao()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun getUserInfoFromKakao() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("KakaoLogin", "사용자 정보 요청 실패", error)
                Toast.makeText(this, "사용자 정보를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
            } else if (user != null) {
                Log.i("KakaoLogin", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                // 카카오 로그인 정보를 SharedPreferences에 저장
                val kakaoId = user.id.toString()
                val email = user.kakaoAccount?.email ?: ""
                val nickname = user.kakaoAccount?.profile?.nickname ?: ""
                val profileImage = user.kakaoAccount?.profile?.thumbnailImageUrl ?: ""

                saveKakaoLoginInfo(kakaoId, email, nickname, profileImage)

                Toast.makeText(this, "${nickname}님, 환영합니다!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun saveTokenToPreferences(token: String?, memberId: Int?, loginType: String) {
        val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        prefs.edit().apply {
            putString("access_token", token)
            putInt("member_id", memberId ?: -1)
            putString("login_type", loginType)
            apply()
        }
    }

    private fun saveKakaoLoginInfo(kakaoId: String, email: String, nickname: String, profileImage: String) {
        val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        prefs.edit().apply {
            putString("kakao_id", kakaoId)
            putString("kakao_email", email)
            putString("kakao_nickname", nickname)
            putString("kakao_profile_image", profileImage)
            putString("login_type", "kakao")
            putBoolean("is_logged_in", true)
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