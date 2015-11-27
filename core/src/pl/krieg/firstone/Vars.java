package pl.krieg.firstone;

/**
 * Created by Mortis on 2014-10-28.
 */
public class Vars {
    //Pixels Per Meter ratio
    public static final float PPM = 32f;
    public static final float MAP_TILED_SCALE = 1 / PPM;
    public static final float UNIT = 1f;
    public static final int MAP_TILE_SIZE = (int) UNIT;

    //World gravity
    public static final float WORLD_GRAVITY = -9.81f;

    //map layers
    public static final String MAP_LAYER_BACKGROUND = "bg";
    public static final String MAP_LAYER_TERRAIN = "ground";
    public static final String MAP_LAYER_COLLISION = "collisions";

    public static enum States {
        IDLE, WALKING, JUMPING
    }

}
