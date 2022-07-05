package ca.philrousse.android02.musculactionX.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculaction.data.entity.views.ICardsCollection
import ca.philrousse.android02.musculactionX.adapter.CardsCollectionsAdapter
import ca.philrousse.android02.musculactionX.viewModel.ListViewModel
import ca.philrousse.android02.musculactionX.databinding.FragmentHeaderAndListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class ExercisesListFragment : Fragment() {

    private var _binding: FragmentHeaderAndListBinding? = null
    private val vm: ListViewModel by viewModels()
    private val binding get() = _binding!!
    private var personalSubCat: Long? = null
    val args: ExercisesListFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeaderAndListBinding.inflate(inflater, container, false)
        binding.isEdit = false

        binding.fabAdd.setOnClickListener {
            addNew()
        }

        return binding.root
    }

    private fun addNew(){
        personalSubCat?.let {
            val action = ExercisesListFragmentDirections.actionAddExercices(subcategoryId = it)
            findNavController().navigate(action)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hookRecycleView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hookRecycleView(){
        val recyclerView: RecyclerView = binding.recyclerView
        val adapter = CardsCollectionsAdapter({ addNew() }){ clickedCard ->
            clickedCard.id?.let { cardId ->
                val action = ExercisesListFragmentDirections.actionExerciseDetail(cardId, clickedCard.name)
                findNavController().navigate(action)
            }
        }
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.exercisesByCategory(args.categoryId).collect { categoryExercisesCollections ->
                    binding.data = categoryExercisesCollections
                    val subcategories = categoryExercisesCollections.child

                    personalSubCat = subcategories.firstOrNull {
                        it.subcategory.isUserGenerated
                    }?.id

                    if (subcategories.isNotEmpty()) {
                        adapter.submitList(subcategories as List<ICardsCollection>)
                    } else {
                        adapter.submitList(null)
                    }
                }
            }
        }
    }
}