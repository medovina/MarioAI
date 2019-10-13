package ch.idsia.benchmark.mario.engine.generalization;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;

/**
 * Holds information about one entity.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class Entity {

	/**
	 * A speed in pixels per tick.
	 */
	public static class Speed {
		
		public float x;
		
		public float y;
		
		public Speed(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public boolean isZero() {
			return Math.abs(x - 0.001) < 0 && Math.abs(y - 0.001) < 0;
		}
		
	}
	
	/**
	 * Type of the entity.
	 */
	public EntityType type;
	
	/**
	 * Delta Tile-X position relative to Mario's Tile-X
	 * <br/><br/>
	 */
	public int dTX;
	
	/**
	 * Delta Tile-Y position relative to Mario's Tile-Y
	 * <br/><br/>
	 */
	public int dTY;
	
	/**
	 * Delta Pixel-X position relative to Mario's Pixel-X
	 * <br/><br/>
	 */
	public float dX;
	
	/**
	 * Delta Pixel-Y position relative to Mario's Pixel-Y
	 * <br/><br/>
	 */
	public float dY;
	
	/**
	 * Current absolute Y-position in pixel coordinates.
	 */
	public float height;
	
	/**
	 * Current speed in pixels per tick.
	 */
	public Speed speed;	
	
	/**
	 * Source sprite.
	 */
	public Sprite sprite;

	public Entity(Sprite sprite, EntityType entityType, int dTX, int dTY, float dX, float dY, float height) {
		this.sprite = sprite;
		this.type = entityType;
		this.dTX = dTX;
		this.dTY = dTY;
		this.dX = dX;
		this.dY = dY;
		this.height = height;
		if (sprite == null) {
			this.speed = new Speed(0,0);
		} else {
			this.speed = new Speed(sprite.xa, sprite.ya);
		}
	}
	
}
