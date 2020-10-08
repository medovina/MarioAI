package agents.examples;

import java.awt.Graphics;

import agents.AgentOptions;
import agents.controllers.MarioAIBase;
import engine.LevelScene;
import engine.VisualizationComponent;
import engine.input.MarioInput;
import environments.IEnvironment;

/**
 * An agent that sprints forward and jumps if it detects an obstacle ahead.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class ForwardAgent extends MarioAIBase {

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
	
	@Override
	public void debugDraw(VisualizationComponent vis, LevelScene level, IEnvironment env, Graphics g) {
		super.debugDraw(vis, level, env, g);
		String debug = "";
		if (enemyAhead()) {
			debug += "|ENEMY AHEAD|";
		}
		if (brickAhead()) {
			debug += "|BRICK AHEAD|";
		}
		if (mario != null && mario.onGround) {
			debug += "|ON GROUND|";
		}
		VisualizationComponent.drawStringDropShadow(g, debug, 0, 26, 1);
	}

	public MarioInput actionSelectionAI() {
		// ALWAYS RUN RIGHT
		control.runRight();
		
		// ALWAYS SPEED RUN
		control.sprint();
		
		// IF (ENEMY || BRICK AHEAD) => JUMP
		if (enemyAhead() || brickAhead()) control.jump();
		
		// If (In the air) => keep JUMPing
		if (!mario.onGround) {
			control.jump();
		}
		
		return action;
	}
}
