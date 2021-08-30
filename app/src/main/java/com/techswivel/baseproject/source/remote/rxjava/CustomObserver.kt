package com.techswivel.baseproject.Data.RemoteRepository.ServerRepository

import com.techswivel.baseproject.source.remote.rxjava.CustomError
import com.techswivel.baseproject.source.remote.rxjava.DisposableManager
import com.techswivel.baseproject.utils.Log
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

/**
 * @param <T>
</T> */

abstract class CustomObserver<T> : io.reactivex.Observer<T> {

    override fun onSubscribe(d: Disposable) {
        DisposableManager.add(d)
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        try {
            Log.e(TAG, "onError: ", e)
            if (e is HttpException) {
                onError(e, false, getErrorMessage(e))
            } else if (e is Exception) {
                onError(
                    e, false,
                    CustomError(
                        e.message!!,
                        e.hashCode()
                    )
                )
            } else {
                onError(e, true, null)
            }

        } catch (ex: Exception) {
            onError(e, false, null)
        }

    }

    override fun onComplete() {
        Log.d(TAG, "onComplete: command complete")
        onRequestComplete()
    }

    private fun getErrorMessage(exception: HttpException): CustomError {
        try {
            return CustomError(
                exception.message(),
                exception.code()
            )
        } catch (e: Exception) {
            return CustomError(
                e.message!!,
                exception.code()
            )
        }

    }

    abstract fun onSuccess(@NonNull t: T)

    abstract fun onError(
        @NonNull e: Throwable,
        isInternetError: Boolean,
        @Nullable error: CustomError?
    )

    abstract fun onRequestComplete()

    companion object {

        private val TAG = "CustomObserver"
    }

}
