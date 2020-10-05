package tournament.run;

import java.util.Random;

import options.FastOpts;
import tournament.MarioConfig;

public class MarioRunsGenerator {
	
	public static int[] generateSeeds(int randomSeed, int count) {
		Random random = new Random(randomSeed);
		int[] seeds = new int[count];
		
		for (int i = 0; i < count; ++i) {
			seeds[i] = random.nextInt();
			while (seeds[i] <= 0) {
				seeds[i] += Integer.MAX_VALUE;
			}			
		}
		
		return seeds;
	}
	
	public static MarioConfig[] generateConfigs(int randomSeed, String prototypeOptions, int runCount) {
		
		int[] seeds = generateSeeds(randomSeed, runCount);
				
		MarioConfig[] configs = new MarioConfig[runCount];
		
		for (int i = 0; i < runCount; ++i) { 
			String options = prototypeOptions + FastOpts.L_RANDOM_SEED(seeds[i]);
			
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
