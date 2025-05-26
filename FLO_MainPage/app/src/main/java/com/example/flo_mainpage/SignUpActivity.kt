package com.example.flo_mainpage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_mainpage.auth.member.Join
import com.example.flo_mainpage.auth.member.JoinResult
import com.example.flo_mainpage.databinding.ActivitySignupBinding
import com.example.flo_mainpage.network.NetworkModule
import com.example.flo_mainpage.auth.OnboardingApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    lateinit var onboardingApi: OnboardingApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit 객체 초기화
        onboardingApi = NetworkModule.getClient().create(OnboardingApi::class.java)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val emailId = binding.signUpIdEt.text.toString()
        val emailDomain = binding.signUpDirectInputEt.text.toString()
        val password = binding.signUpPasswordEt.text.toString()
        val passwordCheck = binding.signUpPasswordCheckEt.text.toString()
        val name = binding.signUpName.text.toString()

        if (emailId.isEmpty() || emailDomain.isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordCheck) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (name.isEmpty()) {
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = "$emailId@$emailDomain"
        val joinRequest = Join(name = name, email = email, password = password)

        onboardingApi.join(joinRequest).enqueue(object : Callback<JoinResult> {
            override fun onResponse(call: Call<JoinResult>, response: Response<JoinResult>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Toast.makeText(this@SignUpActivity, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val msg = response.body()?.message ?: "회원가입 실패"
                    Toast.makeText(this@SignUpActivity, msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JoinResult>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "서버 통신 실패: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.e("SignUpActivity", "onFailure: ", t)
            }
        })
    }
}
