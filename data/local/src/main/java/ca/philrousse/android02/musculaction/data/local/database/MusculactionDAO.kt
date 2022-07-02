package ca.philrousse.android02.musculaction.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ca.philrousse.android02.musculaction.data.entity.*
import ca.philrousse.android02.musculaction.data.entity.views.CardCategory
import ca.philrousse.android02.musculaction.data.entity.views.CategoryExercisesCollections
import ca.philrousse.android02.musculaction.data.entity.views.ExerciseView
import kotlinx.coroutines.flow.Flow


@Dao
interface MusculactionDAO {


    @Transaction
    @Query("SELECT * FROM Category")
    fun getCategoryCardsCollection(): Flow<List<CardCategory>>

    @Transaction
    @Query("SELECT * FROM Category WHERE id = :categoryId")
    fun getCategoryExercisesCollections(categoryId:Long): Flow<CategoryExercisesCollections>

    @Transaction
    @Query("SELECT * FROM Exercise WHERE id = :exerciseId")
    fun getExerciseDetailsCollections(exerciseId:Long): Flow<ExerciseView>

    @Insert
    fun insert(item: Category):Long

    @Insert
    fun insert(item: Exercise):Long

    @Insert
    fun insert(item: Image)

    @Insert
    fun insert(item: ExerciseDetail):Long

    @Insert
    fun insert(item: ExerciseDetailVideo):Long

    @Insert
    fun insert(item: Subcategory):Long


}