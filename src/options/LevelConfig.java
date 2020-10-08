package options;

import engine.core.Enemy;

public enum LevelConfig {
	
	/**
	 * Level with no threats + no jumps.
	 */
	LEVEL_0_FLAT(FastOpts.L_FLAT),
	
	/**
	 * Level where you have to jump.
	 */
	LEVEL_1_JUMPING(FastOpts.ALL_OFF),
	
	/**
	 * And here you must mind malicious GOOMBAs!
	 */
	LEVEL_2_GOOMBAS(FastOpts.ALL_OFF + FastOpts.L_ENEMY(Enemy.GOOMBA)),
	
	/**
	 * + Tubes with dangerous flowers. 
	 */
    LEVEL_3_TUBES(FastOpts.ONLY_TUBES + FastOpts.L_ENEMY(Enemy.GOOMBA)),
	
	/**
	 * Here we're adding SPIKIES! (Cannot be killed by fireballs...)
	 */
    LEVEL_4_SPIKIES(FastOpts.ONLY_TUBES + FastOpts.L_ENEMY(Enemy.GOOMBA, Enemy.SPIKY)),
	
	/**
	 * A level with green turtles (so-called KOOPAs).
	 */
    LEVEL_5_KOOPAS(FastOpts.ONLY_TUBES +
        FastOpts.L_ENEMY(Enemy.GOOMBA, Enemy.SPIKY, Enemy.GREEN_KOOPA)),

    /** The full game, including goodies such as coins, mushrooms, and flowers. */
    LEVEL_6_FULL_GAME(FastOpts.L_ENEMY(Enemy.GOOMBA, Enemy.SPIKY, Enemy.GREEN_KOOPA)),

    /* An extra challenge. */
    LEVEL_7_FULL_GAME_HARD(FastOpts.L_DIFFICULTY(1) +
        FastOpts.L_ENEMY(Enemy.GOOMBA, Enemy.SPIKY, Enemy.GREEN_KOOPA, Enemy.RED_KOOPA,
                         Enemy.GOOMBA_WINGED, Enemy.GREEN_KOOPA_WINGED)),

    /* Even tougher. */
    LEVEL_8_FULL_GAME_EXTRA_HARD(FastOpts.L_DIFFICULTY(2) +
        FastOpts.L_ENEMY(Enemy.GOOMBA, Enemy.SPIKY, Enemy.GREEN_KOOPA, Enemy.RED_KOOPA,
                         Enemy.GOOMBA_WINGED, Enemy.GREEN_KOOPA_WINGED, Enemy.SPIKY_WINGED));

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
