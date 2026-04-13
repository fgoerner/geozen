package dev.goerner.geozen.model

import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.tan

internal object Reprojector {

    private const val EARTH_RADIUS_METERS = 6378137.0
    private const val WEB_MERCATOR_MAX_LATITUDE = 85.051129

    fun reproject(pos: Position, fromCrs: CoordinateReferenceSystem, toCrs: CoordinateReferenceSystem): Position =
        when (toCrs) {
            CoordinateReferenceSystem.WGS_84 -> toWgs84(pos, fromCrs)
            CoordinateReferenceSystem.WEB_MERCATOR -> toWebMercator(pos, fromCrs)
        }

    private fun toWgs84(pos: Position, crs: CoordinateReferenceSystem): Position = when (crs) {
        CoordinateReferenceSystem.WGS_84 -> pos
        CoordinateReferenceSystem.WEB_MERCATOR -> Position(
            Math.toDegrees(pos.longitude / EARTH_RADIUS_METERS),
            Math.toDegrees(2.0 * atan(exp(pos.latitude / EARTH_RADIUS_METERS)) - Math.PI / 2.0),
            pos.altitude
        )
    }

    private fun toWebMercator(pos: Position, crs: CoordinateReferenceSystem): Position = when (crs) {
        CoordinateReferenceSystem.WEB_MERCATOR -> pos
        CoordinateReferenceSystem.WGS_84 -> {
            require(pos.latitude in -WEB_MERCATOR_MAX_LATITUDE..WEB_MERCATOR_MAX_LATITUDE) {
                "Latitude ${pos.latitude} is outside the valid range for Web Mercator " +
                        "[-$WEB_MERCATOR_MAX_LATITUDE, $WEB_MERCATOR_MAX_LATITUDE]"
            }
            Position(
                EARTH_RADIUS_METERS * Math.toRadians(pos.longitude),
                EARTH_RADIUS_METERS * ln(tan(Math.PI / 4.0 + Math.toRadians(pos.latitude) / 2.0)),
                pos.altitude
            )
        }
    }
}