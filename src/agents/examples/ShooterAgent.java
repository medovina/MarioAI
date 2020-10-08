package agents.examples;

import java.awt.Graphics;

import agents.AgentOptions;
import agents.controllers.MarioAIBase;
import engine.core.IEnvironment;
import engine.core.LevelScene;
import engine.graphics.VisualizationComponent;
import engine.input.MarioInput;

/**
 * Agent that sprints forward, jumps and shoots.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class ShooterAgent extends MarioAIBase {

	@Override
	public void reset(AgentOptions options) {
		super.reset(options);
	}

	private boolean enemyAhead() {
		return
				   entities.danger(1, 0) || entities.danger(1, -1) 
				|| entities.danger(2, 0) || entities.danger(2, -1)
				|| entities.danger(3, 0) || entities.danger(2, -1);
	}
	
	private boolean brickAhead() {
		return
				   tiles.brick(1, 0) || tiles.brick(1, -1) 
				|| tiles.brick(2, 0) || tiles.brick(2, -1)
				|| tiles.brick(3, 0) || tiles.brick(3, -1);
	}

	public MarioInput actionSelectionAI() {
		// ALWAYS RUN RIGHT
		control.runRight();
		
		// ALWAYS SPRINT		
		control.sprint();
		
		// ALWAYS SHOOT (if able ... max 2 fireballs at once!)
		control.shoot();
		
		// ENEMY || BRICK AHEAD => JUMP
		// WARNING: do not press JUMP if UNABLE TO JUMP!
		if (enemyAhead() || brickAhead()) control.jump();		
		
		// If in the air => keep JUMPing
		if (!mario.onGround) control.jump();
		
		return action;
	}
	
	@Override
	public void debugDraw(VisualizationComponent vis, LevelScene level, IEnvironment env, Graphics g) {
		super.debugDraw(vis, level, env, g);
		if (mario == null) return;
		String debug = "";
		if (enemyAhead()) debug += "|ENEMY AHEAD|";
		else debug += "|-----------|";
		if (brickAhead()) debug += "|BRICK AHEAD|";
		else debug += "|-----------|";
		VisualizationComponent.drawStringDropShadow(g, debug, 0, 26, 1);
	}
}
