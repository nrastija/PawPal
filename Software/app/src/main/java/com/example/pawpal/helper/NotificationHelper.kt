import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    enum class Priority(val level: Int, val color: Int) {
        HIGH(NotificationCompat.PRIORITY_HIGH, Color.RED),
        MEDIUM(NotificationCompat.PRIORITY_DEFAULT, Color.YELLOW),
        LOW(NotificationCompat.PRIORITY_LOW, Color.GREEN)
    }

    fun getPriorityForReminderType(vrstaPodsjetnika: String): Priority {
        return when (vrstaPodsjetnika) {
            "Hitno" -> Priority.HIGH
            "Važno" -> Priority.MEDIUM
            else -> Priority.LOW
        }
    }

    fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(
        channelId: String,
        notificationId: Int,
        naslov: String,
        opis: String,
        priority: Priority = Priority.MEDIUM,
        slika: Bitmap? = null
    ) {
        Log.d("NotificationHelper", "Slanje notifikacije: $naslov")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(naslov)
            .setContentText(opis)
            .setPriority(priority.level)
            .setColor(priority.color)
            .setAutoCancel(true)


        slika?.let {
            builder.setLargeIcon(it)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(it))
        }

        Log.d("NotificationHelper", "Sending notification...")
        notificationManager.notify(notificationId, builder.build())
    }
}
