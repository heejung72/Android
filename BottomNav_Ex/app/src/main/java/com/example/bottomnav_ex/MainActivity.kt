package com.example.bottomnav_ex

import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var toyouContainer: FrameLayout
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toyouContainer = findViewById(R.id.toyou_container)
        bottomNavigation = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> animateImageTransition(R.drawable.ic_toyou)
                R.id.nav_pen -> animateImageTransition(R.drawable.ic_pen)
                R.id.nav_calendar -> animateImageTransition(R.drawable.ic_calendar)
                R.id.nav_settings -> animateImageTransition(R.drawable.ic_settings)
                else -> false
            }
        }
    }

    private fun animateImageTransition(newImageResource: Int): Boolean {
        // Create a new ImageView for the incoming image
        val newImageView = ImageView(this)
        newImageView.setImageResource(newImageResource)
        newImageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        newImageView.scaleType = ImageView.ScaleType.CENTER

        // Find the existing ImageView
        val existingImageView = toyouContainer.findViewById<ImageView>(R.id.toyou_mailbox)

        // Add the new image view to the container
        toyouContainer.addView(newImageView)

        // Slide and Fade Out Animation for existing image
        val slideOutAnimation = AnimationSet(true)
        val slideOut = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, -1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        slideOut.duration = 300
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 300

        slideOutAnimation.addAnimation(slideOut)
        slideOutAnimation.addAnimation(fadeOut)

        // Slide and Fade In Animation for new image
        val slideInAnimation = AnimationSet(true)
        val slideIn = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        slideIn.duration = 300
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 300

        slideInAnimation.addAnimation(slideIn)
        slideInAnimation.addAnimation(fadeIn)

        // Set animation listeners
        slideOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                toyouContainer.removeView(existingImageView)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        // Start animations
        existingImageView.startAnimation(slideOutAnimation)
        newImageView.startAnimation(slideInAnimation)

        return true
    }
}