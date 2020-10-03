package ch.idsia.benchmark.mario.engine.generalization;

public enum Tile {
	/**
	 * A cannon muzzle.
	 * <p>
	 * ZLevel: 0 only 
	 */
	CANNON_MUZZLE("CM", -82, 0),
	
	/**
	 * A cannon trunk.
	 * <p>
	 * ZLevel: 0 only 
	 */
	CANNON_TRUNK("CT", -80, 0),
	
	/**
	 * A coin that Mario can collect.
	 * <p>
	 * ZLevel: 0, 1 and 2
	 */
	COIN_ANIM("C", 2, 0, 1, 2),
	
	/**
	 * A breakable brick.
	 * <p>
	 * ZLevel: 0 only
	 * <p>
	 * Can be:
	 * <ul>
	 * <li> simple brick without any surprise
     * <li> brick with a hidden coin
	 * <li> brick with a hidden friendly flower
	 * </ul>
	 */
	BREAKABLE_BRICK("BB", -20, 0),
	
	/**
	 * A question brick.
	 * <p>
	 * ZLevel: 0 only
	 * <p>
	 * Can be:
	 * <ul>
	 * <li> question brick containing a coin
	 * <li> question brick containing a flower/mushroom
	 * <li> question brick containing 0-N coins
	 * </ul>
	 */
	QUESTION_BRICK("BQ", -22, 0),
	
	/**
	 * A brick of some form.
	 * <p>
	 * ZLevel: 1 only
	 * <p>
	 * Can be:
	 * <ul>
	 * <li> simple brick without any surprise
	 * <li> brick with a hidden coin
	 * <li> brick with a hidden flower
	 * <li> question brick containing a coin
	 * <li> question brick containing a flower/mushroom
	 * <li> question brick containing 0-N coins
	 * </ul>
	 */
	BRICK("B", -24, 1),
	
	/**
	 * A flower pot.
	 * <p>
	 * ZLevel: 0 only
	 */
	FLOWER_POT("FP", -90, 0),
	
	/**
	 * A solid block.
	 * <p>
	 * ZLevel: 0, 1, 2
	 */
	BORDER_CANNOT_PASS_THROUGH("BI", -60, 0, 1, 2),
	
	/**
	 * A hill block.
	 * <p>
	 * ZLevel: 0, 1
	 * <p>
	 * This is a block that you can stand on, but can also jump through (when jumping up) but not fall through.
	 */
	BORDER_HILL("BH", -62, 0, 1),
	
	/**
	 * A flower pot or a cannon.
	 * <p>
	 * ZLevel: 1 only
	 * 
	 */
	FLOWER_POT_OR_CANNON("PC", -85, 1),
	
	/**
	 * A ladder block (but not its top).
	 * <p>
	 * ZLevel: 0, 1
	 */
	LADDER("L", 61, 0, 1),
	
	/**
	 * The top of a ladder.
	 * <p>
	 * ZLevel: 0, 1
	 * <p>
	 * You cannot climb up any more than this.
	 */
	TOP_OF_LADDER("TL", 61, 0, 1),
	
	/**
	 * The princess.
	 * <p>
	 * ZLevel: 0, 1, 2
	 * <p>
	 * Congratulations: you will win if you reach her!
	 */
	PRINCESS("P", 5, 1, 2),

	/**
	 * Nothing.
	 * <p>
	 * ZLevel: 2 only
	 * <p>
	 * You may pass through this (if it's not a hidden block!)
	 */
	NOTHING("", 0, 2),
	
	/**
	 * Everything else...
	 * <p>
	 * ZLevel: 2 only
	 */
	SOMETHING("S", 1, 2);
		
	private String debug;
	
	private int code;

	private int[] zLevels;
	
	private Tile(String debug, int code, int... zLevels) {
		this.debug = debug;
		this.code = code;
		this.zLevels = zLevels;
	}

	public int getCode() {
		return code;
	}
	
	public int[] getZLevels() {
		return zLevels;
	}
	
	public boolean isZLevel(int zLevel) {
		for (int level : zLevels) {
			if (zLevel == level) return true;
		}
		return false;
	}

	public String getDebug() {
		return debug;
	}
	
}
