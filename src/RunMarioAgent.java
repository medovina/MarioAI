import ch.idsia.agents.IAgent;
import ch.idsia.benchmark.mario.MarioSimulator;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.utils.MarioLog;

class RunMarioAgent {
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
	}}
