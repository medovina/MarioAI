package tournament;

import java.io.File;
import options.LevelConfig;

public class Evaluate {
    public static void evaluateLevels(
            int runs, int seed, int fromLevel, int toLevel, boolean saveResults,
            Class<?> agentClass, String agentId, String resultDir, boolean verbose) {
        for (int l = fromLevel ; l <= toLevel ; ++l) {
            LevelConfig level = LevelConfig.values()[l];
            System.out.println("Evaluating in " + level.name() + "...");
            
            EvaluateAgent evaluate = new EvaluateAgent(
                seed, level, runs,
                resultDir == null ? null : new File(resultDir), verbose);
            evaluate.evaluateAgent(agentClass, agentId);		
        }
	}
}
