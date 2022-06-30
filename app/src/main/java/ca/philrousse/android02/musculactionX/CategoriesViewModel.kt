package ca.philrousse.android02.musculactionX

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.philrousse.android02.musculaction.data.MusculactionRepository
import ca.philrousse.android02.musculaction.data.entity.ExercisesCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject internal constructor(
    musculactionRepository: MusculactionRepository
) : ViewModel() {
    val categoryList: Flow<List<ExercisesCategory>> =
        musculactionRepository.getCategories()
}

