package com.techswivel.qthemusic.ui.fragments.signInFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.enums.*
import com.techswivel.qthemusic.databinding.FragmentSignInBinding
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.SignInNetworkViewModel
import com.techswivel.qthemusic.ui.activities.mainActivity.MainActivity
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.utils.*
import java.util.*


class SignInFragment : BaseFragment() {
    private  val TAG = "SignInFragment"
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var sigInNetworkViewModel: SignInNetworkViewModel
    private lateinit var signInBinding: FragmentSignInBinding
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSinInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager
    private lateinit var twoWayBindingObj: BindingValidationClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signInBinding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        return signInBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initialization()
        clickListeners()
        setViewModelObserver()
    }
    override fun onResume() {
        super.onResume()
        signInViewModel.showAnimation =
            PrefUtils.getBoolean(requireContext(), CommonKeys.SIGNIN_BTN_ANIMATION)
        val animationSignInBtn =
            AnimationUtils.loadAnimation(requireContext(), R.anim.top_to_bottom_sign_in_btn)
        if (signInViewModel.showAnimation) {
            signInBinding.btnSignIn.animation = animationSignInBtn
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == Constants.GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }
    private fun initialization() {
        twoWayBindingObj = BindingValidationClass()
        signInBinding.obj = twoWayBindingObj
        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()
        Glide.with(requireContext()).load(R.drawable.laura_music)
            .transform(BlurImageView(requireContext())).into(signInBinding.ivSigninBg)
        signInBinding.etPasLayout.passwordVisibilityToggleRequested(false)

    }

    private fun setViewModelObserver() {
        sigInNetworkViewModel.signinUserResponse.observe(
            viewLifecycleOwner,
            Observer { signInResponse ->

                when (signInResponse.status) {

                    NetworkStatus.LOADING -> {
                        signInBinding.btnSignIn.visibility = View.INVISIBLE
                        signInBinding.pb.visibility = View.VISIBLE
                    }
                    NetworkStatus.SUCCESS -> {
                        Log.v(TAG, "success")
                        val data = signInResponse.t as ResponseModel
                        Log.d(TAG, "data${data.data?.authModel}")
                        signInBinding.btnSignIn.visibility = View.VISIBLE
                        signInBinding.pb.visibility = View.INVISIBLE
                        activity.let {
                            val intent = Intent(it, MainActivity::class.java)
                            it?.startActivity(intent)
                            it?.finish()
                        }
                    }
                    NetworkStatus.ERROR -> {
                        val error = signInResponse.error as ErrorResponce
                        DialogUtils.errorAlert(
                            requireContext(),
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                    NetworkStatus.EXPIRE -> {
                        Log.v(TAG, "EXPIRE")
                    }
                    NetworkStatus.COMPLETED -> {
                        Log.v("Network_status", "completed")
                    }
                }
            })


        sigInNetworkViewModel.signinUserResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                }
                NetworkStatus.SUCCESS -> {
                    val data = it.t as GoogleResponseModel
                }
                NetworkStatus.EXPIRE -> {

                }
                NetworkStatus.ERROR -> {
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })
    }

    private fun clickListeners() {
        signInBinding.btnSignIn.setOnClickListener {
            if (
                !signInBinding.etLoginEmail.text.isNullOrEmpty() &&
                !signInBinding.etLoginPassword.text.isNullOrEmpty() &&
                twoWayBindingObj.isEmailTextValid.get() == true &&
                twoWayBindingObj.isPasswordTextValid.get() == true
            ) {
                createUserAndCallApi(
                    signInBinding.etLoginEmail.toString(),
                    signInBinding.etLoginPassword.text.toString(),
                    null,
                    LoginType.SIMPLE.name,
                    "kljsdjklsdfkljsdf",
                    "wasi_dev",
                    null
                )
            }
        }

        signInBinding.tvForgotPassword.setOnClickListener {
            PrefUtils.setBoolean(requireContext(), CommonKeys.SIGNIN_BTN_ANIMATION, true)
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.APP_FLOW, OtpType.FORGET_PASSWORD)
            val fortgotPasword = ForgotPassword()
            fortgotPasword.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.auth_container, fortgotPasword)
                .addToBackStack(TAG)
            transaction.commit()
        }

        signInBinding.signSocialPortion.ivGoogleId.setOnClickListener {
            signInGoogle()
        }

        signInBinding.signSocialPortion.ivFbId.setOnClickListener {
            signInFacebook()
        }

    }

    private fun createUserAndCallApi(
        email: String?,
        password: String?,
        accessToken: String?,
        loginType: String?,
        fcmToken: String?,
        deviceIdentifier: String?,
        socialSites: String?
    ) {

        val authModelBilder = AuthRequestBuilder()
        authModelBilder.email = email
        authModelBilder.password = password
        authModelBilder.accessToken = accessToken
        authModelBilder.loginType = loginType
        authModelBilder.socialSite = socialSites
        authModelBilder.fcmToken = fcmToken
        authModelBilder.deviceIdentifier = deviceIdentifier
        val authModel = AuthRequestBuilder.builder(authModelBilder)
        sigInNetworkViewModel.userLogin(authModel)
    }

    private fun signInFacebook() {
        loginManager.logInWithReadPermissions(
            this,
            Arrays.asList(
                getString(R.string.get_email_fb),
            )
        )

        loginManager.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result?.accessToken?.token
                    createUserAndCallApi(
                        null,
                        null,
                        accessToken,
                        LoginType.SOCIAL.name,
                        null,
                        "wasi_dev",
                        SocialSites.FACEBOOK.name
                    )
                    Log.d(TAG, "$accessToken")
                }

                override fun onCancel() {
                }

                override fun onError(exception: FacebookException) {
                    Log.d(TAG, "fb exception ${exception.message}")
                }
            })
    }

    private fun signInGoogle() {

        googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id_staging))
                .requestEmail().build()
        googleSinInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
        val signInIntent = googleSinInClient.signInIntent
        startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)
    }


    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val name = account.displayName
            val email = account.email
            val token = account.idToken
            val authCode = account.serverAuthCode
            if (token != null) {
                //  googleSignViewModel.getGoogleToken(token)
                createUserAndCallApi(
                    null,
                    null,
                    "ksdklfjsdklfjsdklfjsdklfjsdklf",
                    LoginType.SOCIAL.name,
                    "kfjsdlfjsdklfjsdklfds",
                    "wasi_dev", SocialSites.GMAIL.name
                )
            }
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun initViewModel() {
        sigInNetworkViewModel = ViewModelProvider(this).get(SignInNetworkViewModel::class.java)
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

    }
}