package ca.philrousse.android02.musculactionX.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculaction.data.entity.views.ExerciseView
import ca.philrousse.android02.musculaction.data.entity.views.ICard
import ca.philrousse.android02.musculactionX.R
import ca.philrousse.android02.musculactionX.adapter.CardsDetailAdapter
import ca.philrousse.android02.musculactionX.databinding.FragmentHeaderAndListBinding
import ca.philrousse.android02.musculactionX.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ExerciseDetailsFragment : Fragment() {

    private var _binding: FragmentHeaderAndListBinding? = null
    private val vm: ListViewModel by viewModels()
    private val arg: ExerciseDetailsFragmentArgs by navArgs()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeaderAndListBinding.inflate(inflater, container, false)
        binding.isEdit = true


        setHasOptionsMenu(true)
        return binding.root
    }

    private fun edit(){
        val action = ExerciseDetailsFragmentDirections.actionEditDetail(exerciseId = arg.exerciseId)
        findNavController().navigate(action)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exercise, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_menu_edit -> {
                edit()
                true
            }
            R.id.action_menu_delete ->{
                deleteExercise()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteExercise(){
        vm.deleteExercise((binding.data as ExerciseView).exercise){}
        findNavController().navigateUp()


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
        val adapter = CardsDetailAdapter()

        recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.exercisesDetail(arg.exerciseId).collect {
                    binding.data = it
                    (activity as AppCompatActivity?)!!.supportActionBar!!.title = it.name
                    val sections = it.child
//                    binding.title = it.name
//                    binding.description = it.description
//                    context?.let { ctx ->
//                        it.image?.getResourceId(ctx)?.also { imgResource ->
//                            binding.image = imgResource
//                        }
//                    }
                    if (sections.isNotEmpty()) {
                        adapter.submitList(sections as List<ICard>)
                    } else {
                        adapter.submitList(null)
                    }
                }
            }
        }
    }
}