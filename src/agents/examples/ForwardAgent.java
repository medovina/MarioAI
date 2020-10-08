package agents.examples;

import java.awt.Graphics;

import agents.controllers.MarioAIBase;
import engine.core.IEnvironment;
import engine.core.LevelScene;
import engine.graphics.VisualizationComponent;
import engine.input.*;

/**
 * An agent that sprints forward and jumps if it detects an obstacle ahead.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class ForwardAgent extends MarioAIBase {

	private boolean enemyAhead() {
		return     entities.danger(1, 0) || entities.danger(1, -1) 
				|| entities.danger(2, 0) || entities.danger(2, -1)
				|| entities.danger(3, 0) || entities.danger(2, -1);
	}
	
	private boolean brickAhead() {
		return     tiles.brick(1, 0) || tiles.brick(1, -1) 
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
        MarioInput input = new MarioInput();

		// ALWAYS RUN RIGHT
		input.press(MarioKey.RIGHT);
		
		// ALWAYS SPEED RUN
		input.press(MarioKey.SPEED);
		
		// IF (ENEMY || BRICK AHEAD) => JUMP
        if (mario.mayJump && (enemyAhead() || brickAhead()))
            input.press(MarioKey.JUMP);
		
		// Keep jumping to go as high as possible.
		if (mario.isJumping()) {
			input.press(MarioKey.JUMP);
		}
		
		return input;
	}
}
