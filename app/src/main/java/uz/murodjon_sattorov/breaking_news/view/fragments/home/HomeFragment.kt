package uz.murodjon_sattorov.breaking_news.view.fragments.home

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.SmoothBottomBar
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.adapters.HomeAdapter
import uz.murodjon_sattorov.breaking_news.core.app.Logger
import uz.murodjon_sattorov.breaking_news.core.helpers.closeBottomBar
import uz.murodjon_sattorov.breaking_news.core.helpers.hideBottom
import uz.murodjon_sattorov.breaking_news.core.helpers.showBottom
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.core.utils.NetworkUtil
import uz.murodjon_sattorov.breaking_news.databinding.FragmentHomeBinding
import uz.murodjon_sattorov.breaking_news.view.activities.MainActivity

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeAdapter.SetOnNewsClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mRootView: View? = null
    private var mIsFirstLoad = false

    private val viewModel: HomeViewModel by viewModels()

    private var adapter = HomeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        clickSearchAndSettingsIcons()

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
            loadData()
        }

    }

    private fun setListeners() {
        binding.topHeadlinesList.layoutManager = LinearLayoutManager(requireContext())
        binding.topHeadlinesList.adapter = adapter
    }

    private fun setObservers() {
        if (NetworkUtil.hasInternetConnection(requireContext())) {
            binding.noInternet.visibility = View.GONE
            viewModel.errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                Logger.log("isLoading $isLoading")
                if (isLoading!!) {
                    binding.shimmerView.visibility = View.VISIBLE
                    binding.topHeadlinesList.visibility = View.GONE
                } else {
                    binding.topHeadlinesList.visibility = View.VISIBLE
                    binding.shimmerView.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                }
            }
            viewModel.loadTopHeadlines.observe(viewLifecycleOwner) {
                val data = it
                adapter.addData(data!!.articles)
            }
        } else {
            binding.noInternet.visibility = View.VISIBLE
        }
    }

    private fun loadData() {
        viewModel.getTopHeadlinesNews(this.getString(R.string.country))
    }

    private fun clickSearchAndSettingsIcons() {
        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }

    override fun onItemClick(article: Article, imageView: ImageView, position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("news_item_data", article)

        val extras = FragmentNavigatorExtras(
            imageView to "news_image_$position"
        )

        findNavController().navigate(
            R.id.action_homeFragment_to_fullNewsInfoFragment,
            bundle,
            null,
            extras
        )
    }

    override fun onNextPage() {
        loadData()
    }

    override fun onRefresh() {
        adapter.clearData()
        loadData()
    }

}