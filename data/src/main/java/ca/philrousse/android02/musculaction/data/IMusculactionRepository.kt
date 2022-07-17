package ca.philrousse.android02.musculaction.data


import ca.philrousse.android02.musculaction.data.entity.Exercise
import ca.philrousse.android02.musculaction.data.entity.ExerciseDetail
import ca.philrousse.android02.musculaction.data.entity.views.CardCategory
import ca.philrousse.android02.musculaction.data.entity.views.CardExerciseDetail
import ca.philrousse.android02.musculaction.data.entity.views.CategoryExercisesCollections
import ca.philrousse.android02.musculaction.data.entity.views.ExerciseView
import kotlinx.coroutines.flow.Flow

enum class InsertOrUpdate{
    INSERT,
    UPDATE,
    FAIL
}
interface IMusculactionRepository {
    fun getCategoryCards(): Flow<List<CardCategory>>
    fun getExerciseCards(categoryId: Long): Flow<CategoryExercisesCollections>
    fun getExerciseDetails(exerciseId: Long): Flow<ExerciseView>
    fun insertOrUpdate(exerciseView: ExerciseView): InsertOrUpdate
    fun delete(exerciseDetails: ExerciseDetail)
    fun delete(exercise: Exercise)
    fun createNewExerciseView(parentId:Long):ExerciseView
    fun createNewCardExerciseDetail(parentId:Long?):CardExerciseDetail
}