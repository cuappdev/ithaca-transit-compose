package com.cornellappdev.transit.models.ecosystem

import com.squareup.moshi.Json

/**
 * Data class representing an eatery
 */
data class Eatery(
    @Json(name = "id") var id: Int,
    @Json(name = "name") var name: String,
    @Json(name = "menu_summary") var menuSummary: String?,
    @Json(name = "image_url") var imageUrl: String?,
    @Json(name = "location") var location: String?,
    @Json(name = "campus_area") var campusArea: String?,
    @Json(name = "online_order_url") var onlineOrderUrl: String?,
    @Json(name = "latitude") var latitude: Double?,
    @Json(name = "longitude") var longitude: Double?,
    @Json(name = "payment_accepts_meal_swipes") var paymentAcceptsMealSwipes: Boolean?,
    @Json(name = "payment_accepts_brbs") var paymentAcceptsBrbs: Boolean?,
    @Json(name = "payment_accepts_cash") var paymentAcceptsCash: Boolean?,
    )