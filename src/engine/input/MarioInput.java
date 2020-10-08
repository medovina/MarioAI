package engine.input;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Used by {@link IAgent} to represent the state of pressed key by the agent.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class MarioInput {
	private Set<MarioKey> pressed = new TreeSet<MarioKey>(new Comparator<MarioKey>() {
		@Override
		public int compare(MarioKey o1, MarioKey o2) {
			return o1.getCode() - o2.getCode();
		}		
	});
	
	public Set<MarioKey> getPressed() {
		return pressed;
	}
	
	/**
	 * Change state of given 'key'.
	 * @param key
	 */
	public void toggle(MarioKey key) {
		if (pressed.contains(key)) pressed.remove(key);
		else pressed.add(key);
	}
	
	/**
	 * PRESS given 'key' or keep pressed if already pressed.
	 * @param key
	 */
	public void press(MarioKey key) {
		pressed.add(key);		
	}
	
	/**
	 * RELEASE given 'key'.
	 * @param key
	 */
	public void release(MarioKey key) {
		if (!pressed.contains(key)) return;
		pressed.remove(key);
	}
	
	/**
	 * Whether 'key' is PRESSED.
	 * @param key
	 * @return
	 */
	public boolean isPressed(MarioKey key) {
		return pressed.contains(key);
	}
	
	/**
	 * Completely resets the instance.
	 */
	public void reset() {
		pressed.clear();
	}
	
}
