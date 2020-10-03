import static java.lang.System.out;

import engine.MarioSimulator;

public class Mario {
	public static void main(String[] args) {
        int level = 6;

        for (int i = 0 ; i < args.length ; ++i) {
            String s = args[i];
            switch (s) {
                case "-lev":
                    level = Integer.parseInt(args[++i]);
                    break;
                default:
                    out.println("usage: mario [-lev <level-number>]");
                    return;
            }
        }

		MarioSimulator.main(level);
	}
}
