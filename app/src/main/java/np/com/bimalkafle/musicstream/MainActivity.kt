package np.com.bimalkafle.musicstream

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import np.com.bimalkafle.musicstream.adapter.CategoryAdapter
import np.com.bimalkafle.musicstream.adapter.SectionSongListAdapter
import np.com.bimalkafle.musicstream.databinding.ActivityMainBinding
import np.com.bimalkafle.musicstream.models.CategoryModel
import np.com.bimalkafle.musicstream.models.SongModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var allSongs: List<SongModel>
    private lateinit var categories: List<CategoryModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadDataFromAssets()

        setupCategoryRecyclerView(categories)
        setupSectionUI("section_1", binding.section1MainLayout, binding.section1Title, binding.section1RecyclerView)
        setupSectionUI("section_2", binding.section2MainLayout, binding.section2Title, binding.section2RecyclerView)
        setupSectionUI("section_3", binding.section3MainLayout, binding.section3Title, binding.section3RecyclerView)
        setupSectionUI("mostly_played", binding.mostlyPlayedMainLayout, binding.mostlyPlayedTitle, binding.mostlyPlayedRecyclerView)

        binding.optionBtn.setOnClickListener { showPopupMenu() }
    }

    private fun loadDataFromAssets() {
        val gson = Gson()

        // Load songs.json
        val songsJson = assets.open("songs.json").bufferedReader().use { it.readText() }
        allSongs = gson.fromJson(songsJson, object : TypeToken<List<SongModel>>() {}.type)
        val songMap = allSongs.associateBy { it.id }

        // Save song map globally
        LocalSongs.songMap = songMap

        // Load categories.json
        val categoriesJson = assets.open("categories.json").bufferedReader().use { it.readText() }
        categories = gson.fromJson(categoriesJson, object : TypeToken<List<CategoryModel>>() {}.type)
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(this, binding.optionBtn)
        popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun logout() {
        MyExoplayer.getInstance()?.release()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    private fun showPlayerView() {
        binding.playerView.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }

        MyExoplayer.getCurrentSong()?.let { song ->
            binding.playerView.visibility = View.VISIBLE
            binding.songTitleTextView.text = "Now Playing: ${song.title}"
            Glide.with(binding.songCoverImageView)
                .load(song.coverUrl)
                .apply(RequestOptions().transform(RoundedCorners(32)))
                .into(binding.songCoverImageView)
        } ?: run {
            binding.playerView.visibility = View.GONE
        }
    }

    private fun setupCategoryRecyclerView(categoryList: List<CategoryModel>) {
        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.adapter = categoryAdapter
    }

    private fun setupSectionUI(
        sectionId: String,
        mainLayout: RelativeLayout,
        titleView: TextView,
        recyclerView: androidx.recyclerview.widget.RecyclerView
    ) {
        val section = categories.find { it.id == sectionId }
        section?.let {
            mainLayout.visibility = View.VISIBLE
            titleView.text = it.name
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = SectionSongListAdapter(it.songs, LocalSongs.songMap)
            mainLayout.setOnClickListener {
                SongsListActivity.category = section
                startActivity(Intent(this, SongsListActivity::class.java))
            }
        }
    }
}
