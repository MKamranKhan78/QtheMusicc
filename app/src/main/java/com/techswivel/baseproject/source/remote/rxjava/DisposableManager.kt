package com.techswivel.baseproject.source.remote.rxjava

import com.techswivel.baseproject.utils.Log

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

object DisposableManager {

    private val TAG = "DisposableManager"

    private var mCompositeDisposable: CompositeDisposable? = null

    private val compositeDisposable: CompositeDisposable
        get() {
            if (mCompositeDisposable == null || mCompositeDisposable!!.isDisposed) {
                mCompositeDisposable = CompositeDisposable()
            }
            return mCompositeDisposable as CompositeDisposable
        }

    fun add(disposable: Disposable) {
        try {
            compositeDisposable.add(disposable)
        } catch (e: Exception) {
            Log.e(TAG, "add: ", e)
        }

    }

    fun dispose() {
        try {
            compositeDisposable.dispose()
        } catch (e: Exception) {
            Log.e(TAG, "dispose: ", e)
        }

    }
}
