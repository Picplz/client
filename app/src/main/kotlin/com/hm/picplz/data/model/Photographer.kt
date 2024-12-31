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
        location = LatLng.from(37.411960, 127.115587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가2",
        location = LatLng.from(37.409960, 127.119587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가3",
        location = LatLng.from(37.402960, 127.119587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가4",
        location = LatLng.from(37.397960, 127.118587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가5",
        location = LatLng.from(37.397960, 127.115587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가6",
        location = LatLng.from(37.398960, 127.111587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가7",
        location = LatLng.from(37.402960, 127.111587),
        profileImageUri = "https://picsum.photos/200"
    ),
    Photographer(
        name = "작가8",
        location = LatLng.from(37.406960, 127.112587),
        profileImageUri = "https://picsum.photos/200"
    )
)