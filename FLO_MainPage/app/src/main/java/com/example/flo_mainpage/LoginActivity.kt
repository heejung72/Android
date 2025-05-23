package com.example.flo_mainpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_mainpage.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val emailId = binding.loginIdEt.text.toString()
        val emailDomain = binding.loginDirectInputEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        if (emailId.isEmpty() || emailDomain.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = "$emailId@$emailDomain"
        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, password)

        if (user != null) {
            Log.d("LOGIN_ACT/GET_USER", "userId: ${user.id}, $user")
            saveJwt(user.id)
            startMainActivity()
        } else {
            Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 로그인 후 LoginActivity 종료
    }

    private fun saveJwt(jwt: Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putInt("jwt", jwt)
        editor.apply()
    }
}
