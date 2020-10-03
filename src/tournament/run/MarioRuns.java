package tournament.run;

import agents.IAgent;
import tournament.MarioConfig;

public class MarioRuns {
	
	public MarioConfig[] configs;
	
	public MarioRuns(MarioConfig[] configs) {
		this.configs = configs;
	}
	
	public synchronized MarioRunResults run(IAgent agent) {
		MarioRunResults results = new MarioRunResults();
		for (MarioConfig config : configs) {
			MarioRun run = new MarioRun(config);
			MarioRunResult result = run.run(agent);
			results.addRunResults(result);
		}
		return results;
	}

}
