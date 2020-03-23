package com.example.flashcards.db.notification

class NotificationRepository(private val notificationDao: NotificationDao) {

    suspend fun deleteAll() = notificationDao.deleteAll()

    suspend fun deleteNotification(notificationId: Int) = notificationDao.deleteNotification(notificationId)

    suspend fun getNotification(notificationId: Int) = notificationDao.getNotification(notificationId)

    suspend fun getNotifications() = notificationDao.getNotifications()

    suspend fun insertNotification(notification: Notification) = notificationDao.insert(notification)
}
