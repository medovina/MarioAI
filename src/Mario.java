import static java.lang.System.out;

import agents.IAgent;
import agents.controllers.keyboard.CheaterKeyboardAgent;
import engine.MarioSimulator;
import tools.EvaluationInfo;

public class Mario {
	public static void main(String[] args) throws Exception {
		IAgent agent = new CheaterKeyboardAgent();
        int level = 6;

        for (int i = 0 ; i < args.length ; ++i) {
            String s = args[i];
            switch (s) {
                case "-lev":
                    level = Integer.parseInt(args[++i]);
                    break;
                default:
                    if (s.startsWith("-")) {
                        out.println("usage: mario [-lev <level-number>]");
                        return;
                    }
                    agent = (IAgent) Class.forName(s).getConstructor().newInstance();
            }
        }

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
        
        System.exit(0);
	}
}
