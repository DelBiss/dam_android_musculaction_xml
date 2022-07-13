package ca.philrousse.android02.musculaction.remote

import android.util.Log
import ca.philrousse.android02.musculaction.data.entity.Category
import ca.philrousse.android02.musculaction.data.entity.views.CardCategory
import com.google.firebase.firestore.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "MusculactionRemoteDAO"

fun DocumentReference.getAsFlow() = callbackFlow {
    val sub = get().addOnSuccessListener {
        trySend(it)
    }.addOnFailureListener {
        trySend(null)
    }
    awaitClose {}
}
fun CollectionReference.getAsFlow(): Flow<QuerySnapshot?> = callbackFlow {
    val sub = get().addOnSuccessListener {
        trySend(it)
    }.addOnFailureListener {
        trySend(null)
    }
    awaitClose {}
}

inline fun <reified R> QueryDocumentSnapshot.transformCollectionListAsFlow(collectionName:String, crossinline transform:(doc:QueryDocumentSnapshot)->R): Flow<List<R>?> {
    return reference.collection(collectionName).getAsFlow().map { value ->
        value?.map{
            transform(it)
        }
    }
}

inline fun <reified R> QueryDocumentSnapshot.combineCollectionListAsFlow(collectionName:String, crossinline transform:(doc:QueryDocumentSnapshot)->Flow<R>):Flow<List<R>?>{
    return transformCollectionListAsFlow(collectionName,transform).flatMapLatest { fl->
        fl?.let {
            instantCombine(fl)
        } ?: listOf<List<R>?>(null).asFlow()

    }

}

inline fun <reified T> instantCombine(flows: List<Flow<T>>) = channelFlow {
    val array = arrayOfNulls<T>(flows.size)
    flows.forEachIndexed { index, flow ->
        launch {
            flow.collect {
                array[index] = it
                send(array.filterNotNull())
            }
        }
    }
}

class MusculactionRemoteDAO {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCategoriesData(): Flow<List<CardCategory>> {
        val collection = db.collection("categories")
        return collection.getAsFlow().flatMapLatest { query: QuerySnapshot? ->
            query?.let{
                instantCombine(it.map { doc->CategoryDocument(doc).asCardCategory() }).onEach {  }
            } ?: listOf<List<CardCategory>>().asFlow()
        }
    }

    fun getExerciseList(categoryPath:String){

        db.document(categoryPath).getAsFlow().filterNotNull().map {
            CategoryDocument(it).asCategoryExercisesCollections()
        }
    }
    companion object {
        @Volatile
        private var instance: MusculactionRemoteDAO? = null

        fun getInstance(): MusculactionRemoteDAO {
            return instance ?: synchronized(this) {
                instance ?: MusculactionRemoteDAO().also {
                    instance = it
                } }
            }
        }
}