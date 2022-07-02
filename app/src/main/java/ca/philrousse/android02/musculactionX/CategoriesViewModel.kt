package ca.philrousse.android02.musculactionX

import androidx.lifecycle.ViewModel
import ca.philrousse.android02.musculaction.data.IMusculactionRepository
import ca.philrousse.android02.musculaction.data.entity.views.CardCategory
import ca.philrousse.android02.musculaction.data.entity.views.CategoryExercisesCollections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject internal constructor(
    private val musculactionRepository: IMusculactionRepository
) : ViewModel() {

    val categoryList: Flow<List<CardCategory>> =
        musculactionRepository.getCategoryCards()

    fun exercisesByCategory(categoryId: Long): Flow<CategoryExercisesCollections> {
        return musculactionRepository.getExerciseCards(categoryId)
    }


}

