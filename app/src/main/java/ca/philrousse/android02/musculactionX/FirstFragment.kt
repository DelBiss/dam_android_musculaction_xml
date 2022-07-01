package ca.philrousse.android02.musculactionX

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculaction.data.entity.IImageCard

import ca.philrousse.android02.musculactionX.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val vm:CategoriesViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        hookRecycleView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hookRecycleView(){
        val recyclerView: RecyclerView = binding.recyclerView
        val adapter = CardsAdapter { categoryID ->
            val bundle = bundleOf("category_id" to categoryID)
            findNavController().navigate(R.id.action_show_exercices_from_category, bundle)
        }


        recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.categoryList.collect {
                    if (it.isNotEmpty()) {
                        @Suppress("UNCHECKED_CAST")
                        adapter.submitList(it as MutableList<IImageCard>)
                    } else {
                        adapter.submitList(null)
                    }
                }
            }
        }
    }
}