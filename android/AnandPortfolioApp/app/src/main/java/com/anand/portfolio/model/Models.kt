// ===== model/ContactRequest.kt =====
package com.anand.portfolio.model

data class ContactRequest(
    val name: String,
    val email: String,
    val subject: String,
    val message: String
)

data class ContactResponse(
    val success: Boolean,
    val message: String?,
    val error: String?
)
