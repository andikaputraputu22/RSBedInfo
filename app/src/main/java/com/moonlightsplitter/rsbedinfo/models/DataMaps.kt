package com.moonlightsplitter.rsbedinfo.models

import java.io.Serializable

data class DataMaps(
        var id: String? = null,
        var name: String? = null,
        var address: String? = null,
        var lat: String? = null,
        var long: String? = null,
        var gmaps: String? = null
): Serializable
