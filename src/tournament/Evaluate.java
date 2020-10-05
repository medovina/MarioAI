package tournament;

import agents.IAgent;
import options.LevelConfig;

public class Evaluate {
    public static void evaluateLevels(
                int runs, int seed, int fromLevel, int toLevel, boolean saveResults, IAgent agent) {
        for (int l = fromLevel ; l <= toLevel ; ++l) {
            LevelConfig level = LevelConfig.values()[l];
            System.out.println("Evaluating in " + level.name() + "...");
            
            EvaluateAgent evaluate = new EvaluateAgent(
                seed, level.getOptionsVisualizationOff(), runs, null);
            evaluate.evaluateAgent(agent.getName(), agent);		
        }
	}
}
