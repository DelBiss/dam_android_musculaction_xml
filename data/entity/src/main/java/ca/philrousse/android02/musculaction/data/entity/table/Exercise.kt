package ca.philrousse.android02.musculaction.data.entity.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import ca.philrousse.android02.musculaction.data.entity.DescriptiveCard


@Entity
data class Exercise(
    override var name:String="N/D",
    override var description:String="N/D",
    override var imageID:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
):DescriptiveCard{
    override fun toString(): String {
        return "Exercise(id=$id,name=$name,imageID=$imageID)"
    }
}
