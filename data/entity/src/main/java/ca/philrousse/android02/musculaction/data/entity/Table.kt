package ca.philrousse.android02.musculaction.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    override var name:String="N/D",
    override var description:String="N/D",
    override var imageID:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
): DescriptiveCard {
    override fun toString(): String {
        return "Exercise(id=$id,name=$name,imageID=$imageID)"
    }
}


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

@Entity
data class ExerciseDetail(
    var name:String="N/D",
    var description:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null
){
    override fun toString(): String {
        return "ExerciseDetail(id=$id,name=$name)"
    }
}

@Entity
data class ExercisesCategory(
    override var name:String="N/D",
    override var description:String="N/D",
    override var imageID:String="N/D",
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
): DescriptiveCard {

    override fun toString(): String {
        return "ExercisesCategory(id=$id,name=$name,imageID=$imageID)"
    }
}

@Entity
data class ExercisesSubcategory(
    var name:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null
){
    override fun toString(): String {
        return "ExercisesSubcategory(id=$id,name=$name)"
    }
}