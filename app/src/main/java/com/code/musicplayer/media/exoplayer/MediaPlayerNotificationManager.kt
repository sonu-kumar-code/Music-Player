package com.code.musicplayer.media.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.media.session.MediaSession
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat.Token
import com.code.musicplayer.R
import com.code.musicplayer.media.constants.K
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

internal class MediaPlayerNotificationManager(
    context: Context,
    sessionToken: Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {
    private var notificationManager:PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context,sessionToken)
        val builder = PlayerNotificationManager.Builder(
            context,
            K.PLAYBACK_NOTIFICATION_ID,
            K.PLAYBACK_NOTIFICATION_CHANNEL_ID
        )

        with(builder){
            setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            setNotificationListener(notificationListener)
            setChannelNameResourceId(R.string.notification_channel)
            setChannelDescriptionResourceId(R.string.notification_channel_description)
        }

        notificationManager = builder.build()

        with(notificationManager){
            setMediaSessionToken(sessionToken)
            setSmallIcon(R.drawable.baseline_music_note_24)
            setUseRewindAction(false)
            setUseFastForwardAction(false)
        }
    }

    fun hideNotification(){
        notificationManager.setPlayer(null)
    }

    fun showNotification(player: Player){
        notificationManager.setPlayer(player)
    }

    inner class DescriptionAdapter(private val controller: MediaControllerCompat):
            PlayerNotificationManager.MediaDescriptionAdapter{
        override fun getCurrentContentTitle(p0: Player): CharSequence = controller.metadata.description.title.toString()

        override fun createCurrentContentIntent(p0: Player): PendingIntent? = controller.sessionActivity

        override fun getCurrentContentText(p0: Player): CharSequence? = controller.metadata.description.subtitle.toString()

        override fun getCurrentLargeIcon(
            p0: Player,
            p1: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return null
        }

    }
}