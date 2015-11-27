package pl.krieg.firstone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import pl.krieg.firstone.entities.Player;
import pl.krieg.firstone.managers.Level;
import sun.rmi.runtime.Log;

import static pl.krieg.firstone.Vars.PPM;
import static pl.krieg.firstone.Vars.UNIT;

public class GameMain extends Game {

    private static GameMain instance = null;

	SpriteBatch batch;
    OrthographicCamera camera;
    Game game;
    Level level;
    boolean debug = true;
    public static BitmapFont font;

    private float screenWidth, screenHeight;

	@Override
	public void create () {
        instance = this;
        screenWidth = Gdx.graphics.getWidth() / PPM;
        screenHeight = Gdx.graphics.getHeight() / PPM;
		batch = new SpriteBatch();
        camera = new OrthographicCamera(screenWidth , screenHeight);
        camera.position.set(screenWidth / 2, screenHeight /2 , 0);
        camera.update();
        level = new Level("map2.tmx", camera);

        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setColor(Color.RED);
        //collisionLayer = (TiledMapTileLayer) level.getMap().getLayers().get(2);
        //tmpLayer = (TiledMapTileLayer) level.getMap().getLayers().get(0);

	}

    public void update() {

        handleInput();
        level.update(Gdx.graphics.getDeltaTime());
    }

    private void handleInput() {



        //TODO temporary disabled up/down keys
        /*if(Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.position.y += 2f;}
        else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.position.y -= 2f;}*/

        //toggle collision layer debug view
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && debug) {
            level.renderCollisionLayer ^= true;
        }



        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            level.renderTiledMap ^= true;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            level.renderDebugB2D ^= true;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
            level.renderTerrain ^= true;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
            level.dispose();
            level = new Level("map2.tmx", camera);
        }




    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update();
        level.render(batch);
        batch.begin();
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond() + " Bodies: " + level.getWorld().getBodyCount(), 0, Gdx.graphics.getHeight() - 30);
        font.draw(batch, "Player state: " + level.getPlayer().getPlayerState() + " Velocity: " + level.getPlayer().getVelocity(), 0, 20);
        batch.end();
	}

    @Override
    public void dispose() {
        font.dispose();
        level.dispose();
        super.dispose();
    }

    public float getSceenWidth(){
        return screenWidth;
    }

    public float getScreenHeight(){
        return screenHeight;
    }

    public static GameMain getInstance(){
        return instance;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

}
