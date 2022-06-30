package ca.philrousse.android02.musculaction.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ca.philrousse.android02.musculaction.data.entity.*
import kotlinx.coroutines.flow.Flow


@Dao
interface MusculactionDAO {


    @Transaction
    @Query("SELECT * FROM ExercisesCategory")
    fun getFullCategories(): Flow<List<CategoryHierachy>>

    @Query("SELECT * FROM ExercisesCategory")
    fun getCategories(): Flow<List<ExercisesCategory>>

    @Insert
    fun insert(item: ExercisesCategory):Long

    @Insert
    fun insert(item: Exercise):Long

    @Insert
    fun insert(item: ExerciseDetail):Long

    @Insert
    fun insert(item: ExerciseDetailVideo):Long

    @Insert
    fun insert(item: ExercisesSubcategory):Long



    fun insert(parentId:Long, itemView: ExerciceDetailHierachy):Long{
        itemView.item.parentId = parentId
        val itemId = insert(itemView.item)
        itemView.childs.forEach {
            it.parentId = itemId
            insert(it)
        }
        return itemId
    }
    fun insert(parentId:Long, itemView: ExerciseHierachy):Long{
        itemView.item.parentId = parentId
        val itemId = insert(itemView.item)
        itemView.childs.forEach {
            insert(itemId,it)
        }
        return itemId
    }

    fun insert(parentId:Long, itemView: SubcategoryHierachy):Long{
        itemView.item.parentId = parentId
        val itemId = insert(itemView.item)
        itemView.childs.forEach {
            insert(itemId,it)
        }
        return itemId
    }
    fun insert(itemView: CategoryHierachy):Long{
        val itemId = insert(itemView.item)
        itemView.childs.forEach{
            insert(itemId,it)
        }
        return itemId
    }
}