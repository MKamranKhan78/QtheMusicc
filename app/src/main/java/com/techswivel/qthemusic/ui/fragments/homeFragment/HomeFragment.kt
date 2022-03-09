package com.techswivel.qthemusic.ui.fragments.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentHomeBinding
import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.DialogUtils
import kotlinx.coroutines.runBlocking

class HomeFragment : RecyclerViewBaseFragment(), RecyclerViewAdapter.CallBack, BaseInterface {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setObserver()
        viewModel.getRecommendedSongsDataFromServer(
            RecommendedSongsBodyModel(
                null, null,
                isListeningHistory = false,
                isRecommendedForYou = true,
                type = RecommendedSongsType.SONGS
            )
        )
    }

    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        return mAdapter
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_recommended_media
    }

    override fun onNoDataFound() {

    }

    override fun onViewClicked(view: View, data: Any?) {

    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setObserver() {
        viewModel.recommendedSongsResponse.observe(viewLifecycleOwner) { recommendedSongsDataResponse ->
            when (recommendedSongsDataResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val response = recommendedSongsDataResponse.t as ResponseModel

                    viewModel.recommendedSongsDataList.clear()
                    val list = response.data.recommendedSongsResponse

//                    if (list) {
//                        textNoData.visibility = View.GONE
//                        for (an: Any in list) {
//                            viewModel.routeList.add(an)
//                        }
//
//                    } else {
//                        textNoData.visibility = View.VISIBLE
//                    }
//                    if (::mAdapter.isInitialized)
//                        mAdapter.notifyDataSetChanged()

                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    recommendedSongsDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            recommendedSongsDataResponse.error.code.toString(),
                            recommendedSongsDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    DialogUtils.sessionExpireAlert(requireContext(),
                        object : DialogUtils.CallBack {
                            override fun onPositiveCallBack() {
                                runBlocking {
                                    DataStoreUtils.clearAllPreference()
                                }
//                                viewModel.deleteAllLocalData()
//                                ActivityUtils.startNewActivity(
//                                    requireActivity(),
//                                    RegistrationActivity::class.java,
//                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                )
                            }

                            override fun onNegativeCallBack() {
                            }
                        })
                }
                NetworkStatus.COMPLETED -> {
                    hideProgressBar()
                }
            }
        }
    }
}