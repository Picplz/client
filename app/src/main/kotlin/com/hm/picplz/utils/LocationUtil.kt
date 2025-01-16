package com.hm.picplz.utils

import com.kakao.vectormap.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object LocationUtil {
    private const val EARTH_RADIUS_KM = 6371.0

    /**
     * 두 위치 간의 직선 거리를 계산 (km)
     */
    fun getDistance(location1: LatLng, location2: LatLng): Double {
        val earthRadius = EARTH_RADIUS_KM
        val deltaLat = Math.toRadians(location2.latitude - location1.latitude)
        val deltaLng = Math.toRadians(location2.longitude - location1.longitude)

        val haversine = sin(deltaLat/2).pow(2) +
                cos(Math.toRadians(location1.latitude)) *
                cos(Math.toRadians(location2.latitude)) *
                sin(deltaLng/2).pow(2)

        val angularDistance = 2 * atan2(sqrt(haversine), sqrt(1-haversine))
        return earthRadius * angularDistance
    }

    /**
     * 두 위치 간의 상대적 거리를 x,y 좌표로 변환 (km)
     */
    fun calculateRelativeDistance(from: LatLng, to: LatLng): Pair<Double, Double> {
        val latKmPerDegree = 111.0
        val lngKmPerDegree = 111.0 * cos(Math.toRadians(from.latitude))

        val latDiff = (to.latitude - from.latitude) * latKmPerDegree
        val lngDiff = (to.longitude - from.longitude) * lngKmPerDegree

        return Pair(lngDiff, latDiff)
    }

    /**
     * 화면상의 두 점의 거리
     */
    fun calcurateScreenDistance(from: Pair<Float, Float>, to: Pair<Float, Float>): Float {
        val dx = from.first - to.first
        val dy = from.second - to.second
        return sqrt(dx * dx + dy * dy)
    }
}