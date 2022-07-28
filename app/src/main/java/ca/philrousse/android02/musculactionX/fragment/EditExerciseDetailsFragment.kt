package ca.philrousse.android02.musculactionX.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculactionX.R
import ca.philrousse.android02.musculactionX.adapter.ExerciseDetailSectionEditAdapter
import ca.philrousse.android02.musculactionX.databinding.FragmentEditExerciseBinding
import ca.philrousse.android02.musculactionX.viewModel.EditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditExerciseDetailsFragment : Fragment() {

    private var _binding: FragmentEditExerciseBinding? = null
    private val vm: EditViewModel by viewModels()
    private val arg: EditExerciseDetailsFragmentArgs by navArgs()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditExerciseBinding.inflate(inflater, container, false)

        binding.newSection.setOnClickListener {
            vm.addSection()
        }
        setHasOptionsMenu(true)
        return binding.root
    }
    private fun save(){
        vm.commitChange ()
        findNavController().navigateUp()
    }

    private fun delete(){
        vm.deleteExercise()
        findNavController().navigateUp()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_exercise, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_menu_save -> {
                save()
                true
            }
            R.id.action_menu_delete ->{
                delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        val adapter = ExerciseDetailSectionEditAdapter{
            vm.removeSection(it)
        }

        recyclerView.adapter = adapter
        vm.hookViewModel(arg.exerciseId,arg.subcategoryId)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.editedExercise.collect { exerciseView ->
                    exerciseView?.let {
                        binding.data = it
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.sections.collect {
                    if (it.isNotEmpty()) {
                        adapter.submitList(it)
                    } else {
                        adapter.submitList(null)
                    }
                }
            }
        }

    }
}