package pl.krieg.firstone.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import pl.krieg.firstone.Vars;
import pl.krieg.firstone.managers.Entity;
import pl.krieg.firstone.managers.Level;

/**
 * Created by Mortis on 2014-10-29.
 */
public class TerrainTile extends Entity {

    public boolean dispose = false;

    public TerrainTile(BodyDef bdef, FixtureDef fdef, Vector2 pos){
        super(bdef, fdef, pos);
        this.position = body.getPosition();
        Level.collisionCellMatrix[(int)pos.x][(int) pos.y] = this;
    }

    public void update(Vector2 pos){

        //if player is more than 2 units away, destroy tile

        float v = this.position.x - pos.x;
        float v1 = 2 * Vars.UNIT;

        if((v > v1) || (-v > v1)){
                this.dispose = true;
        }
        v = position.y - pos.y;

        if((v > v1) || (-v > v1)){
            this.dispose = true;
        }

    }

    public void dispose(){
        super.dispose();
        Level.collisionCellMatrix[(int) this.position.x][(int) this.position.y] = null;
        Level.getInstance().getCollisionCells().removeValue(this, true);
    }

}
