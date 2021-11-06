package com.moonlightsplitter.rsbedinfo.models

import java.io.Serializable

data class DataHospital(
        var id: String? = null,
        var name: String? = null,
        var address: String? = null,
        var phone: String? = null,
        var queue: Int? = null,
        var bed_availability: Int? = null,
        var available_beds: List<DataBed>? = null,
        var info: String? = null
): Serializable
