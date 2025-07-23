package np.com.bimalkafle.musicstream.models

data class CategoryModel(
    var id : String,
    val name : String,
    val coverUrl : String,
    var songs : List<String>
) {
    constructor() : this("", "","", listOf())
}
