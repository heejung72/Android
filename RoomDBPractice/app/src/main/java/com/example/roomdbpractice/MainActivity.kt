package com.example.roomdbpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roomdbpractice.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var customAdapter: CustomAdapter
    private lateinit var db: ProfileDatabase
    private var list = ArrayList<Profile>()

    lateinit var binding: ActivityMainBinding
    override fun onCreate(SavedInstanceStatae:Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ProfileDatabase.getInstance(this)!!

        Thread{
            val savedContacts = db.profileDao().getAll()
            if (savedContacts.isNotEmpty()){
                list.addAll(savedContacts)
            }
        }.start()

        binding.button.setOnClickListener{
            Thread{
                list.add(Profile("bear","24","0000"))
                db.profileDao().insert(Profile("bear","24","0000"))
            val list = db.profileDao().getAll()
                Log.d("Inserted Primary Key", list[list.size-1].id.toString())
            }.start()
            customAdapter.notifyDataSetChanged()
        }

        customAdapter = CustomAdapter(list, this)
        binding.mainProfileLv.adapter = customAdapter

    }
}
