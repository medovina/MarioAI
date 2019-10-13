package ch.idsia.benchmark.mario.engine.generalization;

import ch.idsia.benchmark.mario.engine.sprites.Mario.MarioMode;

public class MarioEntity extends Entity {
	
	/**
	 * Width of the receptive field (typically 19).
	 */
	public int receptiveFieldWidth;
	
	/**
	 * Height of the receptive field (typically 19).
	 */
	public int receptiveFieldHeight;
		
	/**
	 * The generalization level (0, 1, or 2) for reporting tiles.
	 */
	public int zLevelTiles;
	
	/**
	 * The generalization level (0, 1, or 2) for reporting entities.
	 */
	public int zLevelEntities;

	/**
	 * Where Mario is positioned within the receptive field. Does not change at runtime.
	 * Usual value is 9 for a receptive field of size 19x19.
	 */
	public int egoRow;
	
	/**
	 * Where Mario is positioned within the receptive field. Does not change at runtime.
	 * Usual value is 9 for a receptive field of size 19x19.
	 */
	public int egoCol;
		
	/**
	 * Array filled with data about Mario.
	 * <ul>
	 * <li>    marioState[0]  = this.status;
	 * <li>    marioState[1]  = this.mode.getCode();
	 * <li>    marioState[2]  = this.onGround ? 1 : 0; 
	 * <li>    marioState[3]  = this.mayJump ? 1 : 0; 
	 * <li>    marioState[4]  = this.mayShoot ? 1 : 0; 
	 * <li>    marioState[5]  = this.carrying ? 1 : 0; 
	 * <li>    marioState[6]  = this.killsTotal; 
	 * <li>    marioState[7]  = this.killsByFire;
	 * <li>    marioState[8]  = this.killsByStomp;
	 * <li>    marioState[9]  = this.getKillsByStomp(); 
	 * <li>    marioState[10] = this.killsByShell;
	 * <li>    marioState[11] = this.timeLeft;
	 * </ul>        
	 * READ-ONLY
	 */
	public int[] state = new int[11];
	
	/** Either STATUS_RUNNING (game is in play), STATUS_WIN (you have won),
	 *  or STATUS_DEAD (you have lost). */
	public int status;
	
	/** Mario's current form: small, super (large), or fire. */
	public MarioMode mode;
	
	/** True if Mario is on the ground; false if he is in the air. */
	public boolean onGround;
	
	/**
	 * True if Mario may currently jump (note that if you press the JUMP key while !mayJump,
	 * Mario won't jump and will be stuck in place!)
	 */
	public boolean mayJump;

	/** True if Mario is carrying a shell. */
	public boolean carrying;

	/** True if Mario may currently shoot: he is in fire mode and there are
	 *  not already 2 fireballs on the screen. */
	public boolean mayShoot;
		
  /** Total number of creatures killed. */
	public int killsTotal;

	/** Number of creatures Mario has killed by shooting them. */
	public int killsByFire;

	/** Number of creatures Mario has killed by stepping on them. */
	public int killsByStomp;

	/** Number of creatures Mario has killed by kicking them with a shell. */
	public int killsByShell;	
	
	/** Remaining time in marioseconds. */
	public int timeLeft;

	/** Elapsed time in marioseconds. */
	public int timeSpent;
	
	/**
	 * X-position of Mario within his tile.
	 */
	public int inTileX;
	
	/**
	 * Y-position of Mario within his tile.
	 */
	public int inTileY;
	
	/**
	 * Whether Mario is currently jumping (moving UP).
	 * @return
	 */
	public boolean isJumping() {
		return speed.y < 0;
	}
	
	/**
	 * Whether Mario is currently falling (moving DOWN).
	 * @return
	 */
	public boolean isFalling() {
		return speed.y > 0;
	}
	
	public MarioEntity() {
		super(null, EntityType.MARIO, 0, 0, 0, 0, 0);
	}

}
