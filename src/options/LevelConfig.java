package options;

import engine.generalization.Enemy;

public enum LevelConfig {
	
	/**
	 * Level with no threats + no jumps.
	 */
	LEVEL_0_FLAT(FastOpts.VIS_ON_2X + FastOpts.LEVEL_01_FLAT),
	
	/**
	 * Level where you have to jump.
	 */
	LEVEL_1_JUMPING(FastOpts.VIS_ON_2X + FastOpts.LEVEL_02_JUMPING),
	
	/**
	 * And here you must mind malicious GOOMBAs!
	 */
	LEVEL_2_GOOMBAS(LEVEL_1_JUMPING.options + FastOpts.L_ENEMY(Enemy.GOOMBA)),
	
	/**
	 * + Tubes with dangerous flowers. 
	 */
    LEVEL_3_TUBES(LEVEL_2_GOOMBAS.options + FastOpts.L_TUBES_ON),
	
	/**
	 * Here we're adding SPIKIES! (Cannot be killed by fireballs...)
	 */
    LEVEL_4_SPIKIES(LEVEL_3_TUBES.options + FastOpts.L_ENEMY(Enemy.SPIKY)),
	
	/**
	 * A level with green turtles (so-called KOOPAs).
	 */
    LEVEL_5_KOOPAS(LEVEL_4_SPIKIES.options + FastOpts.L_ENEMY(Enemy.GREEN_KOOPA)),

    /** All of the above, plus other goodies such as coins, mushrooms, and flowers. */
    LEVEL_6_FULL_GAME(FastOpts.VIS_ON_2X +
                      FastOpts.L_ENEMY(Enemy.GOOMBA, Enemy.SPIKY, Enemy.GREEN_KOOPA));

	private String options;
	
	private LevelConfig(String options) {
		this.options = options;
	}
	
	public String getOptions() {
		return options;
	}
	
	public String getOptionsRandomized() {
		return options + FastOpts.L_RANDOMIZE;
	}
	
	public String getOptionsVisualizationOff() {
		return options + FastOpts.VIS_OFF;
	}
	
	public String getOptionsRndVissOff() {
		return options + FastOpts.VIS_OFF + FastOpts.L_RANDOMIZE;
	}
	
}
