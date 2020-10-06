package com.swein.shsceneform3dcode.sceneformpart.tool;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;

import com.google.ar.core.Pose;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants;

import java.util.ArrayList;
import java.util.List;

import static com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants.SFUnit.CM;
import static com.swein.shsceneform3dcode.sceneformpart.constants.SFConstants.SFUnit.M;

public class MathTool {

    public static Vector3 getNormalVectorOfThreeVectors(Vector3 original, Vector3 a, Vector3 b) {

        Vector3 side1 = new Vector3(a.x - original.x, a.y - original.y, a.z - original.z);
        Vector3 side2 = new Vector3(b.x - original.x, b.y - original.y, b.z - original.z);

        return Vector3.cross(side1, side2);
    }

    public static Vector3 transformWorldPositionToLocalPositionOfParent(Node parent, Vector3 worldPosition) {
        return parent.worldToLocalPoint(worldPosition);
    }

    public static float getLengthOfTwoNode(Node startNode, Node endNode) {
        Vector3 startVector3 = startNode.getWorldPosition();
        Vector3 endVector3 = endNode.getWorldPosition();

        Vector3 difference = Vector3.subtract(startVector3, endVector3);
        return difference.length();
    }

    public static float getLengthOfTwoNode(Vector3 startVector3, Vector3 endVector3) {

        Vector3 difference = Vector3.subtract(startVector3, endVector3);
        return difference.length();
    }

    public static float getLengthOfTwoNode2D(float startX, float startY, float endX, float endY) {

        return (float) Math.sqrt(Math.pow((startX - endX), 2) + Math.pow((startY - endY), 2));
    }

    public static double getNodesDistanceMetersWithoutHeight(Node startNode, Node endNode) {
        float dx = startNode.getWorldPosition().x - endNode.getWorldPosition().x;
        float dz = startNode.getWorldPosition().z - endNode.getWorldPosition().z;

        return Math.sqrt(dx * dx + dz * dz);
    }

    public static double getNodesDistanceMeters(Pose startPose, Pose endPose) {
        float dx = startPose.tx() - endPose.tx();
        float dy = startPose.ty() - endPose.ty();
        float dz = startPose.tz() - endPose.tz();

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static double getNodesDistanceMeters(Node startNode, Node endNode) {
        float dx = startNode.getWorldPosition().x - endNode.getWorldPosition().x;
        float dy = startNode.getWorldPosition().y - endNode.getWorldPosition().y;
        float dz = startNode.getWorldPosition().z - endNode.getWorldPosition().z;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * calculate polygon plane area in 3D
     */
    public static float calculateArea(List<Node> list, Vector3 normalVectorOfPlane) {

        List<Vector3> vector3List = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            vector3List.add(list.get(i).getWorldPosition());
        }
        vector3List.add(list.get(0).getWorldPosition());

        return Math.abs(area3DPolygon(list.size(), vector3List, normalVectorOfPlane));
    }

    public static float getLengthBetweenPointToPlane(Vector3 pointInAir, Vector3 pointAtPlane, Vector3 normalVectorOfPlane) {

        Vector3 ab = new Vector3(pointInAir.x - pointAtPlane.x, pointInAir.y - pointAtPlane.y, pointInAir.z - pointAtPlane.z);
        return Math.abs(Vector3.dot(ab, normalVectorOfPlane) / normalVectorOfPlane.length());
    }

    /**
     * computes the area of a 3D planar polygon
     *
     * @param n:                   the number of vertices in the polygon
     * @param vector3List:         an array of n+1 points in a 2D plane with V[n]=V[0]
     * @param normalVectorOfPlane: unit normal vector of the polygon's plane
     * @return the (float) area of the polygon
     */
    public static float area3DPolygon(int n, List<Vector3> vector3List, Vector3 normalVectorOfPlane) {
        float area = 0;
        float an, ax, ay, az; // abs value of normal and its coords
        int coord;           // coord to ignore: 1=x, 2=y, 3=z
        int i, j, k;         // loop indices

        if (n < 3) {
            return 0;  // a degenerate polygon
        }

        // select largest abs coordinate to ignore for projection
        ax = (normalVectorOfPlane.x > 0 ? normalVectorOfPlane.x : -normalVectorOfPlane.x);    // abs x-coord
        ay = (normalVectorOfPlane.y > 0 ? normalVectorOfPlane.y : -normalVectorOfPlane.y);    // abs y-coord
        az = (normalVectorOfPlane.z > 0 ? normalVectorOfPlane.z : -normalVectorOfPlane.z);    // abs z-coord

        coord = 3;                    // ignore z-coord
        if (ax > ay) {
            if (ax > az) {
                coord = 1;   // ignore x-coord
            }
        }
        else if (ay > az) {
            coord = 2;  // ignore y-coord
        }

        // compute area of the 2D projection
        switch (coord) {
            case 1:
                for (i = 1, j = 2, k = 0; i < n; i++, j++, k++) {
                    area += (vector3List.get(i).y * (vector3List.get(j).z - vector3List.get(k).z));
                }
                break;
            case 2:
                for (i = 1, j = 2, k = 0; i < n; i++, j++, k++) {
                    area += (vector3List.get(i).z * (vector3List.get(j).x - vector3List.get(k).x));
                }
                break;
            case 3:
                for (i = 1, j = 2, k = 0; i < n; i++, j++, k++) {
                    area += (vector3List.get(i).x * (vector3List.get(j).y - vector3List.get(k).y));
                }
                break;
        }

        switch (coord) {    // wrap-around term
            case 1:
                area += (vector3List.get(n).y * (vector3List.get(1).z - vector3List.get(n - 1).z));
                break;
            case 2:
                area += (vector3List.get(n).z * (vector3List.get(1).x - vector3List.get(n - 1).x));
                break;
            case 3:
                area += (vector3List.get(n).x * (vector3List.get(1).y - vector3List.get(n - 1).y));
                break;
        }

        // scale to get area before projection
        an = (float) Math.sqrt(ax * ax + ay * ay + az * az); // length of normal vector

        switch (coord) {
            case 1:
                area *= (an / (2 * normalVectorOfPlane.x));
                break;
            case 2:
                area *= (an / (2 * normalVectorOfPlane.y));
                break;
            case 3:
                area *= (an / (2 * normalVectorOfPlane.z));
                break;
        }

        return area;
    }


    /**
     * find the 3D intersection of a segment and a plane
     * return 0 = disjoint (no intersection)
     * 1 =  intersection in the unique point intersectVector
     * 2 = the  segment lies in the plane
     */
    public static int calculateIntersectionOfLineAndPlane(Vector3 rayVector, Vector3 rayPoint, Vector3 planeNormal, Vector3 planePoint, Vector3 intersectVector)
    {
        Vector3 u = new Vector3();
        u.x = rayVector.x - rayPoint.x;
        u.y = rayVector.y - rayPoint.y;
        u.z = rayVector.z - rayPoint.z;

        Vector3 w = new Vector3();
        w.x = rayPoint.x - planePoint.x;
        w.y = rayPoint.y - planePoint.y;
        w.z = rayPoint.z - planePoint.z;

        float D = Vector3.dot(planeNormal, u);
        float N = -Vector3.dot(planeNormal, w);

        if (Math.abs(D) < 0.00000001) {
            // segment is parallel to plane
            if (N == 0) {
                // segment lies in plane
                return 2;
            }
            else {
                // no intersection
                return 0;
            }
        }

        // they are not parallel
        // compute intersect param
        float sI = N / D;
        if (sI < 0 || sI > 1) {
            // no intersection
            return 0;
        }

        intersectVector.x = rayPoint.x + sI * u.x;
        intersectVector.y = rayPoint.y + sI * u.y;
        intersectVector.z = rayPoint.z + sI * u.z;

        // compute segment intersect point
        return 1;
    }

    public static boolean checkIsVectorInPolygon(Vector3 p, List<Vector3> poly) {
        float px = p.x;
        float py = p.z;
        boolean flag = false;

        for(int i = 0, l = poly.size(), j = l - 1; i < l; j = i, i++) {
            float sx = poly.get(i).x;
            float sy = poly.get(i).z;
            float tx = poly.get(j).x;
            float ty = poly.get(j).z;

            // vector on polygon's side
            if((sx == px && sy == py) || (tx == px && ty == py)) {
                return false;
            }


            if((sy < py && ty >= py) || (sy >= py && ty < py)) {

                float x = sx + (py - sy) * (tx - sx) / (ty - sy);

                // vector on polygon's side
                if(x == px) {
                    return false;
                }

                if(x > px) {
                    flag = !flag;
                }
            }
        }

        // vector in polygon
        return flag;
    }

    public static boolean checkIsVectorInPolygon(Vector3 p, Vector3 leftBottom, Vector3 rightTop) {

        boolean isIn = false;

        boolean hr = false;
        if(leftBottom.x <= p.x && p.x <= rightTop.x) {
            hr = true;
        }

        boolean vr = false;
        if(leftBottom.y <= p.y && p.y <= rightTop.y) {
            vr = true;
        }

        if(hr && vr) {
            isIn = true;
        }


        boolean hl = false;
        if(leftBottom.x >= p.x && p.x >= rightTop.x) {
            hl = true;
        }

        boolean vl = false;
        if(leftBottom.y <= p.y && p.y <= rightTop.y) {
            vl = true;
        }

        if(hl && vl) {
            isIn = true;
        }


        return isIn;
    }

    public static SFConstants.SFUnit getUnit(String unit) {
        switch (unit) {
            case "m":
                return M;

            case "cm":
                return CM;

            default:
                return M;
        }
    }

    public static String getLengthUnitString(SFConstants.SFUnit sfUnit) {
        switch (sfUnit) {
            case M:
                return "m";

            case CM:
                return "cm";

            default:
                return "";
        }
    }

    public static SpannableString getAreaUnitString(SFConstants.SFUnit sfUnit) {
        switch (sfUnit) {
            case M:
                return getM2();

            case CM:
                return getCM2();

            default:
                return null;
        }
    }

    public static SpannableString getVolumeUnitString(SFConstants.SFUnit sfUnit) {
        switch (sfUnit) {
            case M:
                return getM3();

            case CM:
                return getCM3();

            default:
                return null;
        }
    }

    public static float getLengthByUnit(SFConstants.SFUnit sfUnit, float length) {
        switch (sfUnit) {
            case CM:
                return length * 100;

            case M:
                return length;

            default:
                return 0;
        }
    }

    public static float getHeightByUnit(SFConstants.SFUnit sfUnit, float height) {
        switch (sfUnit) {
            case CM:
                return height * 0.01f;

            case M:
                return height;

            default:
                return 0;
        }
    }

    public static float getAreaByUnit(SFConstants.SFUnit sfUnit, float area) {
        switch (sfUnit) {
            case CM:
                return area * 10000;

            case M:
                return area;

            default:
                return 0;
        }
    }

    public static float getVolumeByUnit(SFConstants.SFUnit sfUnit, float volume) {
        switch (sfUnit) {
            case CM:
                return volume * 1000000;

            case M:
                return volume;

            default:
                return 0;
        }
    }

    public static SpannableString getM2() {
        SpannableString m2 = new SpannableString("m2");
        m2.setSpan(new RelativeSizeSpan(0.5f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        m2.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return m2;
    }

    public static SpannableString getCM2() {
        SpannableString cm2 = new SpannableString("cm2");
        cm2.setSpan(new RelativeSizeSpan(0.5f), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cm2.setSpan(new SuperscriptSpan(), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return cm2;
    }

    public static SpannableString getM3() {
        SpannableString m2 = new SpannableString("m3");
        m2.setSpan(new RelativeSizeSpan(0.5f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        m2.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return m2;
    }

    public static SpannableString getCM3() {
        SpannableString cm2 = new SpannableString("cm3");
        cm2.setSpan(new RelativeSizeSpan(0.5f), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cm2.setSpan(new SuperscriptSpan(), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return cm2;
    }

}
