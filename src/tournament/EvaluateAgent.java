package tournament;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import agents.IAgent;
import options.LevelConfig;
import tournament.run.MarioRun;
import tournament.run.MarioRunResult;
import tournament.run.MarioRunResults;
import tournament.run.MarioRunsGenerator;
import tournament.utils.Sanitize;

public class EvaluateAgent {
	private int seed = 0;
	private LevelConfig levelConfig;
	private int runCount;
    private File resultDirFile;
    private boolean verbose;
	
    public EvaluateAgent(int seed, LevelConfig levelConfig, int runCount, File resultDirFile,
                         boolean verbose) {
		this.seed = seed;
		this.levelConfig = levelConfig;
		this.runCount = runCount;
        this.resultDirFile = resultDirFile;
        this.verbose = verbose;
	}
	
	public MarioRunResults evaluateAgent(Class<?> agentClass, String agentId) {
		MarioRun[] runs = MarioRunsGenerator.generateRunList(
            seed, levelConfig.getOptionsVisualizationOff(), runCount);
		
		MarioRunResults results = new MarioRunResults();
		
		for (int i = 0; i < runs.length; ++i) {
            IAgent agent;
            
            try {
                agent = (IAgent) agentClass.getConstructor().newInstance();
            } catch (Exception e) { throw new RuntimeException(e); }

            if (i == 0 && agentId == null)
                agentId = Sanitize.idify(agent.getName());

			MarioRunResult result = runs[i].run(agent, verbose);
            
			results.addRunResults(result);			
		}
		
		System.out.println("  " + results.toString());
		
		if (resultDirFile != null)
			outputResults(agentId, results);	
		
		return results;
	}

	private void outputResults(String agentId, MarioRunResults results) {		
		resultDirFile.mkdirs();
		
		outputAgentResults(agentId, results);
		outputAgentGlobalAvgs(agentId, results);
	}

	
	private void outputAgentResults(String agentId, MarioRunResults results) {
		File file = new File(resultDirFile, "games.csv");
		System.out.println("Writing games into " + file.getPath());
		
		PrintWriter writer = null;
		try {
			boolean outputHeaders = !file.exists();
			writer = new PrintWriter(new FileOutputStream(file, true));
            if (outputHeaders)
                writer.println("datetime;agent;level;seed;" + results.getResults().get(0).getCSVHeader());
                
			int simulationNumber = 0;
			for (MarioConfig config : results.getConfigs()) {
                ++simulationNumber;
                writer.print(LocalDateTime.now());
                writer.print(";" + agentId);
                writer.print(";" + levelConfig.name());
                writer.print(";" + config.getSeed());
                EvaluationInfo info = results.getResults().get(simulationNumber-1);
                writer.println(";" + info.getCSV());
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to write results into: " + file.getAbsolutePath());
		} finally {
			if (writer != null) writer.close();
		}
	}
	
	private void outputAgentGlobalAvgs(String agentId, MarioRunResults results) {
		File file = new File(resultDirFile, "averages.csv");		
		System.out.println("Writing averages into " + file.getPath());
		
		PrintWriter writer = null;
		try {
			boolean outputHeaders = !file.exists();
			writer = new PrintWriter(new FileOutputStream(file, true));
			if (outputHeaders) {
				writer.println("datetime;agent;level;startSeed;" + results.getCSVHeader());
			}
            writer.print(LocalDateTime.now() + ";");
			writer.print(agentId + ";");
            writer.print(levelConfig.name() + ";");
			writer.print(seed + ";");
			writer.println(results.getCSV());
		} catch (Exception e) {
			throw new RuntimeException("Failed to write results into: " + file.getAbsolutePath());
		} finally {
			if (writer != null) writer.close();
		}
	}
}
