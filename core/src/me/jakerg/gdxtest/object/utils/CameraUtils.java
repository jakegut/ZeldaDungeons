package me.jakerg.gdxtest.object.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import java.awt.*;

public class CameraUtils {
    public static void lerpToTarget(Camera cam, Point p) {
        Vector3 pos = cam.position;
        pos.x = cam.position.x + (p.x - cam.position.x) * 0.05f;
        pos.y = cam.position.y + (p.y - cam.position.y) * 0.05f;
        cam.position.set(pos);
        cam.update();
    }
}
