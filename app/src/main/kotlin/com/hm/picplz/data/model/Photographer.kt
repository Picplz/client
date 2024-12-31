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
        location = LatLng.from(37.420960, 127.115587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가2",
        location = LatLng.from(37.412510, 127.125137),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가3",
        location = LatLng.from(37.402960, 127.124587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가4",
        location = LatLng.from(37.392960, 127.125587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가5",
        location = LatLng.from(37.384960, 127.115587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가6",
        location = LatLng.from(37.392960, 127.105587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가7",
        location = LatLng.from(37.402960, 127.106587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가8",
        location = LatLng.from(37.412960, 127.105587),
        profileImageUri = "https://picsum.photos/200"
    )
)