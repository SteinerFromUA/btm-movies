package com.steiner.btmmovies.domain.usecase.other

import androidx.core.os.bundleOf
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.squareup.moshi.Moshi
import com.steiner.btmmovies.app.di.account.AccountScope
import com.steiner.btmmovies.core.usecase.ResultSuspendUseCase
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider
import com.steiner.btmmovies.domain.model.Profile
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *
 */
@AccountScope
class GetProfileUseCase @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatcherProvider,
    private val moshi: Moshi
) : ResultSuspendUseCase<GetProfileParameters, Profile>(coroutineDispatchers.computation) {

    override suspend fun execute(parameters: GetProfileParameters): Profile {
        val params = bundleOf("fields" to "id,name,picture,email")
        val request = GraphRequest(
            AccessToken.getCurrentAccessToken(),
            ME_ENDPOINT,
            params,
            HttpMethod.GET
        )
        val response = withContext(coroutineDispatchers.network) {
            request.executeAndWait()
        }
        val jsonAdapter = moshi.adapter(Profile::class.java)

        return requireNotNull(jsonAdapter.fromJson(response.rawResponse)) {
            "Null Facebook profile was parse."
        }
    }

    companion object {
        private const val ME_ENDPOINT = "/me"
    }
}

/**
 *
 */
class GetProfileParameters
