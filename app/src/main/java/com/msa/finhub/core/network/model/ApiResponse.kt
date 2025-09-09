package com.msa.finhub.core.network.model

import kotlinx.serialization.Serializable

/**
 * مدل استاندارد پاسخ API که شامل اطلاعات وضعیت، داده‌ها و خطاهای منطقی است.
 */
@Serializable
data class ApiResponse<T>(
    val code: Int,
    val status: String,
    val data: T? = null,
    val message: String = "",
    val hasError: Boolean = false,
    val pagination: Pagination? = null
)


@Serializable
data class Pagination(
    val currentPage: Int,
    val totalPages: Int,
    val itemsPerPage: Int,
    val totalItems: Long
)