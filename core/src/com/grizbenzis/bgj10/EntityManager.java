package com.grizbenzis.bgj10;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.grizbenzis.bgj10.actors.Actor;
import com.grizbenzis.bgj10.components.BodyComponent;
import com.grizbenzis.bgj10.components.EnemyDataComponent;

/**
 * Created by sponaas on 1/22/16.
 */
public class EntityManager {

    private ComponentMapper<EnemyDataComponent> _enemyDataComponents = ComponentMapper.getFor(EnemyDataComponent.class);

    private static EntityManager _instance;
    private Engine _engine;
    private World _world;
    private static Array<Entity> _toDestroy;
    private static Array<Actor> _actors;

    private EntityManager(Engine engine, World world) {
        _engine = engine;
        _world = world;
        _toDestroy = new Array<Entity>();
        _actors = new Array<Actor>();
    }

    public static void initialize(Engine engine, World world) {
        if(null != _instance)
            _instance.dispose();
        _instance = new EntityManager(engine, world);
    }

    public static EntityManager getInstance() {
        return _instance;
    }

    public void dispose() { // TODO: since i'm keeping the scope of this class pretty small, i dont think we're actually gunna need this
        _instance = null;
    }

    public void addEntity(Entity entity) {
        if(_enemyDataComponents.has(entity))
            GameState.getInstance().addEnemy();
        _engine.addEntity(entity);
    }

    public void destroyEntity(Entity entity) {
        if(_enemyDataComponents.has(entity))
            GameState.getInstance().removeEnemy();
        _toDestroy.add(entity);
    }

    public void addActor(Actor actor) {
        _actors.add(actor);
    }

    public void removeActor(Actor actor) {
        _actors.removeValue(actor, true);
    }

    public void update() {
        for(Entity entity : _toDestroy) {
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
            if((null != bodyComponent) && (null != bodyComponent.body)) {
                _world.destroyBody(bodyComponent.body);
                bodyComponent.body = null;
            }
            _engine.removeEntity(entity);
        }
        _toDestroy.clear();
        for(Actor actor: _actors) {
            actor.update();
        }
    }

}
