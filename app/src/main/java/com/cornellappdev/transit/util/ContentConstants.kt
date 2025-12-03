package com.cornellappdev.transit.util

import android.adservices.adid.AdId


/**
 * Temporary mapping for about content
 */
fun getAboutContent(key: String): String {
    val aboutContent = buildMap {
        put("104West!", "Cornell's kosher and multicultural dining room is STAR-K and STAR-D certified.")
        put("Becker House Dining Room", "Dining room located in Carl Becker House on West Campus. Open only to residents from 6-7pm Wednesdays for House Dinners.")
        put("Cook House Dining Room", "Dining room located in Alice Cook House on West Campus. Open only to residents from 6-7pm Wednesdays for House Dinners.")
        put("Jansen's Dining Room at Bethe House", "Dining room located in Hans Bethe House on West Campus. Open only to residents from 6-7pm Wednesdays for House Dinners.")
        put("Keeton House Dining Room", "Dining room located in William Keeton House on West Campus. Open only to residents from 6-7pm Wednesdays for House Dinners.")
        put("Morrison Dining", "Choose your own culinary adventure at Cornell's newest dining room.")
        put("North Star Dining Room", "Dining room located in Appel Commons on North Campus.")
        put("Okenshields", "Dining room located in Willard Straight Hall on Central Campus.")
        put("Risley Dining Room", "Risley is our gluten-free, tree nut free and peanut free dining room under the AllerCheck™\uFE0F approved by MenuTrinfo®\uFE0F program, in Risley Residential College on North Campus.")
        put("Rose House Dining Room", "Dining room located in Flora Rose House on West Campus. Open only to residents from 6-7pm Wednesdays for House Dinners.")
    }

    return aboutContent.getOrDefault(key, "")
}

/**
 * Mapping gym names to location descriptions
 */
fun getGymLocationString(key: String): String {
    val locationStrings = buildMap {
        put("Helen Newman", "Helen Newman Hall")
        put("Toni Morrison", "Toni Morrison Hall")
        put("Noyes", "Noyes Community Recreation Center")
        put("Teagle Up", "Teagle Hall")
        put("Teagle Down", "Teagle Hall")

    }

    return locationStrings.getOrDefault(key, "")
}