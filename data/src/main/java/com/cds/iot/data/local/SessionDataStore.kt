package com.cds.iot.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cds.iot.domain.model.Session
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionStore by preferencesDataStore("kuda_session")

@Singleton
class SessionDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val USER_ID = intPreferencesKey("user_id")
        val PHONE = stringPreferencesKey("phone")
        val NICKNAME = stringPreferencesKey("nickname")
        val AVATAR = stringPreferencesKey("avatar")
        val TOKEN = stringPreferencesKey("token")
        val LOGGED_IN = booleanPreferencesKey("logged_in")
        val DEMO_MODE = booleanPreferencesKey("demo_mode")
    }

    val session: Flow<Session> = context.sessionStore.data.map { prefs ->
        Session(
            userId = prefs[Keys.USER_ID] ?: 0,
            phone = prefs[Keys.PHONE].orEmpty(),
            nickname = prefs[Keys.NICKNAME].orEmpty(),
            avatarUrl = prefs[Keys.AVATAR].orEmpty(),
            token = prefs[Keys.TOKEN].orEmpty(),
            isLoggedIn = prefs[Keys.LOGGED_IN] ?: false,
        )
    }

    val demoMode: Flow<Boolean> = context.sessionStore.data.map { prefs ->
        prefs[Keys.DEMO_MODE] ?: true
    }

    suspend fun saveSession(session: Session) {
        context.sessionStore.edit { prefs ->
            prefs[Keys.USER_ID] = session.userId
            prefs[Keys.PHONE] = session.phone
            prefs[Keys.NICKNAME] = session.nickname
            prefs[Keys.AVATAR] = session.avatarUrl
            prefs[Keys.TOKEN] = session.token
            prefs[Keys.LOGGED_IN] = session.isLoggedIn
        }
    }

    suspend fun clear() {
        context.sessionStore.edit { prefs ->
            val demo = prefs[Keys.DEMO_MODE]
            prefs.clear()
            if (demo != null) prefs[Keys.DEMO_MODE] = demo
        }
    }

    suspend fun setDemoMode(enabled: Boolean) {
        context.sessionStore.edit { it[Keys.DEMO_MODE] = enabled }
    }
}
