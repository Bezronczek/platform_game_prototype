package pl.krieg.firstone.managers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import pl.krieg.firstone.Vars;
import pl.krieg.firstone.entities.TerrainTile;

import static pl.krieg.firstone.Vars.MAP_TILE_SIZE;

/**
 * Created by Mortis on 2014-10-29.
 */
public class CollisionManager {

    private TiledMapTileLayer collisionLayer;
    private CollisionManager instance = null;

    public CollisionManager(TiledMapTileLayer collisionLayer){
        instance = this;
        this.collisionLayer = collisionLayer;
    }

    public void createCollisionTile(int x, int y){
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape tile = new PolygonShape();
        ChainShape cs = new ChainShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((float) x + 0.5f ,(float) y + 0.5f);
        tile.setAsBox((float) MAP_TILE_SIZE / 2, (float) MAP_TILE_SIZE / 2 );

        fdef.shape = tile;
        fdef.density = 1.0f;
        fdef.friction = .8f;
        TerrainTile tileInstance = new TerrainTile(bdef, fdef, bdef.position);
        Level.getInstance().getCollisionCells().add(tileInstance);
        Level.collisionCellMatrix[x][y] = tileInstance;
    }

    public void checkForCollision(Entity entity){
        int x = (int) entity.position.x;
        int y = (int) entity.position.y;
        //top left -1, 1
        if((isCellBlocked(x - 1, y + 1))  && (Level.collisionCellMatrix[x - 1][y + 1] == null))
            createCollisionTile(x - 1 , y + 1);

        //top center
        if((isCellBlocked(x, y + 1))  && (Level.collisionCellMatrix[x][y + 1] == null))
            createCollisionTile(x , y + 1);

        //top right
        if((isCellBlocked(x + 1, y + 1))  && (Level.collisionCellMatrix[x +1 ][y + 1] == null))
            createCollisionTile(x + 1 , y + 1);

        //middle left
        if((isCellBlocked(x - 1, y))  && (Level.collisionCellMatrix[x - 1][y] == null))
            createCollisionTile(x - 1 , y);

        //middle right
        if((isCellBlocked(x + 1, y )) && (Level.collisionCellMatrix[x + 1][y] == null))
            createCollisionTile(x + 1 , y);

        //bottom left
        if((isCellBlocked(x - 1, y - 1)) && (Level.collisionCellMatrix[x - 1][y - 1] == null))
            createCollisionTile(x - 1 , y - 1);

        //bottom center
        if((isCellBlocked(x, y - 1)) && (Level.collisionCellMatrix[x][y-1] == null))
            createCollisionTile(x, y - 1);

        //bottom right
        if((isCellBlocked(x + 1, y - 1)) && (Level.collisionCellMatrix[x + 1][y - 1] == null))
            createCollisionTile(x + 1 , y - 1);

    }

    private boolean isCellBlocked(int x, int y){
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
        if(cell == null) return false;
        if(cell.getTile() == null) return false;
        return true;
    }

}
