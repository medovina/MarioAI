package agents.examples;

import java.awt.Graphics;

import agents.controllers.MarioAIBase;
import engine.core.IEnvironment;
import engine.core.LevelScene;
import engine.graphics.VisualizationComponent;
import engine.input.*;

/**
 * Agent that sprints forward, jumps and shoots.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class ShooterAgent extends MarioAIBase {
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

	public MarioInput actionSelectionAI() {
        MarioInput input = new MarioInput();

		// ALWAYS RUN RIGHT
		input.press(MarioKey.RIGHT);
        
        // SHOOT WHENEVER POSSIBLE
        if (mario.mayShoot) {
            // TOGGLE SPEED BUTTON
            if (!lastInput.isPressed(MarioKey.SPEED))
                input.press(MarioKey.SPEED);    
        } else
            input.press(MarioKey.SPEED);  // SPRINT

		// IF (ENEMY || BRICK AHEAD) => JUMP
        if (mario.mayJump && (enemyAhead() || brickAhead()))
            input.press(MarioKey.JUMP);
		
		// Keep jumping to go as high as possible.
		if (mario.isJumping()) {
			input.press(MarioKey.JUMP);
		}
		
		return input;
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
