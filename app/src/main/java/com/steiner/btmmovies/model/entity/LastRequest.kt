package com.steiner.btmmovies.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.steiner.btmmovies.model.enumeration.Request

/**
 *
 */
@Entity(
    tableName = "last_requests",
    indices = [Index(value = ["request"], unique = true)]
)
data class LastRequest(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "request") val request: Request,
    @ColumnInfo(name = "timestamp") val timestamp: Long = 0
)