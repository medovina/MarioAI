package tournament;

import agents.IAgent;
import options.LevelConfig;

public class Evaluate {
	private static String[] getEvaluationOptions(int runs, int seed, String levelOptions, boolean saveResults) {
		return new String[] {
				  "-s", String.valueOf(seed) // "seed"
				, "-o", levelOptions
				, "-c", Integer.toString(runs)  // level-count
				, "-i", "MarioAI"   // agent-id
				, "-d", saveResults ? "./results" : null // result-dir"	
		};
	}
	
    public static void evaluateLevels(
                int runs, int seed, int fromLevel, int toLevel, boolean saveResults, IAgent agent) {
        for (int l = fromLevel ; l <= toLevel ; ++l) {
            LevelConfig level = LevelConfig.values()[l];
            System.out.println("Evaluating in " + level.name() + "...");
            
            String[] options =
                getEvaluationOptions(runs, seed, level.getOptionsVisualizationOff(), saveResults);

            EvaluateAgentConsole.evaluate(options, agent);
        }
	}
}
