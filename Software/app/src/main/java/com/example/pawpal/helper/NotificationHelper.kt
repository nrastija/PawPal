import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pawpal.main.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

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

    fun sendNotificationWithCalendarOption(
        channelId: String,
        notificationId: Int,
        naslov: String,
        opis: String,
        priority: Priority,
        datum: String,
        vrijeme: String,
        usluga: String,
        veterinar: String,
        cijena: String,
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val calendarText = "Rezervirana usluga ${usluga} kod veterinara ${veterinar}. Termin je rezerviran ${datum} u ${vrijeme}. Ukupni troškovi usluge iznose ${cijena}. Vidimo se!"

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, naslov)
            putExtra(CalendarContract.Events.DESCRIPTION, calendarText)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, convertToMillis(datum, vrijeme))
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(naslov)
            .setContentText(opis)
            .setPriority(priority.level)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(opis)
        builder.setStyle(bigTextStyle)

        notificationManager.notify(notificationId, builder.build())
    }


    private fun convertToMillis(datum: String, vrijeme: String): Long {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
        val dateTime = "$datum $vrijeme"
        return try {
            formatter.parse(dateTime)?.time ?: System.currentTimeMillis()
        } catch (e: ParseException) {
            Log.e("DatumVrijeme", "Greška pri parsiranju datuma i vremena: $dateTime", e)
            System.currentTimeMillis()
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

        notificationManager.notify(notificationId, builder.build())
    }

    fun sendBigStyleNotification(
        channelId: String,
        notificationId: Int,
        naslov: String,
        opis: String,
        priority: Priority = Priority.MEDIUM,
        slika: Bitmap? = null
    ) {
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

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(opis)
        builder.setStyle(bigTextStyle)

        notificationManager.notify(notificationId, builder.build())
    }

    fun sendHeadsUpNotification(
        channelId: String,
        notificationId: Int,
        naslov: String,
        opis: String,
        priority: Priority = Priority.MEDIUM,
        slika: Bitmap? = null
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(naslov)
            .setContentText(opis)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(priority.color)
            .setAutoCancel(true)
            .setFullScreenIntent(pendingIntent, true)

        slika?.let {
            builder.setLargeIcon(it)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(it))
        }

        notificationManager.notify(notificationId, builder.build())

        Handler(Looper.getMainLooper()).postDelayed({
            notificationManager.cancel(1)
        }, 5000)
    }


}
