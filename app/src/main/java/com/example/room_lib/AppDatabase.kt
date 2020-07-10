package com.example.room_lib

import android.content.Context
import androidx.room.*

/**
 *
 * @author Wen xiao
 * @time 2020/7/9
 */

@Database(entities = arrayOf(User::class), version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UsersDatabase? = null

        fun getInstance(context: Context): UsersDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        UsersDatabase::class.java, "Sample.db")
                        .allowMainThreadQueries()
                        .build()
    }


}

