import java.awt.Graphics;
import agents.AgentOptions;
import agents.controllers.MarioAIBase;
import agents.controllers.modules.Entities;
import agents.controllers.modules.Tiles;
import engine.LevelScene;
import engine.VisualizationComponent;
import engine.generalization.EntityType;
import engine.generalization.Tile;
import engine.input.MarioControl;
import engine.input.MarioInput;
import environments.IEnvironment;

/**
 * Code your custom agent here!
 * <p>
 * Modify {@link #actionSelectionAI()} to change Mario's behavior.
 * <p>
 * Modify {@link #debugDraw(VisualizationComponent, LevelScene, IEnvironment, Graphics)} to draw custom debug information.
 * <p>
 * You can change the type of level you want to play in {@link #main(String[])}.
 * <p>
 * Once your agent is ready, you can use the {@link Evaluate} class to benchmark the quality of your AI. 
 */
public class MyAgent extends MarioAIBase {

	@Override
	public void reset(AgentOptions options) {
		super.reset(options);
	}
	
	@Override
	public void debugDraw(VisualizationComponent vis, LevelScene level,	IEnvironment env, Graphics g) {
		super.debugDraw(vis, level, env, g);
		if (mario == null) return;

		// provide custom visualization using 'g'
		
		// EXAMPLE DEBUG VISUALIZATION
		String debug = "MY DEBUG STRING";
		VisualizationComponent.drawStringDropShadow(g, debug, 0, 26, 1);
	}

	/**
	 * Called on each tick to find out what action(s) Mario should take.
	 * <p>
	 * Use the {@link #e} field to query entities (Goombas, Spikies, Koopas, etc.) around Mario;
	 * see {@link EntityType} for a complete list of entities.
	 * Important methods you will definitely need: {@link Entities#danger(int, int)} and {@link Entities#entityType(int, int)}.
	 * <p>
	 * Use the {@link #t} field to query tiles (bricks, flower pots, etc.} around Mario;
	 * see {@link Tile} for a complete list of tiles.
	 * An important method you will definitely need: {@link Tiles#brick(int, int)}.
	 * <p>
	 * Use {@link #control} to output actions (technically this method must return {@link #action} in order for
	 * {@link #control} to work).
	 * Note that all actions specified through {@link #control} run in "parallel"
	 * (except {@link MarioControl#runLeft()} and {@link MarioControl#runRight()}, which cancel each other out in consecutive calls).
	 * Also note that you have to call {@link #control} methods on every {@link #actionSelectionAI()} tick
	 * (otherwise {@link #control} will think you DO NOT want to perform that action}. 
	 */
	@Override
	public MarioInput actionSelectionAI() {
		// ALWAYS RUN RIGHT
		control.runRight();
		
		// RETURN THE RESULT
		return action;
	}
	
}
