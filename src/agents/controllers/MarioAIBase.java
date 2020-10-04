package agents.controllers;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;

import agents.AgentOptions;
import agents.controllers.modules.*;
import engine.LevelScene;
import engine.SimulatorOptions;
import engine.SimulatorOptions.ReceptiveFieldMode;
import engine.generalization.MarioEntity;
import engine.VisualizationComponent;
import engine.input.*;
import environments.IEnvironment;

/**
 * Splits {@link #actionSelection()} into {@link #actionSelectionAI()} done by AGENT
 * and {@link #actionSelectionKeyboard()} done by {@link #keyboard}.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class MarioAIBase extends MarioAgentBase implements KeyListener, IMarioDebugDraw {
	/**
	 * Information about Mario's body.
	 */
	protected MarioEntity mario;
	
	/**
	 * Actions that an {@link IAgent} wants to perform.
	 */
	protected MarioInput action = new MarioInput();
	
	/**
	 * A layer over {@link #action} that provides a programming-friendly abstraction of Mario actions.
	 */
	protected MarioControl control = new MarioControl(action);
	
	/** Information about entities in Mario's vicinity. */
	protected Entities e = new Entities();
	
	/** Information about tiles in Mario's vicinity. */
	protected Tiles t = new Tiles();

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
		control.reset();
		action.reset();
		e.reset(options);
		t.reset(options);
	}

	public void observe(IEnvironment environment) {
		mario         = environment.getMario();
		control.setMario(mario);
		t.tileField   = environment.getTileField();
		e.entityField = environment.getEntityField();
		e.entities    = environment.getEntities();
		action.tick();
		control.tick();
	}

	public void receiveReward(float intermediateReward) {
	}

	@Override
	public MarioInput actionSelection() {
		if (hijacked) return actionSelectionKeyboard();
		return actionSelectionAI();
	}
	
	public MarioInput actionSelectionAI() {
		return action;
	}
	
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
		marioState = "";
		if (control.wantsShoot()) marioState += "|WANT SHOOT|";
		else marioState += "|-----|";
		if (control.wantsSprint()) marioState += "|WANT SPRINT|";
		else marioState += "|------|";
		VisualizationComponent.drawStringDropShadow(g, marioState, 0, row++, 7);		
		VisualizationComponent.drawStringDropShadow(g, "m.s.[x,y] = [" + floatFormat(mario.sprite.x) + "," + floatFormat(mario.sprite.y) + "]", 0, row++, 7);
		VisualizationComponent.drawStringDropShadow(g, "m.s.[xOld,yOld] = [" + floatFormat(mario.sprite.xOld) + "," + floatFormat(mario.sprite.yOld) + "]", 0, row++, 7);
		VisualizationComponent.drawStringDropShadow(g, "m.inTile[X,Y] = [" + mario.inTileX + "," + mario.inTileY + "]", 0, row++, 7);
		VisualizationComponent.drawStringDropShadow(g, "m.speed.[x,y] = [" + floatFormat(mario.speed.x) + "," + floatFormat(mario.speed.y) + "]", 0, row++, 7);
		
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
		// CHEATS!		
		
		case KeyEvent.VK_V:
			if (isPressed) SimulatorOptions.isVisualization = !SimulatorOptions.isVisualization;
			return;
			
		// FREEZES CREATURES, THEY WILL NOT BE MOVING
		case KeyEvent.VK_O:
			if (isPressed) {
				SimulatorOptions.areFrozenCreatures = !SimulatorOptions.areFrozenCreatures;
			}
			return;
			
		// RENDER MAP PIXEL [X,Y] FOR EVERY ENTITIES WITHIN THE SCENE
		case KeyEvent.VK_L:
			if (isPressed) SimulatorOptions.areLabels = !SimulatorOptions.areLabels;
			return;
			
		// CENTER CAMERA ON MARIO
		case KeyEvent.VK_C:
			if (isPressed) SimulatorOptions.isCameraCenteredOnMario = !SimulatorOptions.isCameraCenteredOnMario;
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
		
		// RECEPTIVE FIELD MODE VISUALIZATION
		case KeyEvent.VK_G:
			if (isPressed) {
				SimulatorOptions.receptiveFieldMode = ReceptiveFieldMode.getForCode(SimulatorOptions.receptiveFieldMode.getCode()+1);
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
