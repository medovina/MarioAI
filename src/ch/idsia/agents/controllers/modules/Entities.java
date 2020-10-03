package ch.idsia.agents.controllers.modules;

import java.util.ArrayList;
import java.util.List;

import ch.idsia.agents.AgentOptions;
import ch.idsia.benchmark.mario.engine.generalization.Entity;
import ch.idsia.benchmark.mario.engine.generalization.EntityKind;
import ch.idsia.benchmark.mario.engine.generalization.EntityType;
import ch.idsia.benchmark.mario.engine.generalization.MarioEntity;

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
	public List<Entity> entities;
	
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
		if (mapY < 0 || mapY >= entityField.length || mapX < 0 || mapX >= entityField[0].length) return EntityType.NOTHING;
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
	protected List<Entity> getEntities(int mapX, int mapY) {
		if (entityField == null) return new ArrayList<Entity>();
		if (mapY < 0 || mapY >= entityField.length || mapX < 0 || mapX >= entityField[0].length) return new ArrayList<Entity>();
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
	public List<Entity> entities(int relMapX, int relMapY) {
		return getEntities(mario.egoCol + relMapX, mario.egoRow + relMapY);
	}
	
	/**
	 * Is there any entity at (relMapX, relMapY)?
	 */
	public boolean anything(int relMapX, int relMapY) {
		return entities(relMapX, relMapY).size() > 0;
	}
	
	/**
	 * Is there no entity at (relMapX, relMapY)?
	 */
	public boolean nothing(int relMapX, int relMapY) {
		return entities(relMapX, relMapY).size() == 0;
	}
	
	/**
	 * Is there anything dangerous at (relMapX, relMapY)?
	 */
	public boolean danger(int relMapX, int relMapY) {
		if (nothing(relMapX, relMapY)) return false;
		for (Entity entity : entities(relMapX, relMapY)) {
			if (isDanger(entity)) return true;
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
		for (Entity entity : entities(relMapX, relMapY)) {
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
		for (Entity entity : entities(relMapX, relMapY)) {
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
		for (Entity entity : entities(relMapX, relMapY)) {
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
