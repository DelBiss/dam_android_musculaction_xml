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
    fun getFullCategories(): Flow<List<HierarchicCategory>>

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
        itemView.parentId = parentId
        val itemId = insert(itemView as ExerciseDetail)
        itemView.child.forEach {
            it.parentId = itemId
            insert(it)
        }
        return itemId
    }
    fun insert(parentId:Long, itemView: ExerciseHierarchic):Long{
        itemView.parentId = parentId
        val itemId = insert(itemView as Exercise)
        itemView.child.forEach {
            insert(itemId,it)
        }
        return itemId
    }

    fun insert(parentId:Long, itemView: SubcategoryHierarchic):Long{
        itemView.parentId = parentId
        val itemId = insert(itemView as Subcategory)
        itemView.child.forEach {
            insert(itemId,it)
        }
        return itemId
    }
    fun insert(itemView: HierarchicCategory):Long{
        val itemId = insert(itemView as Category)
        itemView.child.forEach{
            insert(itemId,it)
        }
        return itemId
    }
}