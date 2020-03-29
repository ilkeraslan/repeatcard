package com.repeatcard.app.db.notification

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications_table")
class Notification(
    @PrimaryKey(autoGenerate = true) val notificationId: Int,
    @ColumnInfo(name = "notification_title") val notificationTitle: String,
    @ColumnInfo(name = "notification_type") val notificationType: String,
    @ColumnInfo(name = "creation_date") val creationDate: String
)
