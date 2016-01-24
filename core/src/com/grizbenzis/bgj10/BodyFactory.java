package com.grizbenzis.bgj10;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by sponaas on 1/22/16.
 */
public class BodyFactory {

    private static BodyFactory _instance = null;
    private World _world;

    private BodyFactory(World world) {
        _world = world;
    }

    public static void initialize(World world) {
        _instance = new BodyFactory(world);
    }

    public static BodyFactory getInstance() {
        return _instance;
    }

    public Body generate(Entity owner, String definitionPath, Vector2 positionInMeters) {
        JsonValue jsonRoot = Utils.readJsonFromFile(definitionPath);

        JsonValue jsonBody = jsonRoot.get("BodyDef");
        BodyDef bodyDef = new BodyDef();

        bodyDef.fixedRotation = false;
        bodyDef.gravityScale = 0f;
        float dimensionX = jsonBody.getFloat("dimensionX");
        float dimensionY = jsonBody.getFloat("dimensionY");

        String collisionType = jsonBody.getString("collisionType");
        short categoryBits = 0;
        if (collisionType.equalsIgnoreCase("PLAYER"))
            categoryBits = Constants.BITMASK_PLAYER;
        else if (collisionType.equalsIgnoreCase("ENEMY"))
            categoryBits = Constants.BITMASK_ENEMY;
        else if (collisionType.equalsIgnoreCase("PLAYER_BULLET"))
            categoryBits = Constants.BITMASK_PLAYER_BULLET;
        else if (collisionType.equalsIgnoreCase("POWERUP"))
            categoryBits = Constants.BITMASK_POWERUP;
        else if (collisionType.equalsIgnoreCase("EXPLOSION"))
            categoryBits = Constants.BITMASK_EXPLOSION;
        else if(collisionType.equalsIgnoreCase("BLACK_HOLE"))
            categoryBits = Constants.BITMASK_BLACK_HOLE;
        else if(collisionType.equalsIgnoreCase("ALIEN")) {
            categoryBits = Constants.BITMASK_ALIEN;
            bodyDef.fixedRotation = true; // ************************* HACK *************************
        }
        else
            Gdx.app.log("WARNING", "Entity Box2D collision type undefined - " + collisionType);

        short maskingBits = 0;
        if(Constants.BITMASK_PLAYER_BULLET == categoryBits)
            maskingBits = Constants.BITMASK_ENEMY | Constants.BITMASK_LEVEL_BOUNDS | Constants.BITMASK_POWERUP | Constants.BITMASK_ALIEN;
        else if(Constants.BITMASK_ENEMY == categoryBits)
            maskingBits = Constants.BITMASK_PLAYER | Constants.BITMASK_PLAYER_BULLET | Constants.BITMASK_ENEMY | Constants.BITMASK_EXPLOSION;
        else if(Constants.BITMASK_PLAYER == categoryBits)
            maskingBits = Constants.BITMASK_ENEMY | Constants.BITMASK_POWERUP | Constants.BITMASK_ALIEN;
        else if(Constants.BITMASK_POWERUP == categoryBits)
            maskingBits = Constants.BITMASK_PLAYER | Constants.BITMASK_PLAYER_BULLET | Constants.BITMASK_EXPLOSION;
        else if(Constants.BITMASK_EXPLOSION == categoryBits)
            maskingBits = Constants.BITMASK_ENEMY | Constants.BITMASK_POWERUP | Constants.BITMASK_ALIEN;
        else if(Constants.BITMASK_BLACK_HOLE == categoryBits)
            maskingBits = Constants.BITMASK_PLAYER;
        else if(Constants.BITMASK_ALIEN == categoryBits)
            maskingBits = Constants.BITMASK_PLAYER | Constants.BITMASK_PLAYER_BULLET | Constants.BITMASK_EXPLOSION;
        else
            Gdx.app.log("WARNING", "Entity Box2D masking bits undefined - " + categoryBits);

        String bodyType = jsonBody.getString("type");
        if (bodyType.equalsIgnoreCase("DynamicBody"))
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        else if (bodyType.equalsIgnoreCase("KinematicBody"))
            bodyDef.type = BodyDef.BodyType.KinematicBody;
        else if (bodyType.equalsIgnoreCase("StaticBody"))
            bodyDef.type = BodyDef.BodyType.StaticBody;
        else
            Gdx.app.log("WARNING", "Entity Box2D body type undefined - " + bodyType);

        // NOTE: position is in meters but dimension is in pixels
        bodyDef.position.set(positionInMeters.x + (dimensionX / 2f * Constants.PIXELS_TO_METERS),
                positionInMeters.y + (dimensionY / 2f * Constants.PIXELS_TO_METERS));
        Body retval = _world.createBody( bodyDef );
        if(Constants.BITMASK_PLAYER_BULLET == categoryBits)
            retval.setBullet(true);
        else
            retval.setBullet(false);

        for(JsonValue jsonFixture : jsonRoot.get("Fixtures")) {
            String fixtureType = jsonFixture.getString("type");
            Shape shape;
            if(fixtureType.equalsIgnoreCase("RectangleShape")) {
                shape = new PolygonShape();
                Vector2 position = new Vector2((retval.getLocalCenter().x + jsonFixture.getFloat("x")) * Constants.PIXELS_TO_METERS,
                        (retval.getLocalCenter().y + jsonFixture.getFloat( "y")) * Constants.PIXELS_TO_METERS );
                ((PolygonShape)shape).setAsBox(jsonFixture.getFloat("width") * Constants.PIXELS_TO_METERS,
                        jsonFixture.getFloat("height") * Constants.PIXELS_TO_METERS,
                        position, 0f);
            }
            else if(fixtureType.equalsIgnoreCase("CircleShape")) {
                shape = new CircleShape();
                Vector2 position = new Vector2((retval.getLocalCenter().x + jsonFixture.getFloat("x")) * Constants.PIXELS_TO_METERS,
                        (retval.getLocalCenter().y + jsonFixture.getFloat( "y")) * Constants.PIXELS_TO_METERS);
                shape.setRadius(jsonFixture.getFloat("radius") * Constants.PIXELS_TO_METERS);
                ((CircleShape)shape).setPosition(position);
            }
            else {
                Gdx.app.log("WARNING", "Generated body shape was invalid");
                continue;
            }

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = jsonFixture.getBoolean("isSensor");
            fixtureDef.density = jsonFixture.getFloat("density");
            fixtureDef.friction = 0f;
            if(jsonFixture.has("restitution"))
                fixtureDef.restitution = jsonFixture.getFloat("restitution");
            fixtureDef.filter.categoryBits = categoryBits;
            fixtureDef.filter.maskBits = maskingBits;
            retval.createFixture(fixtureDef).setUserData(owner);
            shape.dispose();
        }
        return retval;
    }
}
