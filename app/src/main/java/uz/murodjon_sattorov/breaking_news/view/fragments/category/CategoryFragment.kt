package uz.murodjon_sattorov.breaking_news.view.fragments.category

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.adapters.CategoriesCompanyAdapter
import uz.murodjon_sattorov.breaking_news.core.adapters.CategoryNameAdapter
import uz.murodjon_sattorov.breaking_news.core.models.Model
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.utils.NetworkUtil
import uz.murodjon_sattorov.breaking_news.databinding.FragmentCategoryBinding

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private var nameAdapter = CategoryNameAdapter()
    private var data = ArrayList<Model>()

    private val viewModel: CategoryViewModel by viewModels()

    private var companyAdapter = CategoriesCompanyAdapter()
    var sources: Source? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentCategoryBinding.inflate(layoutInflater)

        loadNameData()

        nameItemClicked()

        loadCompanyData()

        onItemClick()

        searchSources()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        if (NetworkUtil.hasInternetConnection(requireContext())) {
            viewModel.errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            viewModel.getCount.observe(viewLifecycleOwner) {
                if (it!! > 0) {
                    getRoomData()
                } else {
                    viewModel.loadCompanies.observe(viewLifecycleOwner) { addData ->
                        viewModel.addFollowData(addData!!.sources)
                        getRoomData()
                    }
                }
            }
        } else {
            getRoomData()
        }
    }

    private fun getRoomData() {
        viewModel.readAllFollowsData.observe(viewLifecycleOwner) { roomData ->
            companyAdapter.clearData()
            companyAdapter.addData(roomData)
        }
    }

    private fun setListeners() {
        binding.loadCategoryName.layoutManager = LinearLayoutManager(requireContext())
        binding.loadCategoryItems.layoutManager = LinearLayoutManager(requireContext())
        binding.loadCategoryName.adapter = nameAdapter
        binding.loadCategoryItems.adapter = companyAdapter
    }

    private fun loadNameData() {
        nameAdapter.clearData()
        data.add(Model(0, "All"))
        data.add(Model(1, "Business"))
        data.add(Model(2, "Entertainment"))
        data.add(Model(3, "General"))
        data.add(Model(4, "Health"))
        data.add(Model(5, "Science"))
        data.add(Model(6, "Sports"))
        data.add(Model(7, "Technology"))
        nameAdapter.addData(data)
    }

    private fun loadCompanyData() {
        viewModel.getCount()
        viewModel.getLoadCompanies("")
    }

    private fun getSelectedData() {
        viewModel.getSelectedDate.observe(viewLifecycleOwner) {
            companyAdapter.clearData()
            companyAdapter.addData(it!!)
        }
    }

    private fun nameItemClicked() {
        nameAdapter.setOnCategoryNameItemClickListener(object :
            CategoryNameAdapter.CategoryNameItemClick {
            override fun onItemNameClick(model: Model) {
                getSelectedData()
                when (model.id) {
                    0 -> getRoomData()
                    1 -> viewModel.getSelectedFollow(getString(R.string.business))
                    2 -> viewModel.getSelectedFollow(getString(R.string.entertainment))
                    3 -> viewModel.getSelectedFollow(getString(R.string.general))
                    4 -> viewModel.getSelectedFollow(getString(R.string.health))
                    5 -> viewModel.getSelectedFollow(getString(R.string.science))
                    6 -> viewModel.getSelectedFollow(getString(R.string.sports))
                    7 -> viewModel.getSelectedFollow(getString(R.string.technology))

                }
            }
        })
    }

    private fun onItemClick() {
        companyAdapter.setCompanyItemClickListener(object :
            CategoriesCompanyAdapter.SetCompanyItemClickListener {
            override fun onItemClick(source: Source) {
                val bundle = Bundle()
                bundle.putSerializable("source_data", source)

                findNavController().navigate(R.id.action_categoryFragment_to_aboutFragment, bundle)
            }

            override fun onItemFollowClick(source: Source) {
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
                }
                viewModel.updateFollowData(sources!!)

            }

        })
    }

    private fun searchSources() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("TTT", "onTextChanged: $s")
                viewModel.getSearchData(s.toString())
                viewModel.getSearchData.observe(viewLifecycleOwner) {
                    companyAdapter.clearData()
                    companyAdapter.addData(it!!)
                    Log.d("TTT", "onTextChanged: $it")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

}