import java.awt.Graphics;
import ch.idsia.agents.AgentOptions;
import ch.idsia.agents.IAgent;
import ch.idsia.agents.controllers.MarioHijackAIBase;
import ch.idsia.agents.controllers.modules.Entities;
import ch.idsia.agents.controllers.modules.Tiles;
import ch.idsia.benchmark.mario.MarioSimulator;
import ch.idsia.benchmark.mario.engine.LevelScene;
import ch.idsia.benchmark.mario.engine.VisualizationComponent;
import ch.idsia.benchmark.mario.engine.generalization.EntityType;
import ch.idsia.benchmark.mario.engine.generalization.Tile;
import ch.idsia.benchmark.mario.engine.input.MarioControl;
import ch.idsia.benchmark.mario.engine.input.MarioInput;
import ch.idsia.benchmark.mario.environments.IEnvironment;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.utils.MarioLog;

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
public class MarioAgent extends MarioHijackAIBase implements IAgent {

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
	
	public static void main(String[] args) {
		// YOU MAY RAISE THE LOGGING LEVEL, even though there is probably no inforamation you need to know...
		//MarioLog.setLogLevel(Level.ALL);
		
		// UNCOMMENT THE LINE OF THE LEVEL YOU WISH TO RUN
		
		LevelConfig level = LevelConfig.LEVEL_0_FLAT;
		//LevelConfig level = LevelConfig.LEVEL_1_JUMPING;
		//LevelConfig level = LevelConfig.LEVEL_2_GOOMBAS;
		//LevelConfig level = LevelConfig.LEVEL_3_TUBES;
		//LevelConfig level = LevelConfig.LEVEL_4_SPIKIES;
		
		// CREATE SIMULATOR
		MarioSimulator simulator = new MarioSimulator(level.getOptions());
		
		// CREATE SIMULATOR AND RANDOMIZE LEVEL GENERATION
		// -- if you wish to use this, comment out the line above and uncomment the line below
		//MarioSimulator simulator = new MarioSimulator(level.getOptionsRandomized());
		
		// INSTANTIATE YOUR AGENT
		IAgent agent = new MarioAgent();
		
		// RUN THE SIMULATION
		EvaluationInfo info = simulator.run(agent);
		
		// CHECK RESULT
		switch (info.getResult()) {
		case LEVEL_TIMEDOUT:
			MarioLog.warn("LEVEL TIMED OUT!");
			break;
			
		case MARIO_DIED:
			MarioLog.warn("MARIO KILLED");
			break;
			
		case SIMULATION_RUNNING:
			MarioLog.error("SIMULATION STILL RUNNING?");
			throw new RuntimeException("Invalid evaluation info state, simulation should not be running.");
			
		case VICTORY:
			MarioLog.warn("VICTORY!!!");
			break;
		}
	}
	
}