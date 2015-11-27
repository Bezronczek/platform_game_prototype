package pl.krieg.firstone.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import pl.krieg.firstone.Vars;

/**
 * Created by Mortis on 2014-10-15.
 */
public abstract class Entity {

    protected static Entity instance = null;
    protected Vector2 spawnPoint;

    //physics stuff
    protected Body body;
    protected BodyDef bodyDef;
    protected Fixture fixture;
    protected FixtureDef fixtureDef;
    protected Vector2 position;
    protected Vector2 velocity = new Vector2(0,0);

    //model stuff
    protected TextureRegion[] frames;
    protected Animation animation;
    protected Texture texture;
    protected String charSheetPath;

    protected void create() {} //i think that standard constructor may be better for this
    protected void update(float delta){
        setPosition(body.getPosition());
        setVelocity(body.getLinearVelocity());
    }
    protected void render(SpriteBatch sb){}
    protected void dispose(){
        this.body.getWorld().destroyBody(this.body);
    }

    public Entity(String path, BodyDef bodyDef, FixtureDef fixtureDef){
        this.charSheetPath = path;
        this.bodyDef = bodyDef;
        this.fixtureDef = fixtureDef;
        body = Level.getInstance().getWorld().createBody(bodyDef);
        this.fixture = body.createFixture(fixtureDef);
        instance = this;
    }

    public Entity(String path, BodyDef bodyDef, FixtureDef fixtureDef, Vector2 spawnPoint){
        this.charSheetPath = path;
        this.bodyDef = bodyDef;
        this.fixtureDef = fixtureDef;
        bodyDef.position.set(spawnPoint);
        this.body = Level.getInstance().getWorld().createBody(bodyDef);
        this.fixture = this.body.createFixture(fixtureDef);
        instance = this;
    }

    public Entity(BodyDef bodyDef, FixtureDef fixtureDef, Vector2 pos){
        this.bodyDef = bodyDef;
        this.fixtureDef = fixtureDef;
        bodyDef.position.set(pos);
        this.body = Level.getInstance().getWorld().createBody(bodyDef);
        this.fixture = body.createFixture(fixtureDef);
        instance = this;
    }

    public Entity() {
    }

    public Body getBody() {
        return body;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }


    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public static Entity getInstance() {
        return instance;
    }

    public TextureRegion[] getFrames() {
        return frames;
    }

}
