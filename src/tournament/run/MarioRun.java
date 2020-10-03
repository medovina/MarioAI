package tournament.run;

import agents.IAgent;
import engine.MarioSimulator;
import tools.EvaluationInfo;
import tournament.MarioConfig;

public class MarioRun {
	
	private MarioConfig config;

	public MarioRun(MarioConfig config) {
		this.config = config;
	}
	
	public synchronized MarioRunResult run(IAgent agent) {
		MarioRunResult result = new MarioRunResult(config);
		for (int i = 0; i < config.getRepetitions(); ++i) {
			MarioSimulator simulator = new MarioSimulator(config.getOptions());
			EvaluationInfo info = simulator.run(agent);
			result.addResult(info);
		}
		return result;		
	}

}
