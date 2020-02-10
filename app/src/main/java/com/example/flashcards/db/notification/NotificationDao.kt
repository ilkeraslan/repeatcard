package com.example.flashcards.db.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDao {

    @Query("DELETE FROM notifications_table")
    suspend fun deleteAll()

    @Query("DELETE FROM notifications_table WHERE notificationId=:notificationId")
    suspend fun deleteNotification(notificationId: Int)

    @Query("SELECT * FROM notifications_table WHERE notificationId=:notificationId")
    suspend fun getNotification(notificationId: Int): Notification

    @Query("SELECT * FROM notifications_table ORDER BY notification_title ASC")
    suspend fun getNotifications(): List<Notification>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(notification: Notification)
}
