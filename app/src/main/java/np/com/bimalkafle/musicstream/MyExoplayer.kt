package np.com.bimalkafle.musicstream

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.datasource.AssetDataSource
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import np.com.bimalkafle.musicstream.models.SongModel

object MyExoplayer {

    private var exoPlayer: ExoPlayer? = null
    private var currentSong: SongModel? = null

    fun getCurrentSong(): SongModel? {
        return currentSong
    }

    fun getInstance(): ExoPlayer? {
        return exoPlayer
    }

    fun startPlaying(context: Context, song: SongModel) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }

        if (currentSong != song) {
            currentSong = song

            val mediaSource = buildMediaSourceFromAssets(context, song.url)
            if (mediaSource != null) {
                exoPlayer?.setMediaSource(mediaSource)
                exoPlayer?.prepare()
                exoPlayer?.play()
            }
        }
    }

    private fun buildMediaSourceFromAssets(context: Context, assetPath: String): ProgressiveMediaSource? {
        return try {
            // ExoPlayer URI for assets must be like asset:///music/Blue.mp3
            val uri = Uri.parse("asset:///$assetPath")

            val dataSourceFactory = DataSource.Factory {
                val dataSource = AssetDataSource(context)
                dataSource.open(DataSpec(uri))
                dataSource
            }

            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
