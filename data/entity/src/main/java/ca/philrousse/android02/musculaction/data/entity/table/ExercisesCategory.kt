package ca.philrousse.android02.musculaction.data.entity.table

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExercisesCategory(
    var name:String="N/D",
    var description:String="N/D",
    var imageID:String="N/D",
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null
){
    override fun toString(): String {
        return "ExercisesCategory(id=$id,name=$name,imageID=$imageID)"
    }
}
