package com.hm.picplz.data.service

import com.hm.picplz.data.model.PhotographerListResponse
import com.hm.picplz.data.model.PhotographerResponse
import com.kakao.vectormap.LatLng
import javax.inject.Inject
import kotlin.random.Random

interface PhotographerService {
    suspend fun getPhotographers(): PhotographerListResponse
}

class PhotographerServiceImpl @Inject constructor() : PhotographerService {
    override suspend fun getPhotographers(): PhotographerListResponse {
        // TODO: API 작동
        // return apiClient.getPhotographers()

        return if (Random.nextBoolean()) {
            dummyPhotographers
        } else {
            dummyPhotographersTwo
        }
    }
}


val dummyPhotographers = listOf(
    PhotographerResponse(
        id = 1,
        name = "작가1",
        location = null,
        profileImageUri = "https://picsum.photos/200",
        isActive = false,
        workingArea = "마포구 서교동",
        distance = 100,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 2,
        name = "작가2",
        location = LatLng.from(37.412510, 127.125137),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 200,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 3,
        name = "작가3",
        location = null,
        profileImageUri = "https://picsum.photos/200",
        isActive = false,
        workingArea = "종로구 무악동",
        distance = 400,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 4,
        name = "작가4",
        location = null,
        profileImageUri = "https://picsum.photos/200",
        isActive = false,
        workingArea = "종로구 무악동",
        distance = 300,
        followers = listOf(1, 2, 4),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 5,
        name = "작가5",
        location = LatLng.from(37.384960, 127.115587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 100,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 6,
        name = "작가6",
        location = null,
        profileImageUri = "https://picsum.photos/200",
        isActive = false,
        workingArea = "종로구 무악동",
        distance = 100,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 7,
        name = "작가7",
        location = LatLng.from(37.402960, 127.106587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 100,
        followers = listOf(1, 2, 4),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 8,
        name = "작가8",
        location = LatLng.from(37.412960, 127.105587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 100,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    )
)

val dummyPhotographersTwo = listOf(
    PhotographerResponse(
        id = 1,
        name = "작가1",
        location = LatLng.from(37.403960, 127.116587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 100,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 2,
        name = "작가2",
        location = LatLng.from(37.401960, 127.114587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 100,
        followers = listOf(1, 2, 4),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 3,
        name = "작가3",
        location = LatLng.from(37.404460, 127.113587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 100,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 4,
        name = "작가4",
        location = LatLng.from(37.401460, 127.117587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 100,
        followers = listOf(1, 2, 4),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 5,
        name = "작가5",
        location = LatLng.from(37.404960, 127.117587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 200,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 6,
        name = "작가6",
        location = LatLng.from(37.400960, 127.113587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 100,
        followers = listOf(1, 2, 3),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 7,
        name = "작가7",
        location = LatLng.from(37.405960, 127.114587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 500,
        followers = listOf(1, 2, 4),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    ),
    PhotographerResponse(
        id = 8,
        name = "작가8",
        location = LatLng.from(37.400960, 127.117587),
        profileImageUri = "https://picsum.photos/200",
        isActive = true,
        workingArea = "종로구 무악동",
        distance = 200,
        followers = listOf(1, 2, 4),
        socialAccount= "@account",
        portfolioPhotos = List(5) { "https://picsum.photos/100" },
    )
)