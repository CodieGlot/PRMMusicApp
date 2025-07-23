package np.com.bimalkafle.musicstream

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import np.com.bimalkafle.musicstream.adapter.SongsListAdapter
import np.com.bimalkafle.musicstream.databinding.ActivitySongsListBinding
import np.com.bimalkafle.musicstream.models.CategoryModel

class SongsListActivity : AppCompatActivity() {

    companion object {
        lateinit var category: CategoryModel
    }

    private lateinit var binding: ActivitySongsListBinding
    private lateinit var songsListAdapter: SongsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set category title and image
        binding.nameTextView.text = category.name
        Glide.with(binding.coverImageView).load(category.coverUrl)
            .apply(RequestOptions().transform(RoundedCorners(32)))
            .into(binding.coverImageView)

        // Set up RecyclerView
        setupSongsListRecyclerView()

        // Handle back button click
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupSongsListRecyclerView() {
        songsListAdapter = SongsListAdapter(
            category.songs,
            LocalSongs.songMap
        )
        binding.songsListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.songsListRecyclerView.adapter = songsListAdapter
    }
}
