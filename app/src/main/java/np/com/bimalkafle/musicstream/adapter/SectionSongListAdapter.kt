package np.com.bimalkafle.musicstream.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import np.com.bimalkafle.musicstream.MyExoplayer
import np.com.bimalkafle.musicstream.PlayerActivity
import np.com.bimalkafle.musicstream.databinding.SectionSongListRecyclerRowBinding
import np.com.bimalkafle.musicstream.models.SongModel

class SectionSongListAdapter(
    private val songIdList: List<String>,
    private val songMap: Map<String, SongModel>
) : RecyclerView.Adapter<SectionSongListAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: SectionSongListRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(song: SongModel) {
            binding.songTitleTextView.text = song.title
            binding.songSubtitleTextView.text = song.subtitle

            Glide.with(binding.songCoverImageView.context)
                .load(song.coverUrl) // e.g. file:///android_asset/images/Blue.jpg
                .apply(RequestOptions().transform(RoundedCorners(32)))
                .into(binding.songCoverImageView)

            binding.root.setOnClickListener {
                MyExoplayer.startPlaying(binding.root.context, song)
                it.context.startActivity(Intent(it.context, PlayerActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SectionSongListRecyclerRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = songIdList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val songId = songIdList[position]
        val song = songMap[songId]
        song?.let {
            holder.bindData(it)
        }
    }
}
