package ca.philrousse.android02.musculaction.data.entity

import androidx.room.Relation

class HierarchicCategory (
    name:String="N/D",
    imageID:String="N/D",
    description:String="N/D",
    id:Long?=null,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Subcategory::class
    )
    val child: List<SubcategoryHierarchic> = listOf(),

):Category(name, description, imageID, id){
    override fun toString(): String {
        return "<Hierarchic${super.toString()}+ChildCount*${child.count()}>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HierarchicCategory) return false
        if(
            super.equals(other) &&
            this.child == other.child
        ) return true
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + child.hashCode()
        return result
    }
}

class SubcategoryHierarchic(
    name:String="N/D",
    parentId:Long?=null,
    id:Long?=null,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Exercise::class
    )
    val child:List<ExerciseHierarchic> = listOf(),

):Subcategory(name, parentId, id){
    override fun toString(): String {
        return "<Hierarchic${super.toString()}+ChildCount*${child.count()}>"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HierarchicCategory) return false
        if(
            super.equals(other) &&
            this.child == other.child
        ) return true
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + child.hashCode()
        return result
    }
}

class ExerciseHierarchic(
     name:String="N/D",
     description:String="N/D",
     imageID:String="N/D",
     parentId:Long?=null,
     id:Long?=null,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetail::class
    )
    val child:List<ExercisesDetailHierarchic> = listOf(),

): Exercise(name, description,imageID,parentId, id)

class ExercisesDetailHierarchic(
     name:String="N/D",
     description:String="N/D",
     parentId:Long?=null,
     id:Long?=null,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetailVideo::class
    )
    val child:List<ExerciseDetailVideo> = listOf(),

):ExerciseDetail(name, description, parentId, id)

