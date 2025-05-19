package com.example.flo_mainpage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_mainpage.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun getUser(): User {
        val email = "${binding.signUpIdEt.text}@${binding.signUpDirectInputEt.text}"
        val pwd = binding.signUpPasswordEt.text.toString()
        return User(email, pwd)
    }

    private fun signUp() {
        val emailId = binding.signUpIdEt.text.toString()
        val emailDomain = binding.signUpDirectInputEt.text.toString()
        val password = binding.signUpPasswordEt.text.toString()
        val passwordCheck = binding.signUpPasswordCheckEt.text.toString()

        if (emailId.isEmpty() || emailDomain.isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordCheck) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = "$emailId@$emailDomain"
        val userDB = SongDatabase.getInstance(this)!!

        // 이미 존재하는 이메일인지 확인 (선택적 기능)
        val existingUsers = userDB.userDao().getUsers()
        if (existingUsers.any { it.email == email }) {
            Toast.makeText(this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        userDB.userDao().insert(User(email, password))

        Log.d("SIGNUPACT", userDB.userDao().getUsers().toString())
        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }
}
