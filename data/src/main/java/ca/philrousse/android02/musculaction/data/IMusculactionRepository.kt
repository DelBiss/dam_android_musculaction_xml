package ca.philrousse.android02.musculaction.data

import ca.philrousse.android02.musculaction.data.entity.views.CardCategory
import ca.philrousse.android02.musculaction.data.entity.views.CategoryExercisesCollections
import ca.philrousse.android02.musculaction.data.entity.views.ExerciseView
import kotlinx.coroutines.flow.Flow

interface IMusculactionRepository {
    fun getCategoryCards(): Flow<List<CardCategory>>
    fun getExerciseCards(categoryId: Long): Flow<CategoryExercisesCollections>
    fun getExerciseDetails(exerciseId: Long): Flow<ExerciseView>


}