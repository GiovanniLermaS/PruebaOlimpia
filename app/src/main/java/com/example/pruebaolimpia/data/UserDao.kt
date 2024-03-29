package com.example.pruebaolimpia.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pruebaolimpia.data.entities.CURRENT_USER_ID
import com.example.pruebaolimpia.data.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: User): Long

    @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
    fun getUser(): User

}