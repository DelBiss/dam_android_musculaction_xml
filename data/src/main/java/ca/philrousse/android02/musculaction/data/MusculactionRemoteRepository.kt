package ca.philrousse.android02.musculaction.data

import android.util.Log
import ca.philrousse.android02.musculaction.data.entity.Exercise
import ca.philrousse.android02.musculaction.data.entity.ExerciseDetail
import ca.philrousse.android02.musculaction.data.entity.views.CardCategory
import ca.philrousse.android02.musculaction.data.entity.views.CategoryExercisesCollections
import ca.philrousse.android02.musculaction.data.entity.views.ExerciseView
//import ca.philrousse.android02.musculaction.data.local.database.MusculactionLocalDAO
import ca.philrousse.android02.musculaction.remote.MusculactionRemoteDAO
import ca.philrousse.android02.musculaction.remote.PathIdDirectory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "MusculactionRemoteRepos"

class MusculactionRemoteRepository @Inject constructor(private val dao: MusculactionRemoteDAO) :
    IMusculactionRepository {
    override fun getCategoryCards(): Flow<List<CardCategory>> = dao.getCategoriesData()

    override fun getExerciseCards(categoryId: Long): Flow<CategoryExercisesCollections> {
        val s = PathIdDirectory.getInstance().getPath("category",categoryId)
        return flow {
            val collectionList = CategoryExercisesCollections()
            emit(collectionList)
        }
    }

    override fun getExerciseDetails(exerciseId: Long): Flow<ExerciseView> {
        return flow {
            val collectionList = ExerciseView()
            emit(collectionList)
        }
    }

    override fun insertOrUpdate(exerciseView: ExerciseView): InsertOrUpdate {
        return InsertOrUpdate.FAIL
    }

    override fun delete(exerciseDetails: ExerciseDetail) {

    }

    override fun delete(exercise: Exercise) {

    }

    companion object{
        @Volatile
        private var instance: MusculactionRemoteRepository? = null
        fun getInstance(
            dao: MusculactionRemoteDAO
        )= instance ?: synchronized(this) {
            instance ?: MusculactionRemoteRepository(dao).also { instance = it }
        }

    }
}