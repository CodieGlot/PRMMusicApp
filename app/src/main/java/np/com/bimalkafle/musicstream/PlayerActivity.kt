package np.com.bimalkafle.musicstream

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import np.com.bimalkafle.musicstream.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var exoPlayer: ExoPlayer

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            showGif(isPlaying)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyExoplayer.getCurrentSong()?.apply {
            binding.songTitleTextView.text = title
            binding.songSubtitleTextView.text = subtitle

            // Load local asset image
            val imageUri = coverUrl
            Glide.with(binding.songCoverImageView.context)
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.songCoverImageView)

            // Load playing GIF from drawable
            Glide.with(binding.songGifImageView)
                .load(R.drawable.media_playing)
                .circleCrop()
                .into(binding.songGifImageView)

            // ExoPlayer hookup
            exoPlayer = MyExoplayer.getInstance()!!
            binding.playerView.player = exoPlayer
            binding.playerView.showController()
            exoPlayer.addListener(playerListener)

            binding.backButton.setOnClickListener {
                finish() // Closes the activity and returns to the previous one
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(playerListener)
    }

    private fun showGif(show: Boolean) {
        binding.songGifImageView.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }
}
