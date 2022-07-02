package ca.philrousse.android02.musculactionX

import androidx.lifecycle.ViewModel
import ca.philrousse.android02.musculaction.data.MusculactionRepository
import ca.philrousse.android02.musculaction.data.entity.CardCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject internal constructor(
    musculactionRepository: MusculactionRepository
) : ViewModel() {
    val categoryList: Flow<List<CardCategory>> =
        musculactionRepository.getCategoryCards()
}

