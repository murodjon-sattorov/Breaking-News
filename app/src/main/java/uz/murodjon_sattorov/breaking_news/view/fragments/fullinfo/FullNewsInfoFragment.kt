package uz.murodjon_sattorov.breaking_news.view.fragments.fullinfo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.app.Logger
import uz.murodjon_sattorov.breaking_news.core.helpers.*
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.core.models.post.Source
import uz.murodjon_sattorov.breaking_news.core.models.saved.SavedArticles
import uz.murodjon_sattorov.breaking_news.core.utils.IMG_BASE_URL
import uz.murodjon_sattorov.breaking_news.core.utils.SMALL_IMG_SIZE
import uz.murodjon_sattorov.breaking_news.databinding.FragmentFullNewsInfoBinding

@AndroidEntryPoint
class FullNewsInfoFragment : Fragment() {

    private var _binding: FragmentFullNewsInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomBar: SmoothBottomBar
    private lateinit var lineView: RelativeLayout

    private val viewModel: FullNewsInfoModel by viewModels()

    private var article: Article? = null

    private var isSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300
        }

        _binding = FragmentFullNewsInfoBinding.inflate(layoutInflater)
        bottomBar = requireActivity().findViewById(R.id.bottomBar)
        lineView = requireActivity().findViewById(R.id.lineView)

        article = arguments?.getSerializable("news_item_data") as Article?

        loadData()

        closeFragment()

        Log.d("TTT", "bindData: 2 $article")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSavedPost()
        setViews()
        clickSavePostBtn()
    }

    private fun loadData() {
        article!!.url?.let { viewModel.saved(it) }
    }

    private fun loadSavedPost() {
        viewModel.isChecked.observe(viewLifecycleOwner) {
            isSaved = it!!
            if (isSaved) {
                binding.savePost.setBackgroundResource(R.drawable.ic_round_bookmark_added_24)
            } else {
                binding.savePost.setBackgroundResource(R.drawable.ic_round_bookmark_border_24)
            }
        }
    }

    private fun clickSavePostBtn() {
        val source = Source("", article!!.source.name)

        //Null data can also come in here so it has been checked for nullness
        val savedArticles = article?.id.let { id ->
            (if (article?.author != null) article!!.author else "")?.let { author ->
                (if (article?.content != null) article!!.content else "")?.let { content ->
                    (if (article?.description != null) article!!.description else "")?.let { description ->
                        (if (article?.publishedAt != null) article!!.publishedAt else "")?.let { publishedAt ->
                            (if (article?.title != null) article!!.title else "")?.let { title ->
                                (if (article?.url != null) article!!.url else "")?.let { url ->
                                    (if (article?.urlToImage != null) article!!.urlToImage else "")?.let { urlToImage ->
                                        SavedArticles(
                                            id!!,
                                            author,
                                            content,
                                            description,
                                            publishedAt,
                                            source,
                                            title,
                                            url,
                                            urlToImage
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.savePost.setOnClickListener {
            isSaved = if (isSaved) {
                article!!.url?.let { it1 -> viewModel.deleteFromBase(it1) }
                binding.savePost.setBackgroundResource(R.drawable.ic_round_bookmark_border_24)
                false
            } else {
                if (savedArticles != null) {
                    viewModel.saveToBase(savedArticles)
                }
                binding.savePost.setBackgroundResource(R.drawable.ic_round_bookmark_added_24)
                true
            }
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

    private fun closeFragment() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setViews() {

        binding.newsImage.transitionName = article!!.source.id

        binding.companyName.text = article!!.source.name
        binding.postTitle.text = article!!.title

        if (article?.content == null || article?.description == null)
            binding.postDescription.text = article?.description
        else binding.postDescription.text =
            Html.fromHtml(article?.description)
                .toString() + "\n\n" + Html.fromHtml(article?.content).toString()

        Glide.with(binding.companyImg.context)
            .load(IMG_BASE_URL + article!!.url?.let { getCompanyImgUrl(it) } + SMALL_IMG_SIZE)
            .placeholder(R.drawable.ic_loading_image_big)
            .into(binding.companyImg)
        binding.agoPostReleaseTime.text = article!!.publishedAt?.let { DateUtil.timeCalculate(it) }

        @SuppressLint("CheckResult")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Glide.with(binding.newsImage.context).load(article!!.urlToImage)
                .placeholder(dayAndNight(binding.newsImage.context)).into(binding.newsImage)
        } else {
            Glide.with(binding.newsImage.context).load(article!!.urlToImage)
                .placeholder(R.drawable.ic_loading_image_big)
        }
    }

    private fun shareUrl() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "${article?.url} \n\nDownload this app:\nhttps://play.google.com/store/apps/details?id=${requireContext().packageName}"
        )
        startActivity(intent)
    }

}