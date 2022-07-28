package ca.philrousse.android02.musculaction.data

import android.util.Log
import ca.philrousse.android02.musculaction.data.entity.views.*
import ca.philrousse.android02.musculaction.remote.*
//import ca.philrousse.android02.musculaction.data.local.database.MusculactionLocalDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "MusculactionRemoteRepos"

class MusculactionRemoteRepository @Inject constructor(private val dao: MusculactionRemoteDAO) :
    IMusculactionRepository {
    override fun getCategoryCards(): Flow<List<ICard>> = dao.getCategoriesData()

    override fun getExerciseCards(categoryId: String): Flow<IViewCardsCollections> {
        Log.d(TAG, "getExerciseCards: $categoryId")
        return dao.getExerciseList(categoryId)
    }

    override fun getExerciseDetails(exerciseId: String): Flow<IViewCards> {
        Log.d(TAG, "getExerciseDetails: $exerciseId")
        return dao.getExerciseDetail(exerciseId)
//        return flow {
//            val collectionList = ExerciseView()
//            emit(collectionList)
//        }
    }

    override fun insertOrUpdate(exerciseView: ExerciseView): InsertOrUpdate {
        Log.d(TAG, "insertOrUpdate: $exerciseView")
        return InsertOrUpdate.FAIL
    }


    override fun insertOrUpdate(doc: IViewCards): InsertOrUpdate {
        Log.d(TAG, "insertOrUpdate: ${doc.id}, ${doc.name}")
        var res:InsertOrUpdate
        var exerciseId:String=""
        if (doc is ExerciseDetailDocument) {
            dao.updateExercise(doc)
            return InsertOrUpdate.UPDATE

        } else if (doc is NewExercises){
            dao.insertExercise(doc)
            return InsertOrUpdate.INSERT
        }
        return InsertOrUpdate.FAIL
    }

    override fun delete(doc: IListElement) {
        Log.d(TAG, "delete: ${doc.id}, ${doc.name}")
        if (doc is DocumentData) {
            val documentData = doc as DocumentData
            documentData.delete()
        }
    }

    override fun createNewExerciseView(parentId: String): NewExercises {
        Log.d(TAG, "createNewExerciseView: $parentId")
        return NewExercises(parentId)
    }

    override fun createNewCardExerciseDetail(): NewDetail {
        Log.d(TAG, "createNewCardExerciseDetail: ")
        return NewDetail()
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