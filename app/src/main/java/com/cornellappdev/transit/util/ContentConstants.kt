package com.cornellappdev.transit.util

private val ABOUT_CONTENT = mapOf(
    "Terrace Restaurant" to "The Terrace often features five to six made-to-order options, such as burritos, pho, gyros, and more throughout the day.",
    "Mac's Café" to "Mac's features grab-and-go deli sandwiches, pizza, chopped salads and healthy smoothies for those looking for a quick bite.",
    "Mac's Cafe" to "Mac's features grab-and-go deli sandwiches, pizza, chopped salads and healthy smoothies for those looking for a quick bite.",
    "Temple of Zeus" to "Serving up fresh soups, salads, and sandwiches for students and faculty.",
    "Gimme Coffee" to "Cappuccinos, hot coffee, and pastries are available here Monday-Friday from 8am-3pm!",
    "Louie's Lunch" to "Louie's Lunch is a food truck featuring an extensive menu with subs, wraps, salads, milkshakes, cajun fries, and much more.",
    "Anabel's Grocery" to "Anabel's Grocery is a student-run nonprofit grocery store that provides fresh, nutritious, and affordable food to Cornell students.",
    "Free Food Fridge" to "The Free Food Fridge stocks excess food from campus cafes and leftover food from campus events to prevent it from being wasted.",
    "Helen Newman" to "Helen Newman Hall features a Pool, two-full sized Courts for Basketball/Volleyball/Badminton, a classroom and a dance studio where many Group Fitness classes, Physical Education classes, and club practices are held, a 16-lane Bowling Center and a Fitness Center. Helen Newman is also the home to the majority of the Recreational Services Administration Offices.",
    "Toni Morrison" to "The Toni Morrison Fitness Center is located in the basement of Toni Morrison Hall.",
    "Noyes" to "The Noyes Fitness Center is located on the second floor of the Noyes Community Recreation Center, adjacent to Jansen's Market.",
    "Teagle Down" to "The Teagle Downstairs Fitness Center is located on the ground floor of Teagle Hall. The entrance of this Fitness Center is adjacent from the entrance to Teagle Hall from the parking lot facing the Lynah Ice Rink.",
    "Teagle Up" to "The Teagle Upstairs Fitness Center is located on the second floor of Teagle Hall. The staircase to the entrance of this Fitness Center is directly across from the entrance to Teagle Hall from the parking lot facing the Lynah Ice Rink.",
)

private val GYM_LOCATION_STRINGS = mapOf(
    "Helen Newman" to "Helen Newman Hall",
    "Toni Morrison" to "Toni Morrison Hall",
    "Noyes" to "Noyes Recreation Center",
    "Teagle Up" to "Teagle Hall",
    "Teagle Down" to "Teagle Hall",
)


/**
 * Temporary mapping for about content
 */
fun getAboutContent(key: String): String {
    return ABOUT_CONTENT[key].orEmpty()
}

/**
 * Mapping gym names to location descriptions
 */
fun getGymLocationString(key: String): String {
    return GYM_LOCATION_STRINGS[key].orEmpty()
}