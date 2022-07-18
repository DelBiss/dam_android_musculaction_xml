package ca.philrousse.android02.musculaction.data


import ca.philrousse.android02.musculaction.data.entity.Exercise
import ca.philrousse.android02.musculaction.data.entity.ExerciseDetail
import ca.philrousse.android02.musculaction.data.entity.views.*
import kotlinx.coroutines.flow.Flow

enum class InsertOrUpdate{
    INSERT,
    UPDATE,
    FAIL
}
interface IMusculactionRepository {
    fun getCategoryCards(): Flow<List<ICard>>
    fun getExerciseCards(categoryId: String): Flow<IViewCardsCollections>
    fun getExerciseDetails(exerciseId: String): Flow<IViewCards>
    fun insertOrUpdate(exerciseView: ExerciseView): InsertOrUpdate
//    fun delete(exerciseDetails: ExerciseDetail)
//    fun delete(exercise: Exercise)
    fun createNewExerciseView(parentId:String):IViewCards
    fun createNewCardExerciseDetail():ICard
    fun delete(doc: IListElement)
//    fun delete(doc: ICard)
    fun insertOrUpdate(doc: IViewCards): InsertOrUpdate
}