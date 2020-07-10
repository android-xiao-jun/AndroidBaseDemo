package com.example.room_lib

import androidx.room.*

/**
 *
 * @author Wen xiao
 * @time 2020/7/9
 */

@Dao
interface UserDao {

    @Query("SELECT * FROM Users WHERE userid = :id")
    fun getUserById(id: String): User?

    @Query("SELECT * FROM Users")
    fun getUserById(): Array<User>?

    /*当数据库中已经有此用户的时候，直接替换*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun upDateUser(user: User)

    @Query("DELETE FROM Users")
    fun deleteAllUsers()
}

