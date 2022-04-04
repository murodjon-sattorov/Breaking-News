package uz.murodjon_sattorov.breaking_news.view.fragments.about

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar
import timber.log.Timber
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.adapters.AboutAdapter
import uz.murodjon_sattorov.breaking_news.core.helpers.closeBottomBar
import uz.murodjon_sattorov.breaking_news.core.helpers.openBottomBar
import uz.murodjon_sattorov.breaking_news.core.models.about.AboutModel
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.databinding.FragmentAboutBinding

@AndroidEntryPoint
class AboutFragment : Fragment(), AboutAdapter.SetOnNewsClickListener {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private var mRootView: View? = null
    private var mIsFirstLoad = false

    private lateinit var bottomBar: SmoothBottomBar
    private lateinit var lineView: RelativeLayout

    private var adapter = AboutAdapter()

    private var addData = ArrayList<BaseModel>()

    private var source: Source? = null

    private val viewModel: AboutViewModel by viewModels()

    private var countAboutItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentAboutBinding.inflate(layoutInflater)
        bottomBar = requireActivity().findViewById(R.id.bottomBar)
        lineView = requireActivity().findViewById(R.id.lineView)

        source = arguments?.getSerializable("source_data") as Source?

        adapter.setOnNewsClickListener = this

        closeFragment()
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
            loadData()
            setObservers()
            setListeners()
        }

    }

    override fun onStart() {
        super.onStart()
        closeBottomBar(bottomBar, lineView)
    }

    override fun onStop() {
        super.onStop()
        openBottomBar(bottomBar, lineView)
    }

    private fun loadData() {
        countAboutItem++
        viewModel.loadSourceData(source!!.id)
    }

    private fun setListeners() {
        binding.aboutRv.layoutManager = LinearLayoutManager(requireContext())
        binding.aboutRv.adapter = adapter
    }

    private fun setObservers() {
        viewModel.errorMessages.observe(viewLifecycleOwner) {
            Timber.d("Error -> $it")
        }
        viewModel.loadSourcesData.observe(viewLifecycleOwner) {
            if (countAboutItem <= 1) {
                loadPostsSize(it!!.totalResults)
            }
            adapter.addData(it!!.articles)
        }
    }

    private fun loadPostsSize(totalResults: Int) {
        var size = ""
        size = if (totalResults > 1000) "${String.format("%.1f", (totalResults.toFloat() / 1000))}K"
        else totalResults.toString()
        addData.add(
            AboutModel(
                source!!.category,
                size,
                source!!.name,
                source!!.url,
                source!!.description
            )
        )
        adapter.addData(addData)
    }

    override fun onItemClick(article: Article, imageView: ImageView, position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("news_item_data", article)

        val extras = FragmentNavigatorExtras(
            imageView to "news_image_$position"
        )
        Log.d("TTT", "onItemClick: -news_image_$position")
        findNavController().navigate(
            R.id.action_aboutFragment_to_fullNewsInfoFragment,
            bundle,
            null,
            extras
        )
    }

    override fun onItemClickForShare(url: String) {
        shareUrl(url)
    }

    override fun onFollowItem(view: TextView) {
        viewModel.readAllFollowingData.observe(viewLifecycleOwner) {
            if (it!!.isNotEmpty()) {
                Timber.d("TTT $it")
                for (i in it.indices) {
                    if (it[i].name == source!!.name) {
                        view.setBackgroundResource(R.drawable.following_button_bg)
                        view.text = this.getString(R.string.following)
                        break
                    } else {
                        view.setBackgroundResource(R.drawable.follow_button_bg)
                        view.text = this.getString(R.string.follow)
                    }
                }
            } else {
                view.setBackgroundResource(R.drawable.follow_button_bg)
                view.text = this.getString(R.string.follow)
            }
        }
    }

    override fun onFollowItemClick(view: TextView) {
        if (source!!.isChecked) {
            view.setBackgroundResource(R.drawable.follow_button_bg)
            view.text = this.getString(R.string.follow)
            source!!.isChecked = false
            viewModel.updateFollowData(source!!)
        } else {
            view.setBackgroundResource(R.drawable.following_button_bg)
            view.text = this.getString(R.string.following)
            source!!.isChecked = true
            viewModel.updateFollowData(source!!)
        }
    }

    override fun onNextPage() {
        loadData()
    }

    private fun shareUrl(url: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "$url \n\nDownload this app:\nhttps://play.google.com/store/apps/details?id=${requireContext().packageName}"
        )
        startActivity(intent)
    }

    private fun closeFragment() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}