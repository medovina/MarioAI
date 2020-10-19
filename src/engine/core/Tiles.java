package engine.core;

import agents.AgentOptions;

/** A {@link Tiles} object provides information about tiles around Mario.
 * <p>
 * In methods in this class, (mapX, mapY) are tile coordinates in the receptive field.
 * In these coordinates, (0,0) is at the upper-left corner of the receptive field.
 * <p>
 * (relMapX, relMapY) are tile coordinates relative to Mario.  In these coordinates, (0, 0) is Mario's position.
 */
public class Tiles {

	private MarioEntity mario;
	
	/**
	 * A 2-dimensional array of tiles (terrain and coins) in the receptive field.
	 * <p>
	 * Tiles are indexed by row, then column: tileField[mapY, mapX] is the tile at (mapX, mapY).
	 * <p>
	 * This array is {@link #receptiveFieldHeight} x {@link #receptiveFieldWidth} in size.
	 */
	public Tile[][] tileField;
	
	public void reset(AgentOptions options) {
		this.mario = options.mario;
	}
	
	/** Return the tile at (mapX, mapY) in the receptive field. */
	public Tile getTile(int mapX, int mapY) {
		if (tileField == null) return Tile.NOTHING;
		if (mapY < 0 || mapY >= tileField.length || mapX < 0 || mapX >= tileField[0].length) return Tile.NOTHING;
		return tileField[mapY][mapX];
	}
	
	/** Return the tile at (relMapX, relMapY) relative to Mario. */
	public Tile tile(int relMapX, int relMapY) {
		return getTile(mario.egoCol + relMapX, mario.egoRow + relMapY);
	}
	
	/** Return true if there is no tile at (relMapX, relMapY) relative to Mario. */
	public boolean emptyTile(int relMapX, int relMapY) {
		return tile(relMapX, relMapY) == Tile.NOTHING;
	}
	
	/** Return true if there is any tile at (relMapX, relMapY) relative to Mario. */
	public boolean anyTile(int relMapX, int relMapY) {
		return !emptyTile(relMapX, relMapY);
	}
	
	/** Return true if (relMapX, relMapY) contains a brick, a flowerpot or a cannon. */
	public boolean brick(int relMapX, int relMapY) {
		Tile tile = tile(relMapX, relMapY); 
		switch (tile) {
		case BORDER_CANNOT_PASS_THROUGH:
		case BREAKABLE_BRICK:
		case CANNON_MUZZLE:
		case CANNON_TRUNK:
		case FLOWER_POT:
		case FLOWER_POT_OR_CANNON:
		case QUESTION_BRICK:
			return true;
			
		default:
			return false;
		}
	}

    /** Return true if any brick overlaps the given bounding box, which is in pixel coordinates
     *  relative to Mario. */
    public boolean brickIn(int x1, int y1, int x2, int y2) {
        int x_start = (int) Math.floor((x1 + mario.inTileX) / 16.0),
            y_start = (int) Math.floor((y1 + mario.inTileY) / 16.0);
        int x_end = (int) Math.floor((x2 + mario.inTileX) / 16.0),
            y_end = (int) Math.floor((y2 + mario.inTileY) / 16.0);

        for (int x = x_start ; x <= x_end ; ++x)
            for (int y = y_start ; y <= y_end ; ++y)
                if (brick(x, y))
                    return true;
        
        return false;
    }
}
