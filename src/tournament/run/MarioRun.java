package tournament.run;

import agents.IAgent;
import engine.core.MarioSimulator;
import tournament.EvaluationInfo;
import tournament.MarioConfig;

public class MarioRun {
	
	private MarioConfig config;

	public MarioRun(MarioConfig config) {
		this.config = config;
	}
	
	public synchronized MarioRunResult run(IAgent agent, boolean verbose) {
		MarioRunResult result = new MarioRunResult(config);
        MarioSimulator simulator = new MarioSimulator(config.getOptions());
        EvaluationInfo info = simulator.run(agent);

        if (verbose)
            System.out.println("  seed " + config.getSeed() + ": " + info.summary());

        result.addResult(info);
		return result;		
	}

}
