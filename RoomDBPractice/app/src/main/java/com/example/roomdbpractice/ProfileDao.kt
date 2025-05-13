package com.example.roomdbpractice

import androidx.room.*

@Dao
interface ProfileDao {
    @Insert
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)

    @Delete
    fun delete(profile: Profile)
}
