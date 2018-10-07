Refactored MarioAI code from: http://code.google.com/p/marioai/

# MarioAI Version 0.3.1

![alt tag](https://github.com/medovina/MarioAI/raw/master/MarioAI4J/MatioAI4J.png)

## Project structure

**MarioAI4J** -> The main project including a runnable MarioSimulator class featuring a keyboard-controlled Mario.  Use the arrow keys + A (jump) + S (sprint/shoot).  Press 'G' multiple times to visualize Mario's receptive field.

**MarioAI4J-Agents** -> Sample agents.  This project must reference MarioAI4J in order to compile.

**MarioAI4J-Tournament** -> Lets you evaluate agents from the console (check the EvaluateAgentConsole class) producing detailed CSV reports.

**MarioAI4J-Playground** -> Here you can start coding your own Mario AI right away.  Just navigate to the MarioAgent class and fool around.  (Requires MarioAI4J, MarioAI4J-Tournament and its libraries on the classpath).

I did not mavenize these projects as I usually do, so you have to set them up manually within your IDE.  But as they need only 2 libraries and the projects feature a rather standard Java project layout, it is a trivial task (the projects are directly importable into Eclipse).

## Change Log

Things that are different from the original MarioAI v0.2.0 project:

1) Only Java agents can be used now.

2) GameViewer + ToolsConfigurator are not working (not refactored/broken).

3) There are new agent base types.  Especially interesting is MarioHijackAIBase, which is an agent you can interrupt anytime during its run, hijacking its controls and manually controlling Mario from the keyboard.  Press 'H' (hijack) to start controlling the agent manually.  This is great for debugging as you can let your AI run "dry" and watch logs / debug draws as you position Mario into a concrete situation.  See the following section for more information about extra debug controls that you can use with MarioHijackAIBase.

4) Agents now may implement the IMarioDebugDraw interface, which is a callback that is regularly called to render custom debugging information inside the visualization component.

5) All options are now grouped within a class MarioOptions that contains different enums for different types of options (boolean, int, float, string).  These options are then read and used by the respective option categories (AIOptions, LevelOptions, SimulationOptions, SystemOptions, VisualizationOptions).

6) You can now easily create an options string using the constants and functions in the FastOpts class.

7) The running simulation is now encapsulated within a class MarioSimulator, which is instantiated using some options and can then be used to run(IAgent).

8) WARNING - 1 JVM can run 1 SIMULATION (visualized or headless) at most.  (I did not change the original architecture that uses statics a lot).

9) The agent/environment interface has been refactored.  Lots of information is now encapsulated inside enums and classes rather than bytes, ints, named constants and C-like function calls.  Querying tiles / entities is now much simpler using the Tiles and Entities sensory modules.

10) Mario's controls have been refactored as well.  Pressing/releasing buttons that control Mario is now encapsulated within the MarioInput class. Or even better, use MarioControl instead of MarioInput as it brushes out shoot/sprint glitches.

11) Sample agents have been reimplemented using the new agent base classes and environment interface.  They are more readable now.

12) Receptive field visualization has been fixed: now it correctly aligns with the respective simulation tiles.  It now includes "relative position", "tiles" and "entity" visualization modes so you can quickly see how to reference concrete tiles, as well as which tiles and entities are present within the receptive field.

13) Generalization of tiles / entities has been improved.  It is now easy to query the current speed of entities and their relative positions with respect to Mario's position in pixels.

14) I've added JavaDoc to crucial fields and methods related to developing Mario agents.

## MarioHijackAIBase 

All sample AIs within the MarioAI4J-Agents and MarioAI4J-Playground projects use this class as their base.  It allows you to visualize / perform extra debugging information, which is extremely useful when developing.

Here are the keys (controls) you can press when running a simulation:

1. "space" or "P": pause the simulation
2. "N": when the simulation is paused, step forward by a single frame
3. "H": hijack Mario, i.e. you may control Mario manually and navigate him into a situation you wish to observe
4. "G": cycle between receptive field grids that indicate what is around Mario:

   * 0 = off
   * 1 = grid showing tile coordinates relative to Mario
   * 2 = grid showing tile types (see the Tile enum)
   * 3 = grid showing entities (see the EntityType enum)
   * 4 = grid showing the highest threats on each tile (see the EntityKind enum)
 
5. "O": freeze creatures, i.e. they will stop moving
6. "E": render extra debug information about Mario, see _MarioHijackAIBase.debugDraw(...)_ for details
7. "L": render sprite positions within the map
8. "F": Mario will start flying; good for quickly moving forward through the map
9. "Z": Toggle scale x2 of the visualization (scale x2 is broken on some systems, dunno why)
10. "+" / "-": adjust simulator frames per second

## Cheers!
