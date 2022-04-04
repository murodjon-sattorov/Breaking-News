package uz.murodjon_sattorov.breaking_news.view.fragments.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.adapters.SearchAdapter
import uz.murodjon_sattorov.breaking_news.core.helpers.hideBottom
import uz.murodjon_sattorov.breaking_news.core.helpers.showBottom
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.databinding.FragmentSearchBinding

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.SetOnNewsClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var q = ""

    private var adapter = SearchAdapter()

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentSearchBinding.inflate(layoutInflater)

        clearText()
        closeFragment()

        adapter.setOnNewsClickListener = this

    }

    private fun loadData() {
        viewModel.loadSearchData(q)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.requestFocus()

        showKeyboard()
        binding.searchView.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchItems.visibility = View.GONE
                binding.shimmerView.visibility = View.VISIBLE
                hideKeyboardAndSearchPosts()
                adapter.clearData()
                setListeners()

                q = binding.searchView.text.toString()
                viewModel.page = 1
                loadData()
                lifecycleScope.launch {
                    delay(3000)
                    binding.shimmerView.visibility = View.GONE
                    binding.searchItems.visibility = View.VISIBLE
                    setObservers()
                }

                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onStart() {
        super.onStart()
        hideBottom(requireActivity())
    }

    override fun onStop() {
        super.onStop()
        showBottom(requireActivity())
    }

    private fun closeFragment() {
        binding.back.setOnClickListener {
            hideKeyboardAndSearchPosts()
            findNavController().popBackStack()
        }
    }

    private fun showKeyboard() {
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    private fun hideKeyboardAndSearchPosts() {
        binding.searchView.clearFocus()
        val inputMethod =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
    }

    private fun clearText() {
        binding.clearText.setOnClickListener {
            if (binding.searchView.text!!.isNotEmpty()) {
                binding.searchView.setText("")
                binding.searchView.requestFocus()
                showKeyboard()
            }
        }
    }

    private fun setListeners() {
        binding.searchItems.layoutManager = LinearLayoutManager(requireContext())
        binding.searchItems.adapter = adapter
    }

    private fun setObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it!!) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Timber.d("ErrorMessage $it")
        }
        viewModel.loadSearchNews.observe(viewLifecycleOwner) {
            Log.d("TTT", "setObservers: ")
            adapter.addData(it!!.articles)
        }
    }

    override fun onItemClick(article: Article, imageView: ImageView, position: Int) {

        hideKeyboardAndSearchPosts()

        val bundle = Bundle()
        bundle.putSerializable("news_item_data", article)

        val extras = FragmentNavigatorExtras(
            imageView to "news_image_$position"
        )

        findNavController().navigate(
            R.id.action_searchFragment_to_fullNewsInfoFragment,
            bundle,
            null,
            extras
        )
    }

    override fun onNextPage() {
        loadData()
    }

}