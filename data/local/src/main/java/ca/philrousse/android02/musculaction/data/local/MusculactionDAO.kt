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
    @Query("SELECT * FROM Category")
    fun getFullCategories(): Flow<List<CategoryHierarchic>>

    @Query("SELECT * FROM Category")
    fun getCategories(): Flow<List<Category>>

    @Insert
    fun insert(item: Category):Long

    @Insert
    fun insert(item: Exercise):Long

    @Insert
    fun insert(item: ExerciseDetail):Long

    @Insert
    fun insert(item: ExerciseDetailVideo):Long

    @Insert
    fun insert(item: Subcategory):Long



    fun insert(parentId:Long, itemView: ExercisesDetailHierarchic):Long{
        itemView.item.parentId = parentId
        val itemId = insert(itemView.item)
        itemView.videos.forEach {
            it.parentId = itemId
            insert(it)
        }
        return itemId
    }
    fun insert(parentId:Long, itemView: ExerciseHierarchic):Long{
        itemView.item.parentId = parentId
        val itemId = insert(itemView.item)
        itemView.exercise_details.forEach {
            insert(itemId,it)
        }
        return itemId
    }

    fun insert(parentId:Long, itemView: SubcategoryHierarchic):Long{
        itemView.item.parentId = parentId
        val itemId = insert(itemView.item)
        itemView.exercises.forEach {
            insert(itemId,it)
        }
        return itemId
    }
    fun insert(itemView: CategoryHierarchic):Long{
        val itemId = insert(itemView.item)
        itemView.subcategories.forEach{
            insert(itemId,it)
        }
        return itemId
    }
}