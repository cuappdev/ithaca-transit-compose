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
        put("Helen Newman", "Helen Newman Hall features a Pool, two-full sized Courts for Basketball/Volleyball/Badminton, a classroom and a dance studio where many Group Fitness classes, Physical Education classes, and club practices are held, a 16-lane Bowling Center and a Fitness Center. Helen Newman is also the home to the majority of the Recreational Services Administration Offices.")
        put("Toni Morrison", "The Toni Morrison Fitness Center is located in the basement of Toni Morrison Hall.")
        put("Noyes", "The Noyes Fitness Center is located on the second floor of the Noyes Community Recreation Center, adjacent to Jansen's Market.")
        put("Teagle Down", "The Teagle Downstairs Fitness Center is located on the ground floor of Teagle Hall. The entrance of this Fitness Center is adjacent from the entrance to Teagle Hall from the parking lot facing the Lynah Ice Rink.")
        put("Teagle Up", "The Teagle Upstairs Fitness Center is located on the second floor of Teagle Hall. The staircase to the entrance of this Fitness Center is directly across from the entrance to Teagle Hall from the parking lot facing the Lynah Ice Rink.")
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