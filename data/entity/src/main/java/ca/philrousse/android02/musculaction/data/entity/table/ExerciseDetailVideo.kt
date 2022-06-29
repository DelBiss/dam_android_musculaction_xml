package ca.philrousse.android02.musculaction.data.entity.table

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExerciseDetailVideo (
    var videoUrl:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null
){
    override fun toString(): String {
        return "ExerciseDetailVideo(id=$id,videoUrl=$videoUrl)"
    }
}
