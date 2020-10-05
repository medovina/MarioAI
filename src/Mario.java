import static java.lang.System.out;

import agents.IAgent;
import agents.controllers.keyboard.CheaterKeyboardAgent;
import engine.MarioSimulator;
import options.LevelConfig;
import tools.EvaluationInfo;
import tournament.Evaluate;

public class Mario {
    static void usage() {
        System.out.println("usage: mario [<agent-classname>] [<option>...]");
        System.out.println("options:");
        System.out.println("  -level <level-number>[-<to-level-number>]");
        System.out.println("  -sim <count>");
        System.exit(1);
    }

	public static void main(String[] args) throws Exception {
		IAgent agent = null;
        int fromLevel = 6, toLevel = 0;
        int sim = 0;

        for (int i = 0 ; i < args.length ; ++i) {
            String s = args[i];
            switch (s) {
                case "-level":
                    String[] a = args[++i].split("-");
                    fromLevel = Integer.parseInt(a[0]);
                    toLevel = a.length > 1 ? Integer.parseInt(a[1]) : fromLevel;
                    break;
                case "-sim":
                    sim = Integer.parseInt(args[++i]);;
                    break;
                default:
                    if (s.startsWith("-"))
                        usage();
                    agent = (IAgent) Class.forName(s).getConstructor().newInstance();
            }
        }

        if (sim > 0) {  // simulate a series of games
            if (agent == null) {
                System.out.println("must specify agent with -sim");
                return;
            }
            Evaluate.evaluateLevels(sim, 0, fromLevel, toLevel, false, agent);
        } else {  // play one game visually
            if (toLevel > fromLevel) {
                System.out.println("level range only works with -sim");
                return;
            }

            if (agent == null)
                agent = new CheaterKeyboardAgent();

            LevelConfig level = LevelConfig.values()[fromLevel];
            System.out.println("Running " + level.name());

            EvaluationInfo info = MarioSimulator.main(agent, level);
            
            switch (info.getResult()) {
                case LEVEL_TIMEDOUT:
                    out.println("The level timed out!");
                    break;
                    
                case SIMULATION_RUNNING:
                    out.println("Simulation still running?");
                    throw new RuntimeException("Invalid evaluation info state, simulation should not be running.");
                    
                case VICTORY:
                    out.println("Victory!!!");
                    break;

                default:
            }
        }
            
        System.exit(0);
	}
}
