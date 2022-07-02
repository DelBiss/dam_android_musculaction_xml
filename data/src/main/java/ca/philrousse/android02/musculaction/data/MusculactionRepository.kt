package ca.philrousse.android02.musculaction.data

import ca.philrousse.android02.musculaction.data.local.database.MusculactionDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusculactionRepository @Inject constructor(private val dao: MusculactionDAO){

    fun getCategoryCards() = dao.getCategoryCardsCollection()
    fun getExerciseCards(categoryId:Long) = dao.getCategoryExercisesCollections(categoryId)
    fun getExerciseDetails(exerciseId:Long) = dao.getExerciseDetailsCollections(exerciseId)

    companion object{
        @Volatile
        private var instance: MusculactionRepository? = null
        fun getInstance(
            dao: MusculactionDAO
        )= instance ?: synchronized(this) {
                instance ?: MusculactionRepository(dao).also { instance = it }
            }

        }


}
