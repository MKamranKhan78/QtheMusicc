package com.techswivel.qthemusic.source.local.preference

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.constant.Constants.PREF_NAME
import com.techswivel.qthemusic.customData.interfaces.IDataStorePreferenceAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

object  DataStoreUtils : IDataStorePreferenceAPI {
    private val Context.dataStore by preferencesDataStore(
        name = PREF_NAME
    )

    private val dataSource = QTheMusicApplication.getContext().dataStore

    override suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T):
            Flow<T> = dataSource.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val result = preferences[key] ?: defaultValue
        result
    }


    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataSource.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataSource.edit {
            it.remove(key)
        }
    }

    override suspend fun clearAllPreference() {
        dataSource.edit { preferences ->
            preferences.clear()
        }
    }
}
