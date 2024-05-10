package com.univer.onlinestore.ui.login

import com.univer.onlinestore.data.model.LoggedInUser

data class LoginResult(
    val success: LoggedInUser? = null,
    val error: Int? = null
)