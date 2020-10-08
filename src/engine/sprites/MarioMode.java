package engine.sprites;

/**
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public enum MarioMode {
/** Small Mario. */
	SMALL(0),

	/** Super Mario. */
	LARGE(1),

	/** Fire Mario. */
	FIRE_LARGE(2);
	
	private int code;
	
	private MarioMode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
