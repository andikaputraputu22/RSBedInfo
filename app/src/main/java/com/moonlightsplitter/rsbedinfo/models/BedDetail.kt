package com.moonlightsplitter.rsbedinfo.models

import java.io.Serializable

data class BedDetail(
        var time: String? = null,
        var stats: BedStats? = null
): Serializable
