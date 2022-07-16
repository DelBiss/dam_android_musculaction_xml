package ca.philrousse.android02.musculactionX.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.philrousse.android02.musculaction.data.IMusculactionRepository
import ca.philrousse.android02.musculaction.data.InsertOrUpdate
import ca.philrousse.android02.musculaction.data.entity.Exercise
import ca.philrousse.android02.musculaction.data.entity.views.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject internal constructor(
    private val musculactionRepository: IMusculactionRepository
) : ViewModel() {

    val categoryList: Flow<List<ICard>> =
        musculactionRepository.getCategoryCards()

    fun exercisesByCategory(categoryId: Long): Flow<IViewCardsCollections> {
        return musculactionRepository.getExerciseCards(categoryId)
    }

    fun exercisesDetail(exerciseId: Long): Flow<IViewCards> {
        return musculactionRepository.getExerciseDetails(exerciseId)
    }

    fun deleteExercise(exercise: Exercise, callback: ((Boolean) -> Unit)? = null) = CoroutineScope(Dispatchers.IO).launch {
        musculactionRepository.delete(exercise)
        callback?.let { cb ->
            cb(true)
        }
    }

}

@HiltViewModel
class EditViewModel @Inject internal constructor(
    private val musculactionRepository: IMusculactionRepository
) : ViewModel() {
    private val removedSection = mutableListOf<ICard>()

    private val _editedExercise = MutableStateFlow<ExerciseView?>(null)
    val editedExercise = _editedExercise.asStateFlow()

    private val _sections = MutableStateFlow<List<CardExerciseDetail>>(listOf())
    val sections = _sections.asStateFlow()

//    private var isLoading = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            sections.collect{ list->
                editedExercise.value?.let {
                    it.child = list
                }
            }
        }
    }


    fun deleteExercise(callback: ((Boolean) -> Unit)? = null)= CoroutineScope(Dispatchers.IO).launch {
        var isSuccess = false
        editedExercise.value?.let {
            it.id?.let { _->
                musculactionRepository.delete(it.exercise)
                isSuccess = true
            }
        }

        callback?.let { cb ->
            cb(isSuccess)
        }
    }

    fun commitChange(callback: ((InsertOrUpdate)->Unit)? = null) = CoroutineScope(Dispatchers.IO).launch {
        removedSection.forEach {
            it.id?.let { _ ->
                musculactionRepository.delete(it.detail)
            }
        }
        val commitResult = editedExercise.value?.let { musculactionRepository.insertOrUpdate(it) } ?: InsertOrUpdate.FAIL
        callback?.let {
            it(commitResult)
        }
    }


    fun hookViewModel(exerciseId: Long, categoryId: Long){

        if(exerciseId == -1L){
            _editedExercise.value = musculactionRepository.createNewExerciseView(categoryId)
                //ExerciseView(parentId = categoryId, image = "ic_baseline_add_24")
        } else{
            viewModelScope.launch {
                musculactionRepository.getExerciseDetails(exerciseId).collect{
                    _editedExercise.value = it
                    _sections.value = it.child
                    this.coroutineContext.job.cancel()
                }
            }
        }


    }


    fun addSection(){
        editedExercise.value?.let {
            val newList = sections.value.toMutableList()
            newList.add(musculactionRepository.createNewCardExerciseDetail(it.id))
//            newList.add(CardExerciseDetail("Nouvelle section #${newList.count()+1}",it.id))
            _sections.value = newList
        }

    }
    fun removeSection(section:ICard){
        removedSection.add(section)
        val newList = sections.value.toMutableList()
        newList.remove(section)
        _sections.value = newList
    }



}