package com.techswivel.qthemusic.utils

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaItem.ClippingConfiguration
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

object PlayerUtils {

    private const val DOWNLOAD_CONTENT_DIRECTORY = "downloads"

    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var httpDataSourceFactory: HttpDataSource.Factory
    private lateinit var downloadCache: Cache
    private lateinit var downloadDirectory: File
    private lateinit var databaseProvider: DatabaseProvider

    @Synchronized
    private fun getHttpDataSourceFactory(): HttpDataSource.Factory {
        if (::httpDataSourceFactory.isInitialized.not()) {
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
            CookieHandler.setDefault(cookieManager)
            httpDataSourceFactory =
                DefaultHttpDataSource.Factory()
        }
        return httpDataSourceFactory
    }

    private fun buildReadOnlyCacheDataSource(
        upstreamFactory: DataSource.Factory, cache: Cache
    ): CacheDataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setCacheWriteDataSinkFactory(null)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    @Synchronized
    private fun getDownloadCache(context: Context): Cache {
        if (::downloadCache.isInitialized.not()) {
            val downloadContentDirectory = File(
                getDownloadDirectory(context),
                DOWNLOAD_CONTENT_DIRECTORY
            )
            downloadCache = SimpleCache(
                downloadContentDirectory,
                NoOpCacheEvictor(),
                getDatabaseProvider(context)
            )
        }
        return downloadCache
    }

    @Synchronized
    private fun getDownloadDirectory(context: Context): File? {
        if (::downloadDirectory.isInitialized.not()) {
            downloadDirectory =
                context.getExternalFilesDir( /* type= */null) ?: File("")
            if (::downloadDirectory.isInitialized.not()) {
                downloadDirectory = context.filesDir
            }
        }
        return downloadDirectory
    }

    @Synchronized
    private fun getDatabaseProvider(context: Context): DatabaseProvider {
        if (::databaseProvider.isInitialized.not()) {
            databaseProvider =
                StandaloneDatabaseProvider(context)
        }
        return databaseProvider
    }

    /** Returns a [DataSource.Factory]. */
    @Synchronized
    fun getDataSourceFactory(context: Context): DataSource.Factory {
        val applicationContext: Context
        if (::dataSourceFactory.isInitialized.not()) {
            applicationContext = context.applicationContext
            val upstreamFactory = DefaultDataSource.Factory(
                applicationContext,
                getHttpDataSourceFactory()
            )
            dataSourceFactory =
                buildReadOnlyCacheDataSource(
                    upstreamFactory,
                    getDownloadCache(applicationContext)
                )
        }
        return dataSourceFactory
    }

    fun prepare(uri: Uri, title: String): MediaItem {
        val mediaItem = MediaItem.Builder()
        val clippingConfiguration = ClippingConfiguration.Builder()
        val adaptiveMimeType =
            Util.getAdaptiveMimeTypeForContentType(Util.inferContentType(uri, ""))
        mediaItem
            .setUri(uri)
            .setMediaMetadata(MediaMetadata.Builder().setTitle(title).build())
            .setMimeType(adaptiveMimeType)
            .setClippingConfiguration(clippingConfiguration.build())
        return mediaItem.build()
    }

    fun buildRenderersFactory(
        context: Context, preferExtensionRenderer: Boolean
    ): RenderersFactory {
        val extensionRendererMode =
            if (useExtensionRenderers()) if (preferExtensionRenderer) DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        return DefaultRenderersFactory(context.applicationContext)
            .setExtensionRendererMode(extensionRendererMode)
    }

    /** Returns whether extension renderers should be used. */
    private fun useExtensionRenderers(): Boolean {
        return false
    }
}