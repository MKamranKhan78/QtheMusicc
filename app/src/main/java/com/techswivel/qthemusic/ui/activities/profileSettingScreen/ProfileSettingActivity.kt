package com.techswivel.qthemusic.ui.activities.profileSettingScreen

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.databinding.ActivityProfileSettingBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.otpVerificationFragment.OtpVerification
import com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment.ProfileUpdatingFragment
import com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment.ProfileUpdatingFragmentImpl
import com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_setting_fragment.ProfileSettingFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils


class ProfileSettingActivity : BaseActivity(), ProfileSettingActivityImpl {

    private lateinit var mBinding: ActivityProfileSettingBinding
    private var mFragment: Fragment? = null
    private lateinit var mFragmentt: Fragment
    private lateinit var viewModel: ProfileSettingViewModel
    private lateinit var mAuthActivityViewModel: AuthNetworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileSettingBinding.inflate(layoutInflater)
        initViewModel()
        setContentView(mBinding.root)
        setToolBar()
        getBundleData()
        setObserver()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::mFragmentt.isInitialized) {
            mFragmentt.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onBackPressed() {

        if (getEntryCount() == 3) {
            mBinding.activityToolbar.toolbarTitle.text = getString(R.string.profile_settings)

        }
        if (getEntryCount() == 2) {
            mBinding.activityToolbar.toolbarTitle.text = getString(R.string.settings)

        }
        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (getEntryCount() == 3) {
            mBinding.activityToolbar.toolbarTitle.text = getString(R.string.profile_settings)

        }
        if (getEntryCount() == 2) {
            mBinding.activityToolbar.toolbarTitle.text = getString(R.string.settings)

        }

        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
        return super.onSupportNavigateUp()
    }

    override fun replaceCurrentFragment(fragment: Fragment) {
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.profile_settings)
        mFragmentt = fragment
        replaceFragment(mBinding.mainContainer.id, fragment)
    }

    override fun openAuthActivityWithPhoneNo(phoneNumber: String?, otpType: OtpType) {
        mBinding.activityToolbar.toolbarTitle.text = ""
        viewModel.phone = phoneNumber
        val bundle = Bundle()
        bundle.putString(CommonKeys.KEY_PHONE_NUMBER, phoneNumber)
        bundle.putSerializable(CommonKeys.OTP_TYPE, otpType)
        openFragment(OtpVerification.newInstance(bundle))

    }

    override fun verifyOtpRequest(authRequestBuilder: AuthRequestModel) {
        mAuthActivityViewModel.verifyOtpResponse(authRequestBuilder)
    }


    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }

    override fun openProfileSettingFragmentWithPnone(phoneNumber: String?) {
    }

    override fun openProfileSettingFragmentWithName(authModel: AuthModel?) {
    }

    override fun openProfileSettingFragmentWithAddress(authModel: AuthModel?) {
    }

    override fun openProfileSettingFragmentWithGender(authModel: AuthModel?) {
    }

    private fun setObserver() {
        mAuthActivityViewModel.otpVerificationResponse.observe(
            this,
            Observer { api_response ->
                when (api_response.status) {
                    NetworkStatus.LOADING -> {
                        showProgressBar()
                    }
                    NetworkStatus.SUCCESS -> {
                        hideProgressBar()
                        openProfileUpdatingFragment(ProfileUpdatingFragment())
                        mBinding.activityToolbar.toolbarTitle.text =
                            getString(R.string.profile_settings)
                        (mFragmentt as ProfileUpdatingFragmentImpl).openUpdatingFragment(viewModel.phone)
                        Toast.makeText(
                            this,
                            getString(R.string.phone_number_added),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    NetworkStatus.EXPIRE -> {
                        hideProgressBar()
                        val error = api_response.error as ErrorResponse
                        DialogUtils.errorAlert(
                            this,
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                    NetworkStatus.ERROR -> {
                        hideProgressBar()
                        val error = api_response.error as ErrorResponse
                        DialogUtils.errorAlert(
                            this,
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                }
            })
    }

    private fun openSettingFragment() {
        popUpAllFragmentIncludeThis(ProfileSettingFragment::class.java.name)
        openFragment(ProfileSettingFragment.newInstance())
    }

    private fun openUpdatingFragment() {
        var bundle = Bundle()
        bundle.putString(CommonKeys.KEY_PHONE_NUMBER, viewModel.phone)
        viewModel.fragment = ProfileUpdatingFragment.newInstance()
        openFragment(ProfileUpdatingFragment.newInstance(bundle))
    }


    private fun openFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { fragmentInstance ->
            fragmentInstance?.let { fragmentToBeReplaced ->
                replaceFragment(mBinding.mainContainer.id, fragmentToBeReplaced)
            }
        }
    }

    private fun openProfileUpdatingFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { fragmentInstance ->
            fragmentInstance?.let { fragmentToBeReplaced ->
                replaceFragmentWithoutAddingToBackStack(
                    mBinding.mainContainer.id,
                    fragmentToBeReplaced
                )
            }
        }
    }


    private fun setToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar, "", false, true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.settings)
    }


    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(ProfileSettingViewModel::class.java)
        mAuthActivityViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
    }

    private fun getBundleData() {
        val bundle = intent.extras
        var phone: String? = null
        phone = bundle?.getString(CommonKeys.KEY_PHONE_NUMBER)
        viewModel.phone = phone
        if (phone != null) {
            openUpdatingFragment()
        } else {
            openSettingFragment()
        }

    }
}