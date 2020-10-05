package tournament.run;

import options.FastOpts;
import tournament.MarioConfig;

public class MarioRunsGenerator {
	public static MarioConfig[] generateConfigs(int randomSeed, String prototypeOptions, int runCount) {
		MarioConfig[] configs = new MarioConfig[runCount];
		
		for (int i = 0; i < runCount; ++i) { 
			String options = prototypeOptions + FastOpts.L_RANDOM_SEED(randomSeed + i);
			
			configs[i] = new MarioConfig();
			configs[i].setOptions(options);
		}
		
		return configs;
	}
	
	public static MarioRun[] generateRunList(int randomSeed, String prototypeOptions, int runCount) {
		MarioConfig[] configs = generateConfigs(randomSeed, prototypeOptions, runCount);
		MarioRun[] runs = new MarioRun[runCount];
		for (int i = 0; i < runCount; ++i) {
			runs[i] = new MarioRun(configs[i]);
		}
		return runs;
	}
	
	public static MarioRuns generateRuns(int randomSeed, String prototypeOptions, int runCount) {
		MarioConfig[] configs = generateConfigs(randomSeed, prototypeOptions, runCount);
		return new MarioRuns(configs);
	}

}
