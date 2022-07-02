package ca.philrousse.android02.musculaction.data

import ca.philrousse.android02.musculaction.data.local.database.MusculactionDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusculactionLocalRepository @Inject constructor(private val dao: MusculactionDAO) :
    IMusculactionRepository {

    override fun getCategoryCards() = dao.getCategoryCardsCollection()
    override fun getExerciseCards(categoryId:Long) = dao.getCategoryExercisesCollections(categoryId)
    override fun getExerciseDetails(exerciseId:Long) = dao.getExerciseDetailsCollections(exerciseId)

    companion object{
        @Volatile
        private var instance: MusculactionLocalRepository? = null
        fun getInstance(
            dao: MusculactionDAO
        )= instance ?: synchronized(this) {
                instance ?: MusculactionLocalRepository(dao).also { instance = it }
            }

        }


}
