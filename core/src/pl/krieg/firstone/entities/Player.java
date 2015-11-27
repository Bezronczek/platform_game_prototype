package pl.krieg.firstone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import pl.krieg.firstone.Vars;
import pl.krieg.firstone.managers.Entity;
import pl.krieg.firstone.managers.Level;

import static pl.krieg.firstone.Vars.UNIT;
import static pl.krieg.firstone.Vars.States;

/**
 * Created by Mortis on 2014-10-01.
 */
public class Player extends Entity {

    //player constants go here
    public static final float PLAYER_DIMENSION_WIDTH = UNIT * .9f;
    public static final float PLAYER_DIMENSION_HEIGHT =  UNIT * .9f;
    public static final float PLAYER_MAX_VELOCITY = 3 * UNIT;



    //player variables go here
    private int score;
    private short lives;
    private int ammo;
    private Object[] inventory;
    private Vars.States playerState;
    private boolean hasDoublejump;

    //player methods go here
    public void handleInput() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();


        if (Gdx.input.isKeyPressed(Input.Keys.A) && vel.x > -PLAYER_MAX_VELOCITY){
                body.applyLinearImpulse(-UNIT * 2, 0, pos.x, pos.y, true);
                playerState = States.WALKING;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && vel.x < PLAYER_MAX_VELOCITY) {
            if(!(playerState == States.WALKING && vel.x == 0f)) {
                body.applyLinearImpulse(UNIT * 2, 0, pos.x, pos.y, true);
                playerState = States.WALKING;
            } else {
                playerState = States.IDLE;
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            body.getPosition().set(5 * UNIT, 8 * UNIT);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && vel.y == 0){
            body.applyForceToCenter(0, 400, true);

        }
        /*if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && vel.y < PLAYER_MAX_VELOCITY){
            if(body.getAngularVelocity() < PLAYER_MAX_VELOCITY)
                body.applyForceToCenter(0, 10, true);
        }*/

    }
    public void savePlayer() {} //TODO
    public void loadPlayer() {} //TODO


    //entity methods, update&render player + some constructors
    @Override
    protected void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
//        setPosition(body.getPosition());
  //      setVelocity(body.getLinearVelocity());
        handleInput();
        Level.getInstance().getCollisionManager().checkForCollision(this);
        if(velocity.x == 0 && playerState != States.IDLE){ playerState = States.IDLE; }
    }

    @Override
    protected void dispose() {
        super.dispose();
    }

    public Player(String path, BodyDef bodyDef, FixtureDef fixtureDef, Vector2 spawnPoint) {
        super(path, bodyDef, fixtureDef, spawnPoint);
    }
    public Player(String path, BodyDef bodyDef, FixtureDef fixtureDef) {
        super(path, bodyDef, fixtureDef);
        playerState = States.IDLE;
    }


    public States getPlayerState() {
        return playerState;
    }

}
