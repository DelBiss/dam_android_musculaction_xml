package ca.philrousse.android02.musculaction.data.entity.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import ca.philrousse.android02.musculaction.data.entity.DescriptiveCard


@Entity
data class ExercisesCategory(
    override var name:String="N/D",
    override var description:String="N/D",
    override var imageID:String="N/D",
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null
):DescriptiveCard{

    override fun toString(): String {
        return "ExercisesCategory(id=$id,name=$name,imageID=$imageID)"
    }
}
