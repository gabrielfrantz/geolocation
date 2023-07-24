package com.example.geolocation.activities.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.geolocation.api.NetworkService
import com.example.geolocation.api.UserRequest
import com.example.geolocation.api.UsersApi
import com.example.geolocation.api.asError
import com.example.geolocation.utils.ActionLiveData
import com.example.geolocation.utils.launchAction
import com.squareup.moshi.Json
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel  @Inject constructor(
    application: Application,
    handle: SavedStateHandle,
    private val usersApi: UsersApi
) : AndroidViewModel(application) {
    private val networkService = NetworkService()

    val password = handle.getLiveData("password", "")

    val passwordConfirmation = handle.getLiveData("passwordConfirmation", "")

    val register = ActionLiveData<Unit, Unit>()
    fun register(): Unit = viewModelScope.launchAction(register, Unit, Dispatchers.IO) {
        val password = password.value.orEmpty()
        val passwordConfirmation = passwordConfirmation.value.orEmpty()

        if (password != passwordConfirmation)
            throw Exception("Senhas não são iguais!")

        //val authApi = networkService.getUsersApi(getApplication())

        val user = UserRequest (
            name = "teste",
            email = "teste@gmail.com",
            username = "teste",
            password = "12345678"
        )
        val response = usersApi.postUser(user)
        val text = response
       /* if (!response.isSuccessful)
            throw Exception(
                try {
                    response.asError?.error
                } catch (ex: Exception) {
                    response.errorBody()?.string()
                }
            )*/

        /*val accountManager = AccountManager.get(getApplication())
        accountManager.setPassword(args.account, newPass)*/
    }
}