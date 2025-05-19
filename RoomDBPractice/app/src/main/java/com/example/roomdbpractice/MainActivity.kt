package com.example.roomdbpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.roomdbpractice.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val list = ArrayList<Profile>()
    private lateinit var customAdapter: CustomAdapter
    private lateinit var db: ProfileDatabase

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ProfileDatabase.getInstance(this)!!
        customAdapter = CustomAdapter(list, this)
        binding.mainProfileLv.adapter = customAdapter

        // Load data using coroutines
        loadProfiles()

        binding.button.setOnClickListener {
            addProfile()
        }
    }

    private fun loadProfiles() {
        lifecycleScope.launch(Dispatchers.IO) {
            val savedContacts = db.profileDao().getAll()
            withContext(Dispatchers.Main) {
                if (savedContacts.isNotEmpty()) {
                    list.clear()
                    list.addAll(savedContacts)
                    customAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun addProfile() {
        lifecycleScope.launch(Dispatchers.IO) {
            val profile = Profile("베어", "24", "0000")
            db.profileDao().insert(profile)
            val updatedProfiles = db.profileDao().getAll()

            withContext(Dispatchers.Main) {
                list.clear()
                list.addAll(updatedProfiles)
                customAdapter.notifyDataSetChanged()
            }
        }
    }
}