package com.code.musicplayer.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.code.musicplayer.data.model.Audio
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContentResolverHelper @Inject constructor(@ApplicationContext val context: Context) {
    private var mCursor: Cursor? = null

    private val projections: Array<String> = arrayOf(
        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.TITLE,
    )

    private var selectionClause: String? = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"
    private var selectionArg = arrayOf("1")

    private val sortOrder = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"

    @WorkerThread
    fun getAudioData(): List<Audio> {
        return getCursorData()
    }

    private fun getCursorData(): MutableList<Audio> {
        val audioList = mutableListOf<Audio>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projections,
            selectionClause,
            selectionArg,
            sortOrder
        )

        mCursor?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val displayNameCol =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)

            cursor.apply {
                if (count == 0) {
                    print("Cursor is empty")
                } else {
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idCol)
                        val displayName = cursor.getString(displayNameCol)
                        val artist = cursor.getString(artistCol)
                        val data = cursor.getString(dataCol)
                        val duration = cursor.getInt(durationCol)
                        val title = cursor.getString(titleCol)

                        val uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                        )

                        audioList += Audio(
                            uri, displayName, id, artist, data, duration, title
                        )
                    }
                }
            }
        }
        return audioList
    }

}