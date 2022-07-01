package ca.philrousse.android02.musculaction.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Category(
    override var name:String="N/D",
    override var description:String="N/D",
    override var imageID:String="N/D",
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
):IDescriptiveImageCard {
    override fun equals(other: Any?): Boolean = IDescriptiveImageCard.equals(this,other)
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + imageID.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}

@Entity
open class Subcategory(
    override var name:String="N/D",
    override var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
):IListElement,IChildItem{
    override fun toString(): String {
        return "Subcategory(id=$id,name=$name)"
    }
    override fun equals(other: Any?): Boolean = IListElement.equals(this,other) && IChildItem.equals(this,other)
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (parentId?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}

@Entity
open class Exercise(
    override var name:String="N/D",
    override var description:String="N/D",
    override var imageID:String="N/D",
    override var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
): IDescriptiveImageCard,IChildItem {
    override fun toString(): String {
        return "Exercise(${super.toString()})"
    }
    override fun equals(other: Any?): Boolean = IDescriptiveImageCard.equals(this,other) && IChildItem.equals(this,other)
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + imageID.hashCode()
        result = 31 * result + (parentId?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}

@Entity
open class ExerciseDetail(
    override var name:String="N/D",
    override var description:String="N/D",
    override var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
):IDescriptiveCard,IChildItem{
    override fun toString(): String {
        return "ExerciseDetail(id=$id,name=$name)"
    }
    override fun equals(other: Any?): Boolean = IDescriptiveCard.equals(this,other) && IChildItem.equals(this,other)
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (parentId?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}

@Entity
open class ExerciseDetailVideo (
    var videoUrl:String="N/D",
    override var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
):IChildItem{
    override fun toString(): String {
        return "ExerciseDetailVideo(id=$id,videoUrl=$videoUrl)"
    }
    override fun equals(other: Any?): Boolean{
        return IChildItem.equals(this,other)
    }

    override fun hashCode(): Int {
        var result = videoUrl.hashCode()
        result = 31 * result + (parentId?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}