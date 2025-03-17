package com.example.practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.practice.ui.theme.PracticeTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class EmotionStamp(
    val id: Int, // 감정 스탬프 ID
    val imageRes: Int, // 이미지 리소스 ID
    val description: String // 감정 설명
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EmotionStampScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun EmotionStampScreen(modifier: Modifier = Modifier) {
    var selectedEmotion by remember { mutableStateOf<EmotionStamp?>(null) }

    val emotions = listOf(
        EmotionStamp(1, R.drawable.stamp1, "더 없이 행복한 하루였어요"),
        EmotionStamp(2, R.drawable.stamp2, "들뜨고 흥분돼요"),
        EmotionStamp(3, R.drawable.stamp3, "평범한 하루였어요"),
        EmotionStamp(4, R.drawable.stamp4, "생각이 많아지고 불안해요"),
        EmotionStamp(5, R.drawable.stamp5, "부글부글 화가 나요")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "오늘 하루는 어땠나요?",
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "감정 우표를 선택해주세요.",
            fontSize = 22.sp,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "선택된 감정우표를 기반으로 맞춤형 질문이 배달됩니다",
            fontSize = 8.sp,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(10.dp))


        LazyColumn {
            items(emotions) { emotion ->
                EmotionStampItem(
                    emotion = emotion,
                    onClick = { selectedEmotion = it }
                )
            }
        }


    }
}

@Composable
fun EmotionStampItem(emotion: EmotionStamp, onClick: (EmotionStamp) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(emotion) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = emotion.imageRes),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(8.dp)) // 이미지와 텍스트 사이 간격

        Text(
            fontSize = 18.sp,
            text = emotion.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmotionStampPreview() {
    EmotionStampScreen()
}
