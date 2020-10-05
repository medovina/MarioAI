package tournament.run;

import tournament.EvaluationInfos;
import tournament.MarioConfig;

public class MarioRunResult extends EvaluationInfos {
	private MarioConfig config;	
		
	public MarioRunResult(MarioConfig config) {
		this.config = config;
	}
	
	public MarioConfig getConfig() {
		return config;
	}
	
	public String getCSVHeader() {
		return super.getCSVHeader() + ";levelSeed;options";
	}
	
	public String getCSV() {
		return super.getCSV() + ";" + config.getSeed() + ";" + config.getOptions();		
	}
	
}
