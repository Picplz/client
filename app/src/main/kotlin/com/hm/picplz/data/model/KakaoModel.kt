package com.hm.picplz.data.model

import com.hm.picplz.BuildConfig

data class KaKaoAddressRequest(
    val latitude: Double,
    val longitude: Double
)

data class KaKaoAddressResponse(
    val meta: Meta,
    val documents: List<Document>
) {
    data class Meta(
        val total_count: Int
    )

    data class Document(
        val road_address: RoadAddress?,
        val address: Address
    )

    data class RoadAddress(
        val address_name: String,
        val region_1depth_name: String,
        val region_2depth_name: String,
        val region_3depth_name: String,
        val road_name: String,
        val underground_yn: String,
        val main_building_no: String,
        val sub_building_no: String,
        val building_name: String,
        val zone_no: String
    )

    data class Address(
        val address_name: String,
        val region_1depth_name: String,
        val region_2depth_name: String,
        val region_3depth_name: String,
        val mountain_yn: String,
        val main_address_no: String,
        val sub_address_no: String
    )
}