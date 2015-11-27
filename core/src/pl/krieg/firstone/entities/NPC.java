package pl.krieg.firstone.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import pl.krieg.firstone.managers.Entity;
import pl.krieg.firstone.managers.Level;

/**
 * Created by Mortis on 2014-10-29.
 */
public class NPC extends Entity {

    public NPC(BodyDef bdef, FixtureDef fdef, Vector2 pos){
        super(bdef, fdef, pos);

    }

    public void update(float dt){
        super.update(dt);
        Level.getInstance().getCollisionManager().checkForCollision(this);
    }
}
