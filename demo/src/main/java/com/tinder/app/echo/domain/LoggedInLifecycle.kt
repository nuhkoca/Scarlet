/*
 * © 2018 Match Group, LLC.
 */

package com.tinder.app.echo.domain

import com.tinder.app.echo.inject.EchoBotScope
import com.tinder.scarlet.v2.Lifecycle
import com.tinder.scarlet.v2.LifecycleState
import com.tinder.scarlet.v2.lifecycle.LifecycleRegistry
import javax.inject.Inject

@EchoBotScope
class LoggedInLifecycle constructor(
    authStatusRepository: AuthStatusRepository,
    private val lifecycleRegistry: LifecycleRegistry
) : Lifecycle by lifecycleRegistry {

    @Inject constructor(authStatusRepository: AuthStatusRepository) : this(authStatusRepository, LifecycleRegistry())

    init {
        authStatusRepository.observeAuthStatus()
            .map {
                when (it) {
                    AuthStatus.LOGGED_IN -> LifecycleState.Started
                    AuthStatus.LOGGED_OUT -> LifecycleState.Stopped
                }
            }
            .subscribe(lifecycleRegistry)
    }
}
