package com.techswivel.udeoglobe.Data.RemoteRepository.firebaseRespository


class FirebaseRepository : FirebaseRepositoryImp {

    /* fun firebaseLogin(pContext: Context, pActivity: Activity)
     {
         UdeoGlobeApplication.getFirebaseAuthen().signInWithCustomToken(pContext.resources.getString(
             R.string.friebaseCustomToken))
             .addOnCompleteListener(pActivity) {
                 if (it.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                     Log.e(">>>>>>", "signInWithCustomToken:success")

                 } else { // If sign in fails, display a message to the user.
                     Log.e(">>>>>>", "signInWithCustomToken:failure", it.exception)
                     Toast.makeText(
                         pContext, "Authentication failed.",
                         Toast.LENGTH_SHORT
                     ).show()
                 }
             }
     }*/

//    @SuppressLint("DefaultLocale")
//    override fun FirebaseLoginWithEmail(emailText: String, callBack: FirebaseCallBack, isInstaLogin: Boolean) {
//        val lowerEmail = emailText.toLowerCase()
//        val hashPassForFirebase = Hashing.HashValue(lowerEmail)
//        SeekerApplication.getFirebaseAuthen().signInWithEmailAndPassword(lowerEmail, hashPassForFirebase)
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        if (isInstaLogin) {
//                            callBack.onInstaLoginSuccess(it)
//                        } else {
//                            callBack.onSimpleLoginSuccess(it)
//                        }
//                    } else {
//                        if (isInstaLogin) {
//                            callBack.onInstaLoginFailure(it)
//                        } else {
//                            callBack.onSimpleLoginFailure(it)
//                        }
//                    }
//                }
//    }
//
//    override fun fbSignupFirebase(currentUser: AccessToken, callBack: FirebaseCallBack) {
//        val credential = FacebookAuthProvider.getCredential(currentUser.token)
//        SeekerApplication.getFirebaseAuthen().signInWithCredential(credential)
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        callBack.onFacebookLoginSuccess(it)
//                    } else {
//                        callBack.onFacebookLoginFailure(it)
//                    }
//
//                }
//
//    }
//
//    override fun gmailSignupFirebase(account: GoogleSignInAccount, callBack: FirebaseCallBack) {
//        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//        SeekerApplication.getFirebaseAuthen().signInWithCredential(credential)
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        callBack.onGmailLoginSuccess(it)
//                    } else {
//                        callBack.onGmailLoginFailure(it)
//                    }
//
//                }
//
//    }
//
//    @SuppressLint("DefaultLocale")
//    override fun createSimpleEmailUser(callBack: FirebaseCallBack) {
//        if (SeekerApplication.getFirebaseAuthen().currentUser != null) {
//            FirebaseAuth.getInstance().signOut()
//        }
//        val lowerEmail = GlobalVariables.seekerEmail?.toLowerCase()
//        val hashPassForFirebase = Hashing.HashValue(lowerEmail)
//        SeekerApplication.getFirebaseAuthen().createUserWithEmailAndPassword(lowerEmail!!, hashPassForFirebase).addOnCompleteListener()
//        { task ->
//            if (task.isSuccessful) {
//                callBack.onUserCreatedSuccess(task)
//              //  utilities.AddSeekerDataToFirebase(requireContext(), userData, SeekerApplication.getFirebaseAuthen().currentUser?.uid)
//            } else {
//                callBack.onUserCreatedFailure(task)
////                val intent = Intent(requireContext(), VerifyReferralActivity::class.java)
////                startActivity(intent)
////                requireActivity().finish()
//            }
//        }
//    }
//
//    override fun updatedSocialSignUpDataToFirebase(userData: UserData, emailExist: Boolean, callBack: FirebaseCallBack) {
//        val user = HashMap<String, Any>()
//        user["seeker_id"] = userData.serviceSeekerId
//        user["seeker_email"] = userData.email
//        if (SeekerApplication.getFirebaseAuthen().uid != null) {
//            SeekerApplication.getFirebaseDatabase().collection("SeekerData").document(SeekerApplication.getFirebaseAuthen().uid.toString()).set(user)
//                    .addOnSuccessListener {
//                        if (emailExist) {
//                            callBack.onUserUpdateSuccess()
//                        } else {
//                            createSimpleEmailUser(callBack)
//                        }
//                    }.addOnFailureListener { e ->
//                        //                                            email_verificaiton_code_btn.isEnabled = true
//                        Log.e("TAG", e.toString())
//                        callBack.onUserUpdateFailure(e)
//                    }
//        }
//    }
//
//    @SuppressLint("DefaultLocale")
//    override fun instaLogin(email: String): Observable<Task<AuthResult>> {
//        val emailInsta = email.split("@")
//        var emailInsta1 = emailInsta[0]
//        var emailInsta2 = emailInsta[1]
//        emailInsta1 += "insta"
//        emailInsta2 = "@$emailInsta2"
//        val mEmailInsta = emailInsta1 + emailInsta2
//
//        val lowerEmail = mEmailInsta.toLowerCase()
//        val hashPassForFirebase = Hashing.HashValue(lowerEmail)
//        return Observable.create { emitter ->
//            SeekerApplication.getFirebaseAuthen().signInWithEmailAndPassword(lowerEmail, hashPassForFirebase)
//                    .addOnCompleteListener {
//                        if (it.isSuccessful)
//                        {
//                            emitter.onNext(it)
//                        }else
//                            emitter.onError(it.exception as Throwable)
//                    }
//        }
////        SeekerApplication.getFirebaseAuthen().signInWithEmailAndPassword(lowerEmail, hashPassForFirebase)
////                .addOnCompleteListener {
////                    if (it.isSuccessful) {
////                        callBack.onInstaLoginSuccess(it)
////                    } else {
////                        callBack.onInstaLoginFailure(it)
////                    }
////                }
//    }
//
//    override fun instaSignUp(email: String): Observable<Task<AuthResult>> {
//        val emailInsta = email.split("@")
//        var emailInsta1 = emailInsta[0]
//        var emailInsta2 = emailInsta[1]
//        emailInsta1 += "insta"
//        emailInsta2 = "@$emailInsta2"
//        val mEmailInsta = emailInsta1 + emailInsta2
//
//        val lowerEmail = mEmailInsta.toLowerCase()
//        val hashPassForFirebase = Hashing.HashValue(lowerEmail)
//        return Observable.create { emitter ->
//            SeekerApplication.getFirebaseAuthen().createUserWithEmailAndPassword(lowerEmail, hashPassForFirebase)
//                    .addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            emitter.onNext(it)
//                        } else
//                            emitter.onError(it.exception as Throwable)
//                    }
//        }
//    }
//
//
////    override fun instaLogin(email: String, callBack: FirebaseCallBack) {
////
////    }
//
//    interface FirebaseCallBack {
//
//        fun onSimpleLoginSuccess(task: Task<AuthResult>)
//        fun onSimpleLoginFailure(task: Task<AuthResult>)
//
//        fun onFacebookLoginSuccess(task: Task<AuthResult>)
//        fun onFacebookLoginFailure(task: Task<AuthResult>)
//
//        fun onGmailLoginSuccess(task: Task<AuthResult>)
//        fun onGmailLoginFailure(task: Task<AuthResult>)
//
//        fun onInstaLoginSuccess(task: Task<AuthResult>)
//        fun onInstaLoginFailure(task: Task<AuthResult>)
//
//        fun onUserCreatedSuccess(task: Task<AuthResult>)
//        fun onUserCreatedFailure(task: Task<AuthResult>)
//
//        fun onUserUpdateSuccess()
//        fun onUserUpdateFailure(e: Exception)
//    }
}