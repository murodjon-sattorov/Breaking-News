package uz.murodjon_sattorov.breaking_news.view.fragments.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.adapters.SavedAdapter
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.databinding.FragmentSavedBinding
import uz.murodjon_sattorov.breaking_news.view.activities.MainActivity

@AndroidEntryPoint
class SavedFragment : Fragment(), SavedAdapter.SetOnNewsClickListener {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private var mRootView: View? = null
    private var mIsFirstLoad = false

    private var adapter = SavedAdapter()

    private val viewModel: SavedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentSavedBinding.inflate(layoutInflater)

        adapter.setOnNewsClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = _binding?.root
            mIsFirstLoad = true
        } else {
            mIsFirstLoad = false
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        if (mIsFirstLoad) {
            setListeners()
            setObservers()
        }
    }

    override fun onStart() {
        super.onStart()
        openFragment(requireActivity())
    }

    //this is method for at the settings fragment saved item clicked
    private fun openFragment(context: FragmentActivity) {
        if (context is MainActivity) {
            context.mainBinding.bottomBar.itemActiveIndex = 3
        }
    }

    private fun setListeners() {
        binding.savedPostsList.layoutManager = LinearLayoutManager(requireContext())
        binding.savedPostsList.adapter = adapter
    }

    private fun setObservers() {
        viewModel.readAllSavedPostNewsData.observe(viewLifecycleOwner) {
            adapter.addData(it)
        }
    }

    override fun onItemClick(article: Article, imageView: ImageView, position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("news_item_data", article)

        val extras = FragmentNavigatorExtras(
            imageView to "news_image_$position"
        )

        findNavController().navigate(
            R.id.action_savedFragment_to_fullNewsInfoFragment,
            bundle,
            null,
            extras
        )
    }

}