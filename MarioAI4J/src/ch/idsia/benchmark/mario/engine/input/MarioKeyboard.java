package ch.idsia.benchmark.mario.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents configurable keyboard. Keyboard is configured via {@link #register(int, MarioKey)},
 * than it acts as {@link KeyListener}.
 *  
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class MarioKeyboard implements KeyListener {

	private Map<Integer, MarioKey> mapping = new HashMap<Integer, MarioKey>();
	
	private MarioInput input = new MarioInput();
	
	public void register(int keyboardCode, MarioKey key) {
		mapping.put(keyboardCode, key);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		MarioKey key = mapping.get(e.getKeyCode());
		if (key != null) input.press(key);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		MarioKey key = mapping.get(e.getKeyCode());
		if (key != null) input.release(key);
	}

	public void reset() {
		input.reset();		
	}

	public MarioInput getInput() {
		return input;
	}
	
}
