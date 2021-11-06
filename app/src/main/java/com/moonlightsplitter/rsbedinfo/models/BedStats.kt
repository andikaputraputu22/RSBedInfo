package com.moonlightsplitter.rsbedinfo.models

import java.io.Serializable

data class BedStats(
        var title: String? = null,
        var bed_available: Int? = null,
        var bed_empty: Int? = null,
        var queue: Int? = null
): Serializable
