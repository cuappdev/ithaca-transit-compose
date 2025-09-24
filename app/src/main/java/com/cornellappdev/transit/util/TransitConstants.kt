package com.cornellappdev.transit.util

import com.cornellappdev.transit.BuildConfig

const val ECOSYSTEM_FLAG = BuildConfig.ECOSYSTEM_FLAG


var SEARCH_URL = if (BuildConfig.ECOSYSTEM_FLAG) "/api/v3/appleSearch" else "/api/v2/appleSearch"