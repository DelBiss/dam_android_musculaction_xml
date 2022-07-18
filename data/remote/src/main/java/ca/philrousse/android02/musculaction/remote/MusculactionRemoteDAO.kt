package ca.philrousse.android02.musculaction.remote

import android.util.Log
import ca.philrousse.android02.musculaction.data.entity.views.ICard
import ca.philrousse.android02.musculaction.data.entity.views.IViewCards
import ca.philrousse.android02.musculaction.data.entity.views.IViewCardsCollections
import com.google.firebase.firestore.*
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

inline fun <reified R> DocumentSnapshot.transformCollectionListAsFlow(collectionName:String, crossinline transform:(doc:QueryDocumentSnapshot)->R): Flow<List<R>?> {
    return reference.collection(collectionName).getAsFlow().map { value ->
        value?.map{
            transform(it)
        }
    }
}

inline fun <reified R> DocumentSnapshot.combineCollectionListAsFlow(collectionName:String, crossinline transform:(doc:QueryDocumentSnapshot)->Flow<R>):Flow<List<R>?>{
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

    fun getCategoriesData(): Flow<List<ICard>> {
        val collection = db.collection("categories")
        return collection.getAsFlow().map { it: QuerySnapshot? ->
//            it?.toObjects(DataCategoryDocument::class.java) ?: listOf()
            it?.let{ query:QuerySnapshot ->
                query.map { doc->
                    CategoryItemDocument(doc)
                }
            } ?: listOf()
        }
    }

    fun getExerciseList(categoryPath:String): Flow<IViewCardsCollections> {
        val docRef = db.document(categoryPath)
        val flow1 = docRef.getAsFlow().filterNotNull()
        val flow2 = docRef.collection("exercises").getAsFlow().map { query ->
            query?.map {
                ExerciseItemDocument(it)
            }?: emptyList()
        }
        return flow1.combine(flow2){
          categorieDoc,exercises  ->
            CategoryDetailDocument(categorieDoc,exercises)
        }

    }

    fun getExerciseDetail(categoryPath:String): Flow<IViewCards> {
        val docRef = db.document(categoryPath)
        val flow1 = docRef.getAsFlow().filterNotNull()
        val flow2 = docRef.collection("details").getAsFlow().map { query ->
            Log.d(TAG, "getExerciseDetail: Flow detail")
            query?.sortedBy {
                it.getLong("order")
            }?.map {
                ExerciseDocumentDetail(it)
            }?: emptyList()
        }
        return flow1.combine(flow2){
                categorieDoc,exercises  ->
            Log.d(TAG, "getExerciseDetail: Receive Doc ${categorieDoc.reference.path} and ${exercises.count()}")
            ExerciseDetailDocument(categorieDoc,exercises)
        }

    }



    fun updateExercise(doc:ExerciseDetailDocument){
        Log.d(TAG, "updateExercise: ")
        doc.snapshot.reference.update(mapOf(
            "description" to doc.description,
            "name" to doc.name,
            "short_description" to doc.short_description,
        ))

        doc.child.forEach {
            if (it is ExerciseDocumentDetail) {
                val detail = it as ExerciseDocumentDetail
                detail.snapshot.reference.update(
                    mapOf(
                        "description" to detail.description,
                        "name" to detail.name,
                        "video" to detail.video,
                        "order" to detail.order,
                    )
                )
            } else if(it is NewDetail){
                val detail = it as NewDetail
                doc.snapshot.reference.collection("details").document(detail.name).set(detail)
            }


        }
    }

    fun insertExercise(doc: NewExercises) {
        Log.d(TAG, "insertExercise: ")
        val docRef = db.document(doc.parentId).collection("exercises").document(doc.name)
        docRef.set(doc)
        doc.child.forEach {
            val newDetail = it as NewDetail
            docRef.collection("details").document(newDetail.name).set(newDetail)
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