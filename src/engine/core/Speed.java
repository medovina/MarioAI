package engine.core;

/**
 * A speed in pixels per tick.
 */
public class Speed {
	
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
