import java.awt.Graphics;
import agents.controllers.MarioAIBase;
import engine.core.IEnvironment;
import engine.core.LevelScene;
import engine.graphics.VisualizationComponent;
import engine.input.*;

// Code your custom agent here!

public class MyAgent extends MarioAIBase {
	@Override
	public void debugDraw(VisualizationComponent vis, LevelScene level,	IEnvironment env, Graphics g) {
		super.debugDraw(vis, level, env, g);
		if (mario == null) return;

		// EXAMPLE DEBUG VISUALIZATION
		String debug = "MY DEBUG STRING";
		VisualizationComponent.drawStringDropShadow(g, debug, 0, 26, 1);
	}

    // Called on each tick to find out what action(s) Mario should take.
	@Override
	public MarioInput actionSelectionAI() {
        MarioInput input = new MarioInput();
        input.press(MarioKey.RIGHT);
        return input;
	}
}
