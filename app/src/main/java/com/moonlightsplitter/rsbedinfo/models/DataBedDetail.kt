package com.moonlightsplitter.rsbedinfo.models

import java.io.Serializable

data class DataBedDetail(
        var id: String? = null,
        var name: String? = null,
        var address: String? = null,
        var phone: String? = null,
        var bedDetail: ArrayList<BedDetail>? = null
): Serializable
