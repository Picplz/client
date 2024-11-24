package com.hm.picplz.data.model

data class KaKaoAddressRequest(
    val x: String,
    val y: String
)

data class KaKaoAddressResponse(
    val meta: Meta,
    val documents: List<Document>
) {
    data class Meta(
        val total_count: Int
    )

    data class Document(
        val address: Address,
        val road_address: RoadAddress?
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
}