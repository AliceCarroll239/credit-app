package com.example.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ValidationErrors(
    @SerialName("errors")
    val errors: List<String>
)
