package ca.philrousse.android02.musculactionX.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculaction.data.entity.views.ICard
import ca.philrousse.android02.musculactionX.adapter.CardsAdapter
import ca.philrousse.android02.musculactionX.viewModel.ListViewModel
import ca.philrousse.android02.musculactionX.R
import ca.philrousse.android02.musculactionX.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "CategoriesListFragment"
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val vm: ListViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_phone -> {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${getString(R.string.phone_intent)}")
                }
                if (activity?.let { intent.resolveActivity(it.packageManager) } != null) {
                    startActivity(intent)
                }
                true
            }
            R.id.action_mail ->{

                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:") // only email apps should handle this

                intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_recipient))
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body))
                if (activity?.let { intent.resolveActivity(it.packageManager) } != null) {
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        val adapter = CardsAdapter {
            Log.d(TAG, "hookRecycleView: $it")
            it.id?.let { categoryId ->
                
                val action = CategoriesListFragmentDirections.actionDetailCategory(categoryId,it.name)
                findNavController().navigate(action)
            }

        }

        recyclerView.adapter = adapter

        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.categoryList.collect {
                    if (it.isNotEmpty()) {
                        adapter.submitList(it as List<ICard>)
                    } else {
                        adapter.submitList(null)
                    }
                }
//            }
        }
    }

}