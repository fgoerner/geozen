package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position

/**
 * Utility object for geometric calculations and predicates.
 *
 * These methods use planar approximations (treating lat/lon as x/y coordinates)
 * which are suitable for intersection detection and orientation tests over
 * small to medium distances. The planar approximation is acceptable for intersection
 * detection because segments that intersect are by definition very close together,
 * and at close range, the spheroid surface is nearly planar.
 */
internal object GeometricUtils {

    /**
     * Checks if two line segments intersect using the cross product method.
     *
     * This method uses a planar approximation (treating lat/lon as x/y coordinates),
     * which is reasonable for small distances. For segments that are far apart or
     * very long, this approximation may introduce small errors, but for intersection
     * detection it's generally sufficient.
     *
     * **How the algorithm works:**
     *
     * The algorithm is based on checking the "orientation" (clockwise, counterclockwise, or collinear)
     * of point triplets. Two segments intersect if and only if one of these conditions is true:
     *
     * 1. **General case**: The segments "straddle" each other. This means:
     *    - The two endpoints of segment 2 are on opposite sides of the line defined by segment 1
     *    - AND the two endpoints of segment 1 are on opposite sides of the line defined by segment 2
     *
     * 2. **Special case**: One segment's endpoint lies directly on the other segment (collinear case)
     *
     * **Visual Example:**
     *
     * Intersecting segments (X pattern):
     * ```
     *     A
     *    / \
     *   /   \
     *  C     D
     *   \   /
     *    \ /
     *     B
     * ```
     * Segment AB intersects segment CD because:
     * - C and D are on opposite sides of line AB (different orientations)
     * - A and B are on opposite sides of line CD (different orientations)
     *
     * Non-intersecting segments:
     * ```
     *   A---B
     *
     *       C---D
     * ```
     * Segments don't intersect because C and D are on the same side of line AB.
     *
     * **What "orientation" means:**
     *
     * For three points P, Q, R, the orientation tells us if we turn left (counterclockwise),
     * turn right (clockwise), or go straight (collinear) when traveling from P→Q→R.
     *
     * This is calculated using the cross product of vectors (Q-P) and (R-Q):
     * - Cross product > 0: counterclockwise turn (points are arranged counterclockwise)
     * - Cross product < 0: clockwise turn (points are arranged clockwise)
     * - Cross product = 0: collinear (all three points lie on the same line)
     *
     * @param seg1Start start position of the first segment
     * @param seg1End   end position of the first segment
     * @param seg2Start start position of the second segment
     * @param seg2End   end position of the second segment
     * @return true if the segments intersect, false otherwise
     */
    fun doSegmentsIntersect(
        seg1Start: Position,
        seg1End: Position,
        seg2Start: Position,
        seg2End: Position
    ): Boolean {
        // Check if segments share an endpoint
        if (seg1Start == seg2Start || seg1Start == seg2End || seg1End == seg2Start || seg1End == seg2End) {
            return true
        }

        // Calculate orientations for segment 1's line with respect to segment 2's endpoints
        // o1: orientation of triplet (seg1Start, seg1End, seg2Start)
        // o2: orientation of triplet (seg1Start, seg1End, seg2End)
        val o1 = orientation(seg1Start, seg1End, seg2Start)
        val o2 = orientation(seg1Start, seg1End, seg2End)

        // Calculate orientations for segment 2's line with respect to segment 1's endpoints
        // o3: orientation of triplet (seg2Start, seg2End, seg1Start)
        // o4: orientation of triplet (seg2Start, seg2End, seg1End)
        val o3 = orientation(seg2Start, seg2End, seg1Start)
        val o4 = orientation(seg2Start, seg2End, seg1End)

        // General case: segments intersect if they have different orientations
        // This means:
        // - seg2Start and seg2End are on opposite sides of the line through seg1 (o1 != o2)
        // - AND seg1Start and seg1End are on opposite sides of the line through seg2 (o3 != o4)
        if (o1 != o2 && o3 != o4) {
            return true
        }

        // Special cases: check if point lies on segment (when orientation is 0, meaning collinear)
        // If three points are collinear and one lies between the other two, the segments touch
        if (o1 == 0 && onSegment(seg1Start, seg2Start, seg1End)) return true
        if (o2 == 0 && onSegment(seg1Start, seg2End, seg1End)) return true
        if (o3 == 0 && onSegment(seg2Start, seg1Start, seg2End)) return true
        if (o4 == 0 && onSegment(seg2Start, seg1End, seg2End)) return true

        return false
    }

    /**
     * Calculates the orientation of an ordered triplet of points P, Q, R.
     *
     * **The math behind it:**
     *
     * We calculate the cross product of vectors (Q-P) and (R-Q):
     * ```
     * Cross product = (Q.lat - P.lat) * (R.lon - Q.lon) - (Q.lon - P.lon) * (R.lat - Q.lat)
     * ```
     *
     * **Geometric interpretation:**
     * - If you walk from P to Q to R:
     *   - Positive cross product (1): you turn RIGHT (clockwise)
     *   - Negative cross product (2): you turn LEFT (counterclockwise)
     *   - Zero cross product (0): you go STRAIGHT (collinear)
     *
     * **Visual examples:**
     *
     * Clockwise (returns 1):
     * ```
     *     Q
     *    /
     *   P
     *    \
     *     R
     * ```
     *
     * Counterclockwise (returns 2):
     * ```
     *     Q
     *      \
     *       P
     *      /
     *     R
     * ```
     *
     * Collinear (returns 0):
     * ```
     *   P---Q---R
     * ```
     *
     * @param p first point
     * @param q second point
     * @param r third point
     * @return 0 if collinear, 1 if clockwise, 2 if counterclockwise
     */
    fun orientation(p: Position, q: Position, r: Position): Int {
        val val1 = (q.latitude - p.latitude) * (r.longitude - q.longitude)
        val val2 = (q.longitude - p.longitude) * (r.latitude - q.latitude)
        val diff = val1 - val2

        val epsilon = 1e-10
        return when {
            diff in -epsilon..epsilon -> 0  // Collinear
            diff > 0 -> 1          // Clockwise
            else -> 2               // Counterclockwise
        }
    }

    /**
     * Checks if point q lies on segment pr (given that p, q, r are collinear).
     *
     * This method performs a bounding box check to determine if q is between p and r.
     *
     * @param p start of segment
     * @param q point to check
     * @param r end of segment
     * @return true if q lies on segment pr, false otherwise
     */
    fun onSegment(p: Position, q: Position, r: Position): Boolean {
        return q.longitude <= maxOf(p.longitude, r.longitude) &&
                q.longitude >= minOf(p.longitude, r.longitude) &&
                q.latitude <= maxOf(p.latitude, r.latitude) &&
                q.latitude >= minOf(p.latitude, r.latitude)
    }
}

