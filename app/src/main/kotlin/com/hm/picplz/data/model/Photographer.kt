package com.hm.picplz.data.model

import com.kakao.vectormap.LatLng

data class Photographer (
    val name: String,
    val location: LatLng,
    val profileImageUri: String,
)


val dummyPhotographers = listOf(
    Photographer(
        name = "작가1",
        location = LatLng.from(37.406960, 127.115587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가2",
        location = LatLng.from(37.408960, 127.117587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가3",
        location = LatLng.from(37.384921, 127.125171),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가4",
        location = LatLng.from(37.339832, 127.109160),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가5",
        location = LatLng.from(37.340521, 127.108872),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가6",
        location = LatLng.from(37.339245, 127.109876),
        profileImageUri = "https://picsum.photos/200"
    )
)