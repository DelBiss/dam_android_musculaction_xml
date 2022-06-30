package ca.philrousse.android02.musculaction.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Category(
    override var name:String="N/D",
    override var description:String="N/D",
    override var imageID:String="N/D",
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
): DescriptiveCard(name, description, imageID, id) {


    override fun toString(): String {
        return "ExercisesCategory(${super.toString()})"
    }
}

@Entity
data class Subcategory(
    var name:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null
){
    override fun toString(): String {
        return "ExercisesSubcategory(id=$id,name=$name)"
    }
}

@Entity
data class Exercise(
    override var name:String="N/D",
    override var description:String="N/D",
    override var imageID:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
): DescriptiveCard(name, description, imageID, id) {
    override fun toString(): String {
        return "Exercise(${super.toString()})"
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