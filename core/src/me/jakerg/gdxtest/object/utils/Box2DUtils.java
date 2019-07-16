package me.jakerg.gdxtest.object.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Box2DUtils {

    public static final float PPM = 16;

    public static Body createDynRectBody(World world, Sprite sprite) {
        return createDynRectBody(world, sprite, 0);
    }

    public static Body createDynRectBody(World world, Sprite sprite, float dif) {
        return createRectBody(world, sprite.getX(), sprite.getY(), sprite.getWidth() - dif, sprite.getHeight() - dif, BodyType.DynamicBody);
    }

    public static Body createKineRectBody(World world, float x, float y, float width, float height) {
        return createRectBody(world, x, y, width, height, BodyType.KinematicBody);
    }

    public static Body createStaticRectBody(World world, float x, float y, float width, float height) {
        return createRectBody(world, x, y, width, height, BodyType.StaticBody);
    }

    private static Body createRectBody(World world, float x, float y, float width, float height, BodyType type) {
        BodyDef body = new BodyDef();
        body.type = type;
        body.position.set(x / PPM, y / PPM);

        Body b = world.createBody(body);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / PPM / 2, height / PPM / 2);

        b.createFixture(shape, 0.0f);
        shape.dispose();
        return b;
    }

    public static Body createCircBody(World world, float x, float y, float radius) {
        BodyDef body = new BodyDef();
        body.type = BodyType.DynamicBody;
        body.position.set(x / PPM, y / PPM);

        Body b = world.createBody(body);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        b.createFixture(shape, 0.0f);
        shape.dispose();
        return b;
    }

    public static void setSpritePosition(Sprite sprite, Body body) {
//		System.out.println();
//		System.out.println(sprite.getX());
//		System.out.println(sprite.getY());
        sprite.setX(body.getPosition().x * PPM - sprite.getWidth() / 2);
        sprite.setY(body.getPosition().y * PPM - sprite.getHeight() / 2);
        sprite.setRotation((float) (body.getAngle() * (180 / Math.PI)));
    }
}
