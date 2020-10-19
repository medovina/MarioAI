package agents.controllers;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;

import agents.AgentOptions;
import engine.core.Entities;
import engine.core.IEnvironment;
import engine.core.LevelScene;
import engine.core.MarioEntity;
import engine.core.Tiles;
import engine.graphics.VisualizationComponent;
import engine.input.*;
import options.SimulatorOptions;
import options.SimulatorOptions.ReceptiveFieldMode;

/**
 * Splits {@link #actionSelection()} into {@link #actionSelectionAI()} done by AGENT
 * and {@link #actionSelectionKeyboard()} done by {@link #keyboard}.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
abstract public class MarioAIBase extends MarioAgentBase implements KeyListener, IMarioDebugDraw {
	/**
	 * Information about Mario's body.
	 */
	protected MarioEntity mario;
	
	/**
	 * The most recent actions that an agent has performed.
	 */
	protected MarioInput lastInput = new MarioInput();
	
	/** Information about entities in Mario's vicinity. */
	protected Entities entities = new Entities();
	
	/** Information about tiles in Mario's vicinity. */
	protected Tiles tiles = new Tiles();

	protected MarioCheaterKeyboard keyboard = new MarioCheaterKeyboard();
	
	protected boolean hijacked = false;
	
	protected DecimalFormat floatFormat = new DecimalFormat("0.0");	 
	
	protected boolean renderExtraDebugInfo = false;

	public MarioAIBase() {
		super("MarioAIBase");
		name = getClass().getSimpleName();
	}
	
	@Override
	public void reset(AgentOptions options) {
		super.reset(options);
		lastInput.reset();
		entities.reset(options);
		tiles.reset(options);
	}

	public void observe(IEnvironment environment) {
		mario         = environment.getMario();
		tiles.tileField   = environment.getTileField();
		entities.entityField = environment.getEntityField();
		entities.allEntities    = environment.getEntities();
	}

	@Override
	public MarioInput actionSelection() {
        lastInput = hijacked ? actionSelectionKeyboard() : actionSelectionAI();
        return lastInput;
	}
	
	/**
	 * Called on each tick to find out what action(s) Mario should take.
	 * <p>
	 * Use the {@link #entities} field to query entities (Goombas, Spikies, Koopas, etc.) around Mario;
	 * see {@link EntityType} for a complete list of entities.
	 * Important methods you will definitely need: {@link Entities#danger(int, int)} and {@link Entities#entityType(int, int)}.
	 * <p>
	 * Use the {@link #tiles} field to query tiles (bricks, flower pots, etc.} around Mario;
	 * see {@link Tile} for a complete list of tiles.
	 * An important method you will definitely need: {@link Tiles#brick(int, int)}.
	 * <p>
	 */
	public abstract MarioInput actionSelectionAI();
	
	public MarioInput actionSelectionKeyboard() {
		return keyboard.getInput();
	}	
	
	@Override
	public void debugDraw(VisualizationComponent vis, LevelScene level,	IEnvironment env, Graphics g) {
		if (hijacked) {
			MarioInput ai = actionSelectionAI();
			if (ai != null) {
				String msg = "AGENT KEYS:   ";
				boolean first = true;				
				for (MarioKey pressedKey : ai.getPressed()) {
					if (first) first = false;
					else msg += " ";
					msg += pressedKey.getDebug();
				}
				VisualizationComponent.drawStringDropShadow(g, msg, 0, 9, 6);
			}
		}
		if (mario == null) return;
		
        if (!renderExtraDebugInfo) return;

		VisualizationComponent.drawStringDropShadow(g, "FPS: ", 33, 3, 7);
		VisualizationComponent.drawStringDropShadow(g,
				((SimulatorOptions.FPS > 99) ? "\\infty" : "  "
						+ SimulatorOptions.FPS.toString()), 33, 4, 7);

        String msg = "PRESSED KEYS: ";
		VisualizationComponent.drawStringDropShadow(g, msg, 0, 7, 6);

        msg = "";
        for (MarioKey pressedKey : lastInput.getPressed())
            msg += (msg.equals("") ? pressedKey.getDebug() : " " + pressedKey.getDebug());
        VisualizationComponent.drawString(g, msg, 109, 61, 1);
		
		int row = 10;
		
		String marioState = "";
		marioState += "|FBs:" + level.fireballsOnScreen + "|";
		if (mario.mayJump) marioState += "|M.JUMP|";
		else marioState += "|------|";
		if (mario.mayShoot) marioState += "|M.SHOOT|";
		else marioState += "|-------|";
		if (mario.onGround) marioState += "|ON.GRND|";
		else marioState += "|-------|";
		VisualizationComponent.drawStringDropShadow(g, marioState, 0, row++, 7);
        VisualizationComponent.drawStringDropShadow(g, "m.s.[x,y] = " +
            "[" + floatFormat(mario.sprite.x) + "," + floatFormat(mario.sprite.y) + "]", 0, row++, 7);
        VisualizationComponent.drawStringDropShadow(g, "m.s.[xOld,yOld] = " +
            "[" + floatFormat(mario.sprite.xOld) + "," + floatFormat(mario.sprite.yOld) + "]", 0, row++, 7);
        VisualizationComponent.drawStringDropShadow(g, "m.inTile[X,Y] = " +
            "[" + mario.inTileX + "," + mario.inTileY + "]", 0, row++, 7);
        VisualizationComponent.drawStringDropShadow(g, "m.speed.[x,y] = " +
            "[" + floatFormat(mario.speed.x) + "," + floatFormat(mario.speed.y) + "]", 0, row++, 7);
        
        drawProgress(g, env);
	}
    
	private void drawProgress(Graphics g, IEnvironment env) {
		String entirePathStr = "......................................>";
		double physLength = (env.getLevelScene().getLevelLength()) * 16;
		int progressInChars = (int) (mario.sprite.x * (entirePathStr.length() / physLength));
		String progress_str = "";
		for (int i = 0; i < progressInChars - 1; ++i)
			progress_str += ".";
		progress_str += "M";
		try {
			VisualizationComponent.drawStringDropShadow(g,
					entirePathStr.substring(progress_str.length()),
					progress_str.length(), 28, 0);
		} catch (StringIndexOutOfBoundsException e) {
			// System.err.println("warning: progress line inaccuracy");
		}
		VisualizationComponent.drawStringDropShadow(g, progress_str, 0, 28, 2);
	}

	private String floatFormat(float num) {
		return floatFormat.format(num).replace(",", ".");
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		keyboard.keyTyped(e);
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e, true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e, false);
	}
	
	protected void toggleKey(KeyEvent e, boolean isPressed) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		
		case KeyEvent.VK_H:
			if (isPressed) hijacked = !hijacked;
			return;
			
		// TOGGLE EXTRA DEBUG STUFF
		case KeyEvent.VK_E:
			if (isPressed) renderExtraDebugInfo = !renderExtraDebugInfo;
			return;
		
		// FREEZES CREATURES, THEY WILL NOT BE MOVING
		case KeyEvent.VK_O:
			if (isPressed) {
				SimulatorOptions.areFrozenCreatures = !SimulatorOptions.areFrozenCreatures;
			}
			return;
			
		// RENDER MAP PIXEL [X,Y] FOR EVERY ENTITY WITHIN THE SCENE
		case KeyEvent.VK_L:
			if (isPressed) SimulatorOptions.showLabels = (SimulatorOptions.showLabels + 1) % 3;
			return;
			
		// ADJUST SIMULATOR FPS
		case KeyEvent.VK_PLUS:
		case 61:
			if (isPressed) {
				++SimulatorOptions.FPS;
				SimulatorOptions.FPS = (SimulatorOptions.FPS > SimulatorOptions.MaxFPS ? SimulatorOptions.MaxFPS : SimulatorOptions.FPS);
				SimulatorOptions.AdjustMarioVisualComponentFPS();
			}
			return;			
		case KeyEvent.VK_MINUS:
			if (isPressed) {
				--SimulatorOptions.FPS;
				SimulatorOptions.FPS = (SimulatorOptions.FPS < 1 ? 1 : SimulatorOptions.FPS);
				SimulatorOptions.AdjustMarioVisualComponentFPS();
			}
			return;
		
		// GRID VISUALIZATION
		case KeyEvent.VK_G:
			if (isPressed) {
                SimulatorOptions.receptiveFieldMode =
                    ReceptiveFieldMode.getForCode(SimulatorOptions.receptiveFieldMode.getCode()+1);
			}
			return;
			
		// PAUSES THE SIMULATION
		case KeyEvent.VK_P:
		case KeyEvent.VK_SPACE:
			if (isPressed) {
				SimulatorOptions.isGameplayStopped = !SimulatorOptions.isGameplayStopped;
			}
			return;
			
		// WHEN PAUSED, POKES THE SIMULATOR TO COMPUTE NEXT FRAME
		case KeyEvent.VK_N:
			if (isPressed) {
				SimulatorOptions.nextFrameIfPaused = true;
			}
			return;
			
		// MARIO WILL FLY
		case KeyEvent.VK_F:
			if (isPressed) {
				SimulatorOptions.isFly = !SimulatorOptions.isFly;
			}
			return;
			
		}
		// NOT HANDLED YET
		// => ask keyboard		
		if (isPressed) keyboard.keyPressed(e);
		else keyboard.keyReleased(e);
	}
}
