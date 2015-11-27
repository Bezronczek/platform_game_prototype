package pl.krieg.firstone.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

import pl.krieg.firstone.Vars;
import pl.krieg.firstone.entities.NPC;
import pl.krieg.firstone.entities.TerrainTile;
import pl.krieg.firstone.entities.Player;

import static pl.krieg.firstone.Vars.MAP_LAYER_COLLISION;
import static pl.krieg.firstone.Vars.UNIT;
import static pl.krieg.firstone.Vars.WORLD_GRAVITY;

/**
 * Created by Mortis on 2014-10-15.
 */
public class Level {

    private static Level instance = null;

    //standard map stuff, tiled map + b2d world
    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private World b2dWorld;
    private Vector2 layerDimensions;
    private CollisionManager collisionManager;

    //topkek, layer tag + layer? maybe, leaving for now
    private HashMap<String, TiledMapTileLayer> layers;
    private MapObjects objects;

    //cells around the player
    private Array<TerrainTile> collisionCells = new Array<TerrainTile>(); //FIXME move this part to npc/player class
    public static TerrainTile[][] collisionCellMatrix;

    //table for NPCs, for future usage; Player&camera = standard stuff
    private Array<Entity> entities;
    private Player player;
    private OrthographicCamera cam;

    //debug stuff, delete this in _final rev
    //
    public boolean renderTiledMap = true;
    public boolean renderCollisionLayer = false;
    public boolean renderDebugB2D = true;
    public boolean renderTerrain = true;
    public int bodiesCount;
    private Box2DDebugRenderer b2dDebugRenderer;
    private BitmapFont font;


    public Level(String path, OrthographicCamera cam) {
        instance = this;
        b2dWorld = new World(new Vector2(0, WORLD_GRAVITY), true);
        b2dDebugRenderer = new Box2DDebugRenderer();
        this.cam = cam;
        map = new TmxMapLoader().load(path);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, Vars.MAP_TILED_SCALE);

        layers = createLayers();
        //createTerrain();
        entities = createEntities();
        collisionManager = new CollisionManager(layers.get(MAP_LAYER_COLLISION));

        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setColor(Color.RED);


        //generate terrain
       /* for(int x = 0; x <= layerDimensions.x; x++){
            for(int y = 0; y <= layerDimensions.y; y++)
                if(layers.get(MAP_LAYER_COLLISION).getCell(x,y) != null){
                    collisionManager.createCollisionTile(x, y);
                }
        }*/

    }

    protected Level(){}

    public void update(float dt){
       for(Entity e : entities){
            e.update(dt);
        }
        //cam.position.set(player.getPosition(),0);
        //cam.update();
    }

    public void render(SpriteBatch sb) {
        tiledMapRenderer.setView(cam);

        //debug stuff
        if(renderTiledMap) //FIXME this cannot be here in _final
            tiledMapRenderer.render();
        layers.get(Vars.MAP_LAYER_COLLISION).setVisible(renderCollisionLayer);
        layers.get(Vars.MAP_LAYER_TERRAIN).setVisible(renderTerrain);



        sb.begin();
//        for(Entity e : entities){
//            e.render(sb);
//        }
        if(renderDebugB2D){
            b2dDebugRenderer.render(b2dWorld, cam.combined);}


        sb.end();

        b2dWorld.step(1/60f, 6, 2);
        bodiesCount = b2dWorld.getBodyCount();

        for(TerrainTile tile : collisionCells) {
            tile.update(player.getPosition());
            if(tile.dispose)
                tile.dispose();
        }
    }

    private HashMap<String, TiledMapTileLayer> createLayers() {
        layers = new HashMap<String, TiledMapTileLayer>();
        layers.put(Vars.MAP_LAYER_BACKGROUND, (TiledMapTileLayer) map.getLayers().get(Vars.MAP_LAYER_BACKGROUND));
        layers.put(Vars.MAP_LAYER_TERRAIN, (TiledMapTileLayer) map.getLayers().get(Vars.MAP_LAYER_TERRAIN));
        layers.put(Vars.MAP_LAYER_COLLISION, (TiledMapTileLayer) map.getLayers().get(Vars.MAP_LAYER_COLLISION));
        objects = new MapObjects();

        layerDimensions = new Vector2(layers.get(Vars.MAP_LAYER_COLLISION).getWidth(), layers.get(Vars.MAP_LAYER_COLLISION).getHeight());
        collisionCellMatrix = new TerrainTile[(int)layerDimensions.x][(int)layerDimensions.y];

        return layers;
    }

    private Array<Entity> createEntities() {
        entities = new Array<Entity>();

        createPlayer();

        return entities;
    }

    private Player createPlayer() {
        BodyDef bdef = new BodyDef();
        FixtureDef bfix = new FixtureDef();
        PolygonShape playerBox = new PolygonShape();

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(5 * UNIT , 8 * UNIT);
        bdef.fixedRotation = true;
        playerBox.setAsBox(Player.PLAYER_DIMENSION_WIDTH / 2, Player.PLAYER_DIMENSION_HEIGHT / 2);

        bfix.shape = playerBox;
        bfix.density = .8f;
        bfix.friction = 1f;
        bfix.restitution = 0f;

        player = new Player("sprites/player.png", bdef, bfix);
        player.setPosition(new Vector2(5 * UNIT, 8 * UNIT));

        entities.add(player);
        return player;
    }

    public void dispose(){
        for(Entity e : entities){
            e.dispose();
       }
       map.dispose();
       b2dWorld.dispose();
    }

    public World getWorld() { return b2dWorld; }
    public TiledMap getMap(){ return this.map; }
    public HashMap<String, TiledMapTileLayer> getLayers() { return this.layers; }
    public Vector2 getLayerDimensions() { return this.layerDimensions; }
    public Array<TerrainTile> getCollisionCells() {return collisionCells;}
    public CollisionManager getCollisionManager() { return this.collisionManager; }
    public Player getPlayer() {return this.player;}
    public static Level getInstance() {

        if(instance == null){
            instance = new Level();
        }

        return instance;

    }

}
