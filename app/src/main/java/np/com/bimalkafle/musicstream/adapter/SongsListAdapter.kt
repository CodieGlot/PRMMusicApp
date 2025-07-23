package np.com.bimalkafle.musicstream.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import np.com.bimalkafle.musicstream.MyExoplayer
import np.com.bimalkafle.musicstream.PlayerActivity
import np.com.bimalkafle.musicstream.SongsListActivity
import np.com.bimalkafle.musicstream.databinding.SongListItemRecyclerRowBinding
import np.com.bimalkafle.musicstream.models.SongModel
import android.util.Log
import android.widget.Toast
class SongsListAdapter(private  val songIdList : List<String>) :
    RecyclerView.Adapter<SongsListAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: SongListItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        //bind data with view
        fun bindData(songId: String) {
            // Log xem bài hát nào đang được bind
            Log.d("SongsListAdapter", "Binding songId: $songId")
            Toast.makeText(binding.root.context, "Loading song: $songId", Toast.LENGTH_SHORT).show()

            FirebaseFirestore.getInstance().collection("songs")
                .document(songId).get()
                .addOnSuccessListener {
                    val song = it.toObject(SongModel::class.java)
                    if (song != null) {
                        Log.d("SongsListAdapter", "Loaded song: ${song.title}")
                        binding.songTitleTextView.text = song.title
                        binding.songSubtitleTextView.text = song.subtitle
                        Glide.with(binding.songCoverImageView).load(song.coverUrl)
                            .apply(RequestOptions().transform(RoundedCorners(32)))
                            .into(binding.songCoverImageView)

                        binding.root.setOnClickListener {
                            MyExoplayer.startPlaying(binding.root.context, song)
                            it.context.startActivity(Intent(it.context, PlayerActivity::class.java))
                        }
                    } else {
                        Log.w("SongsListAdapter", "Song not found for ID: $songId")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("SongsListAdapter", "Error loading song with ID: $songId", e)
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SongListItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songIdList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(songIdList[position])
    }

}