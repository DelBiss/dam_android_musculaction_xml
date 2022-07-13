package ca.philrousse.android02.musculaction.data.local.database

import android.util.Log
import androidx.room.*
import ca.philrousse.android02.musculaction.data.entity.*
import ca.philrousse.android02.musculaction.data.entity.views.CardCategory
import ca.philrousse.android02.musculaction.data.entity.views.CardExerciseDetail
import ca.philrousse.android02.musculaction.data.entity.views.CategoryExercisesCollections
import ca.philrousse.android02.musculaction.data.entity.views.ExerciseView
import kotlinx.coroutines.flow.Flow

private const val TAG = "MusculactionDAO"

@Dao
interface MusculactionDAO {



    @Transaction
    @Query("SELECT * FROM Category")
    fun getCategoryCardsCollection(): Flow<List<CardCategory>>

    @Transaction
    @Query("SELECT * FROM Category WHERE id = :categoryId")
    fun getCategoryExercisesCollections(categoryId:Long): Flow<CategoryExercisesCollections>

    @Transaction
    @Query("SELECT * FROM Exercise WHERE id = :exerciseId LIMIT 1")
    fun getExerciseDetailsCollections(exerciseId:Long): Flow<ExerciseView>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Category):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Exercise):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Image)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ExerciseDetail):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ExerciseDetailVideo):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Subcategory):Long

    @Delete
    fun delete(vararg exerciseDetails: ExerciseDetail)

    @Delete
    fun delete(exercise: Exercise)

    fun insert(item: ExerciseView, parentId:Long? = null):Long{

        item.exercise.parentId = item.exercise.parentId?:parentId

        assert(item.exercise.parentId != null) { "Parent ID is null" }
        assert(parentId ==null || item.exercise.parentId == parentId) { "Parent ID are different" }

        val itemId = insert(item.exercise)

        if(item.exercise.id == itemId){
            Log.d(TAG, "Update [exercise]: id=${itemId}, name='${item.exercise.name}'")
        } else {
            Log.d(TAG, "Insert [exercise]: id=${itemId}, name='${item.exercise.name}'")
        }

        item.child.forEach {
            insert(it, itemId)
        }

        return itemId

    }

    fun insert(item: CardExerciseDetail, parentId:Long? = null):Long{

        item.detail.parentId = item.detail.parentId?:parentId
        assert(item.detail.parentId != null) { "Parent ID is null" }
        assert(parentId ==null || item.detail.parentId == parentId) { "Parent ID are different" }

        val itemId = insert(item.detail)

        if(item.detail.id == itemId){
            Log.d(TAG, "Update [exerciseDetail]: id=${itemId}, name='${item.detail.name}'")
        } else {
            Log.d(TAG, "Insert [exerciseDetail]: id=${itemId}, name='${item.detail.name}'")
        }

        item.videosList.forEach {
            insert(it, itemId)
        }

        return itemId
    }

    fun insert(item: ExerciseDetailVideo, parentId:Long? = null):Long{

        item.parentId = item.parentId?:parentId
        assert(item.parentId != null) { "Parent ID is null" }
        assert(parentId ==null || item.parentId == parentId) { "Parent ID are different" }

        val itemId = insert(item)

        if(item.id == itemId){
            Log.d(TAG, "Update [exerciseDetail]: id=${itemId}, name='${item.videoUrl}'")
        } else {
            Log.d(TAG, "Insert [exerciseDetail]: id=${itemId}, name='${item.videoUrl}'")
        }

        return itemId
    }
}