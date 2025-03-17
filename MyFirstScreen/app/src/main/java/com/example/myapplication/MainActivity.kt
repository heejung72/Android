package com.example.myfirstapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var textView: TextView? = null
    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI 요소 초기화
        textView = findViewById(R.id.textView)  // R.id.textView를 올바르게 참조
        button = findViewById(R.id.button)      // R.id.button을 올바르게 참조

        // 버튼 클릭 이벤트 처리
        button?.setOnClickListener {
            textView?.text = "Button Clicked!"  // 텍스트 변경
        }
    }
}
