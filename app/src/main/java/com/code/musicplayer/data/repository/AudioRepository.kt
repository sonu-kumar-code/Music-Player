package com.code.musicplayer.data.repository

import com.code.musicplayer.data.ContentResolverHelper
import com.code.musicplayer.data.model.Audio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioRepository @Inject constructor(private val contentResolverHelper: ContentResolverHelper) {

    suspend fun getAudioData(): List<Audio> = withContext(Dispatchers.IO) {
        contentResolverHelper.getAudioData()
    }
}