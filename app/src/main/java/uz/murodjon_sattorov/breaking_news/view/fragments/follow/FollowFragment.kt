package uz.murodjon_sattorov.breaking_news.view.fragments.follow

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.adapters.FollowAdapter
import uz.murodjon_sattorov.breaking_news.core.adapters.IsFollowsAdapter
import uz.murodjon_sattorov.breaking_news.core.app.Logger
import uz.murodjon_sattorov.breaking_news.core.models.company.CompanyCategoryResponse
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.follow.FollowModel
import uz.murodjon_sattorov.breaking_news.core.utils.NetworkUtil
import uz.murodjon_sattorov.breaking_news.databinding.ActivityMainBinding
import uz.murodjon_sattorov.breaking_news.databinding.FragmentFollowBinding
import uz.murodjon_sattorov.breaking_news.view.activities.MainActivity

@AndroidEntryPoint
class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private var mRootView: View? = null
    private var mIsFirstLoad = false

    private var followsAdapter = IsFollowsAdapter()
    private var followNewsAdapter = FollowAdapter()

    private val viewModel: FollowViewModel by viewModels()

    var sources: Source? = null

    private lateinit var companyCategoryResponse: CompanyCategoryResponse

    private var domains = ""

    var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentFollowBinding.inflate(layoutInflater)

        loadData()
        onItemClick()

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

        if (mIsFirstLoad){
            setListeners()
            setObservers()
            moreViewClicked(requireActivity())
        }
    }

    private fun setListeners() {
        binding.recommendedList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.loadFollowingAndItsNewsList.layoutManager = LinearLayoutManager(requireContext())
        binding.recommendedList.adapter = followsAdapter
        binding.loadFollowingAndItsNewsList.adapter = followNewsAdapter
    }

    private fun setObservers() {
        viewModel.errorMessages.observe(viewLifecycleOwner) {
            Logger.log("Follows data -> ${it!!}")
        }
        viewModel.getFollowsCount.observe(viewLifecycleOwner) { followingData ->
            if (followingData!! > 0) {
                binding.textView2.visibility = View.GONE
                binding.recommendedItemMore.visibility = View.GONE
                binding.recommendedList.visibility = View.GONE
                binding.noData.visibility = View.GONE
                binding.shimmerView.visibility = View.VISIBLE
                binding.loadFollowingAndItsNewsList.visibility = View.VISIBLE
                viewModel.readAllFollowingData.observe(viewLifecycleOwner) {
                    for (i in it.indices) {
                        if (it[i].url.substring(it[i].url.length - 1, it[i].url.length) == "/") {
                            val data: String = it[i].url.substring(0, it[i].url.length - 1)
                            getFollowDomain(data)
                        } else {
                            val data: String = it[i].url
                            getFollowDomain(data)
                        }
                    }
                    companyCategoryResponse = CompanyCategoryResponse(it, "ok")
                    followNewsAdapter.addData(companyCategoryResponse)
                    viewModel.loadFollowsNews(requireContext(), domains)
                }
                viewModel.loadFollowsNews.observe(viewLifecycleOwner) {
                    binding.shimmerView.visibility = View.GONE
                    followNewsAdapter.addData(it!!)
                    if (it.articles.isEmpty()) {
                        binding.noData.visibility = View.VISIBLE
                        binding.noDataText.text = requireContext().getString(R.string.no_news)
                    }
                }

            } else {
                viewModel.getCount.observe(viewLifecycleOwner) { followData ->
                    binding.loadFollowingAndItsNewsList.visibility = View.GONE
                    binding.shimmerView.visibility = View.GONE
                    binding.textView2.visibility = View.VISIBLE
                    binding.recommendedItemMore.visibility = View.VISIBLE
                    binding.recommendedList.visibility = View.VISIBLE
                    binding.noData.visibility = View.VISIBLE
                    if (followData!! > 0) {
                        getRoomData()
                    } else {
                        if (NetworkUtil.hasInternetConnection(requireContext())) {
                            viewModel.loadCompanies.observe(viewLifecycleOwner) { addData ->
                                viewModel.addFollowData(addData!!.sources)
                                getRoomData()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getFollowDomain(data: String) {
        if (data.substring(0, 5) == "https" && data.substring(8, 11) == "www") {
            domains += data.substring(12) + ","
        } else if (data.substring(0, 4) == "http" && data.substring(7, 10) == "www") {
            domains += data.substring(11) + ","
        } else if (data.substring(0, 5) == "https") {
            domains += data.substring(8) + ","
        } else if (data.substring(0, 4) == "http") {
            domains += data.substring(7) + ","
        }
    }

    private fun getRoomData() {
        viewModel.readAllFollowsData.observe(viewLifecycleOwner) { roomData ->
            followsAdapter.clearData()
            followsAdapter.addData(roomData)
        }
    }

    private fun loadData() {
        viewModel.getCount()
        viewModel.getFollowsCount()
        viewModel.getLoadCompanies("")
    }

    private fun onItemClick() {
        followsAdapter.setOnClickListener(object : IsFollowsAdapter.SetOnClickListener {
            override fun onItemClick(source: Source) {

            }

            override fun onFollowItemClick(source: Source) {
                if (source.isChecked) {
                    sources = Source(
                        source.ids,
                        source.category,
                        source.country,
                        source.description,
                        source.id,
                        source.language,
                        source.name,
                        source.url,
                        false
                    )
                    isChecked = false
                } else {
                    sources = Source(
                        source.ids,
                        source.category,
                        source.country,
                        source.description,
                        source.id,
                        source.language,
                        source.name,
                        source.url,
                        true
                    )
                    isChecked = true
                }
                viewModel.updateFollowData(sources!!)
                if (isChecked) {
                    binding.restart.setOnClickListener {
                        restart()
                    }
                }
            }

        })
    }

    private fun restart() {
        val navController = findNavController()
        navController.run {
            popBackStack()
            navigate(R.id.followFragment)
        }
    }

    private fun moreViewClicked(context: FragmentActivity) {
        binding.recommendedItemMore.setOnClickListener {
            if (context is MainActivity) {
                context.navController.navigate(R.id.categoryFragment)
                context.mainBinding.bottomBar.itemActiveIndex = 1
            }
        }
    }


}