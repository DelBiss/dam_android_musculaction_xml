package ca.philrousse.android02.musculaction.data

import ca.philrousse.android02.musculaction.data.entity.Exercise
import ca.philrousse.android02.musculaction.data.entity.ExerciseDetail
import ca.philrousse.android02.musculaction.data.entity.views.ExerciseView
//import ca.philrousse.android02.musculaction.data.local.database.MusculactionLocalDAO
import javax.inject.Inject
import javax.inject.Singleton
//
//@Singleton
//class MusculactionLocalRepository @Inject constructor(private val dao: MusculactionLocalDAO) :
//    IMusculactionRepository {
//
//    override fun getCategoryCards() = dao.getCategoryCardsCollection()
//    override fun getExerciseCards(categoryId:Long) = dao.getCategoryExercisesCollections(categoryId)
//    override fun getExerciseDetails(exerciseId:Long) = dao.getExerciseDetailsCollections(exerciseId)
//    override fun insertOrUpdate(exerciseView: ExerciseView): InsertOrUpdate {
//        val id = dao.insert(exerciseView)
//
//        return if(id == exerciseView.id){
//            InsertOrUpdate.UPDATE
//        } else {
//            InsertOrUpdate.INSERT
//        }
//    }
//
//    override fun delete(exerciseDetails:ExerciseDetail) {
//        dao.delete(exerciseDetails)
//    }
//
//    override fun delete(exercise: Exercise) {
//        dao.delete(exercise)
//    }
//
//    companion object{
//        @Volatile
//        private var instance: MusculactionLocalRepository? = null
//        fun getInstance(
//            dao: MusculactionLocalDAO
//        )= instance ?: synchronized(this) {
//                instance ?: MusculactionLocalRepository(dao).also { instance = it }
//            }
//
//        }
//
//
//}
