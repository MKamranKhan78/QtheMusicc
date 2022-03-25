package com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.databinding.FragmentProfileUpdatingBinding
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.addAddressDialogFragment.AddAddressDialogFragment
import com.techswivel.qthemusic.ui.fragments.addGenderDialogFragment.AddGenderDialogFragment
import com.techswivel.qthemusic.ui.fragments.addNameDialogFragment.AddNameDialogFragment
import com.techswivel.qthemusic.ui.fragments.addPhoneNumberDialogFragment.AddPhoneNumberDialogFragment
import java.util.*


class ProfileUpdatingFragment : BaseFragment() {

    private lateinit var mBinding: FragmentProfileUpdatingBinding
    private lateinit var viewModel: ProfileUpdatingViewModel
    private lateinit var netWorkViewModel: AuthNetworkViewModel

    val mcurrentTime = Calendar.getInstance()
    val year = mcurrentTime.get(Calendar.YEAR)
    val month = mcurrentTime.get(Calendar.MONTH)
    val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileUpdatingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolBar()
        initViewModel()
        changeStatusBarColor()
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        bindViewModelWithView()
        clickListeners()

    }

    private fun clickListeners() {
        mBinding.textviewChangeNameID.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddNameDialogFragment.newInstance()
            dialogFragment.show(fragmentTransaction, TAG)

        }

        mBinding.addPhoneNumberTextviewID.setOnClickListener {
            mBinding.backgroundBlurView.visibility = View.VISIBLE
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddPhoneNumberDialogFragment.newInstance()
            dialogFragment.show(fragmentTransaction, TAG)

        }

        mBinding.textviewChangeGenderID.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddGenderDialogFragment.newInstance()
            dialogFragment.show(fragmentTransaction, TAG)

        }

        mBinding.textviewChangeDobID.setOnClickListener {

            val datePicker = DatePickerDialog(
                requireContext(), R.style.MyDatePickerStyle,
                { view, year, month, dayOfMonth ->
                    mBinding.tvDateOfBirth.setText(
                        String.format(
                            "%d / %d / %d",
                            dayOfMonth,
                            month + 1,
                            year
                        )
                    )
                },
                year,
                month,
                day
            )

            openDatePicker(datePicker)

        }

        mBinding.textviewChangeAddressID.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddAddressDialogFragment.newInstance()
            dialogFragment.show(fragmentTransaction, TAG)
        }
    }

    @SuppressLint("NewApi")
    private fun openDatePicker(datePicker: DatePickerDialog) {
        datePicker.show()
        // its not changing color by xml style so this is used to change ok and cancel button color.
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(QTheMusicApplication.getContext().getColor(R.color.color_black))
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(QTheMusicApplication.getContext().getColor(R.color.color_black))
        // for disabling the past date
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusBarColor() {
        val window = requireActivity().window
        window.statusBarColor =
            ContextCompat.getColor(QTheMusicApplication.getContext(), R.color.color_black)
    }


    private fun setToolBar() {
        setUpActionBar(
            mBinding.fragmentToolbar.toolbar, "", true
        )
        mBinding.fragmentToolbar.toolbarTitle.text = getString(R.string.profileSetting)
    }

    private fun bindViewModelWithView() {
        mBinding.viewModel = viewModel
        mBinding.executePendingBindings()
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(ProfileUpdatingViewModel::class.java)

        netWorkViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
    }

}