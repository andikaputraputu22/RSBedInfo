package com.moonlightsplitter.rsbedinfo.models

import java.io.Serializable

data class DataBed(
        var available: Int? = null,
        var bed_class: String? = null,
        var room_name: String? = null,
        var info: String? = null
): Serializable
