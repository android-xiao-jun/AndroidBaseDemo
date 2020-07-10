package com.example.room_lib

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 *
 * @author Wen xiao
 * @time 2020/7/9
 */

@Entity(tableName = "users")
data class User(
        @PrimaryKey
        @ColumnInfo(name = "userid") val id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "username") val userName: String)

