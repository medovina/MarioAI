package engine.core;

import java.util.ArrayList;
import java.util.List;

import agents.AgentOptions;
import engine.sprites.Sprite;

/** An {@link Entities} object provides information about entities around Mario.
 * <p>
 * In methods in this class, (mapX, mapY) are tile coordinates in the receptive field.
 * In these coordinates, (0,0) is at the upper-left corner of the receptive field.
 * <p>
 * (relMapX, relMapY) are tile coordinates relative to Mario.  In these coordinates, (0, 0) is Mario's position.
 */
public class Entities {

	private MarioEntity mario;
	
	/**
	 * A 2-dimensional array of entities in the receptive field.
	 * <p>
	 * Entities are indexed by row, then column: entityField[mapY, mapX] is the entity at (mapX, mapY).
	 * <p>
	 * Mario is at [marioEgoRow][marioEgoCol].
	 * <p>
	 * The array is {@link #receptiveFieldHeight} x {@link #receptiveFieldWidth} in size.
	 */
	public List<Entity>[][] entityField;
	
	/**
	 * A list of all entities within Mario's receptive field.
	 */
	public List<Entity> allEntities;
	
	public void reset(AgentOptions options) {
		this.mario = options.mario;
	}
	
	/**
	 * Returns the most dangerous entity type at (mapX, mapY).
	 * The most dangerous entity is the one with the lowest {@link EntityKind#getThreatLevel()} .
	 * @param mapX absolute receptive field tile x-coordinate
	 * @param mapY absolute receptive field tile y-coordinate
	 */
	protected EntityType getEntityType(int mapX, int mapY) {
        if (mapY < 0 || mapY >= entityField.length || mapX < 0 || mapX >= entityField[0].length)
            return EntityType.NOTHING;
            
		if (entityField[mapY][mapX].size() > 0) {
			// SEARCH FOR THE MOST DANGEROUS ONE
			Entity result = entityField[mapY][mapX].get(0);
			for (Entity entity : entityField[mapY][mapX]) {
				if (result.type.getKind().getThreatLevel() > entity.type.getKind().getThreatLevel()) result = entity;
			}
			return result.type;
		} else {
			return EntityType.NOTHING;
		}
	}
	
	/**
	 * Return all entities at (mapX, mapY).
	 * @param mapX absolute receptive field tile x-coordinate
	 * @param mapY absolute receptive field tile y-coordinate
	 */
	protected List<Entity> getAllAt(int mapX, int mapY) {
        if (mapY < 0 || mapY >= entityField.length || mapX < 0 || mapX >= entityField[0].length)
            return new ArrayList<Entity>();
		return entityField[mapY][mapX];
	}
	
	/**
	 * Return the most dangerous entity type at (relMapX, relMapY).
	 * The most dangerous entity is the one with the lowest {@link EntityKind#getThreatLevel()}.
	 */
	public EntityType entityType(int relMapX, int relMapY) {
		return getEntityType(mario.egoCol + relMapX,mario.egoRow + relMapY);
	}
	
	/**
	 * Returns all entities at (relMapX, relMapY).
	 */
	public List<Entity> allAt(int relMapX, int relMapY) {
		return getAllAt(mario.egoCol + relMapX, mario.egoRow + relMapY);
	}
	
	/**
	 * Is there any entity at (relMapX, relMapY)?
	 */
	public boolean anything(int relMapX, int relMapY) {
		return allAt(relMapX, relMapY).size() > 0;
	}
	
	/**
	 * Is there no entity at (relMapX, relMapY)?
	 */
	public boolean nothing(int relMapX, int relMapY) {
		return allAt(relMapX, relMapY).size() == 0;
	}
	
	/**
	 * Is there anything dangerous at (relMapX, relMapY)?
	 */
	public boolean danger(int relMapX, int relMapY) {
		if (nothing(relMapX, relMapY)) return false;
		for (Entity entity : allAt(relMapX, relMapY)) {
			if (isDanger(entity)) return true;
		}
		return false;
    }
    
    /* Return true if anything dangerous overlaps the given bounding box,
     * which is in pixel coordinates relative to Mario. */
    public boolean dangerIn(int x1, int y1, int x2, int y2) {
        for (Entity e: allEntities)
            if (Entities.isDanger(e)) {
                Sprite s = e.sprite;
                if (e.dX + s.wPic / 2 >= x1 && e.dX - s.wPic / 2 <= x2 &&
                    e.dY >= y1 && e.dY - s.height <= y2)
                    return true;
            }
        return false;
    }

	/** Return true if this entity is dangerous. */
	public static boolean isDanger(Entity entity) {
		return entity.type.getKind().isDangerous();
	}
	
	/**
	 * Return true if Mario can squish something at (relMapX, relMapY).
	 * Note that this will return FALSE if there is non-squishable dangerous stuff there.
	 */
	public boolean squishy(int relMapX, int relMapY) {
		if (nothing(relMapX, relMapY)) return false;
		boolean result = false;
		for (Entity entity : allAt(relMapX, relMapY)) {
			if (!isSquishy(entity) && isDanger(entity)) return false;
			if (isSquishy(entity)) {
				result = true;
			}
		}	
		return result;
	}
	
	/**
	 * Is this entity squishable by jumping on it (== destroyable)?
	 */
	public static boolean isSquishy(Entity entity) {
		return entity.type.getKind().isSquishy();
	}
	
	/**
	 * Can I shoot something at (relMapX, relMapY)?
	 * <p>
	 * If the tile is occupied by both shootable and non-shootable entities,
	 * this will return FALSE (to play safe).
	 */
	public boolean shootable(int relMapX, int relMapY) {
		if (nothing(relMapX, relMapY)) return false;		
		boolean result = false;
		for (Entity entity : allAt(relMapX, relMapY)) {
			if (!isShootable(entity)) return false;
			result = true;
		}	
		return result;
	}
	
	/**
	 * Is this entity shootable by fireball?
	 */
	public static boolean isShootable(Entity entity) {
		return entity.type.getKind().isShootable();
	}
	
	/**
	 * Can I collect something at (relMapX, relMapY)?
	 * <p>
	 * This will only return true if there is also nothing dangerous at that position.
	 */
	public boolean collectible(int relMapX, int relMapY) {
		if (nothing(relMapX, relMapY)) return false;
		if (danger(relMapX, relMapY)) return false;
		for (Entity entity : allAt(relMapX, relMapY)) {
			if (isCollectible(entity)) return true;
		}		
		return false;
	}
	
	/**
	 * Is this entity collectible?
	 */
	public static boolean isCollectible(Entity entity) {
		return entity.type.getKind().isCollectible();
	}
	
}
