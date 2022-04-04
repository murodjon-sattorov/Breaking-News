package uz.murodjon_sattorov.breaking_news.view.fragments.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.helpers.hideBottom
import uz.murodjon_sattorov.breaking_news.core.helpers.showBottom
import uz.murodjon_sattorov.breaking_news.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    private var aboutVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentSettingsBinding.inflate(layoutInflater)

        loadData()
        otherItemsClick()
        closeFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        saveSwitchChanges()
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
            findNavController().popBackStack()
        }
    }

    private fun otherItemsClick() {
        binding.saved.setOnClickListener {
            findNavController().navigate(R.id.savedFragment)
        }
        binding.supportUs.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${requireContext().packageName}")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
                    )
                )
            }
        }
        binding.openEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:sattorovmurodjon43@gmail.com")
                    .buildUpon()
                    .appendQueryParameter("subject", "Enter your subject")
                    .appendQueryParameter("body", Build.MANUFACTURER + "\n" + Build.MODEL)
                    .build()
            }
            startActivity(Intent.createChooser(emailIntent, "Send feedback"))
        }
        binding.about.setOnClickListener {
            if (aboutVisible) {
                aboutVisible = false
                binding.fullAbout.visibility = View.GONE
            } else {
                aboutVisible = true
                binding.fullAbout.visibility = View.VISIBLE
            }
        }
    }

    private fun loadData() {
        viewModel.getSavedNightModeData()
        viewModel.getSavedNotificationData()
    }

    private fun setObservers() {
        viewModel.getSavedNightModeData.observe(viewLifecycleOwner) {
            when (it) {
                "NIGHT" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.nightMode.isChecked = true
                }
                "DAY" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.nightMode.isChecked = false
                }
            }
        }
        viewModel.getSavedNotificationData.observe(viewLifecycleOwner) {
            when (it) {
                "PUSH_YES" -> {
                    binding.pushNotification.isChecked = true
                }
                "PUSH_NO" -> {
                    binding.pushNotification.isChecked = false
                }
            }
        }
    }


    private fun saveSwitchChanges() {
        binding.pushNotification.setOnCheckedChangeListener { _, notificationChecked ->
            if (notificationChecked) {
                viewModel.setSavedNotificationData("PUSH_YES")
            } else {
                viewModel.setSavedNotificationData("PUSH_NO")
            }
            viewModel.getSavedNotificationData()
        }
        binding.nightMode.setOnCheckedChangeListener { _, nightModeChecked ->
            if (nightModeChecked) {
                viewModel.setSavedNightModeData("NIGHT")
            } else {
                viewModel.setSavedNightModeData("DAY")
            }
            viewModel.getSavedNightModeData()
        }
    }
}