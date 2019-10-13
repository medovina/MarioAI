Refactored MarioAI code from: http://code.google.com/p/marioai/

# MarioAI Version 0.3.1

![alt tag](https://github.com/medovina/MarioAI/raw/master/MarioAI4J/MatioAI4J.png)

## Overview

This is a Java implementation of the classic video game Super Mario Bros.  You can play it live from the keyboard, but it is primarily intended for experimenting with algorithms that play the game automatically. You can write your own agent that plays the game using the API described below.

## History

Nintendo released the original [Super Mario Bros.](https://en.wikipedia.org/wiki/Super_Mario_Bros.) game for the Nintendo Entertainment System in 1985.  It quickly became a classic and influenced many other games.  (If you have a ROM for the original game, you can play it using the [Nestopia](https://github.com/0ldsk00l/nestopia) emulator.)

Some time around 2006, Markus Persson (creator of Minecraft) wrote a Java program called Infinite Mario Bros., which was like the original Super Mario Bros., but with a random level generator.  The original Infinite Mario Bros. web pages have vanished, but the Java code is freely available and lives on. 

Around 2009, Julian Togelius and Sergey Karakovskiy modified Infinite Mario Bros. by adding an API for computer agents that play the game automatically.  This modified version was called [MarioAI](https://code.google.com/archive/p/marioai/).  It was the basic for a series of [tournaments](http://www.marioai.org/) for AI players for the game.

The code in this repository is derived from [MarioAI 0.2.0](https://code.google.com/archive/p/marioai/), with additional improvements by Jakub Gemrot and [Adam Dingle](https://ksvi.mff.cuni.cz/~dingle/) from the Faculty of Mathematics and Physics at Charles University in Prague.

Meanwhile, a new version of MarioAI (0.8) has recently (in 2019) appeared at [marioai.org](http://www.marioai.org).  Perhaps we will merge in the changes from that version at some point.

## Building the game

This version of MarioAI works with Java 11, 12, or 13, and probably older Java versions as well.

The sources include .project files for Eclipse.  You should easily be able to load them into Eclipse, IntelliJ, or Visual Studio Code, all of which understand the Eclipse .project file format.

## Playing the game

To play the game from the keyboard in a default configuration, run the class RunMario in MarioAI4J/src/ch/idsia/RunMario.java.

The keyboard controls are:

- __left/right arrow keys__: move left/right
- __A__: jump
- __S__: press to shoot; hold down to sprint (move faster)

You will see three kinds of enemies:

- [Goombas](https://en.wikipedia.org/wiki/Goomba) are reddish and are the weakest kind of enemy.  You can kill a Goomba by stepping on it or shooting it.

- [Koopas](https://en.wikipedia.org/wiki/Koopa_Troopa) look like green turtles.  If you shoot a Koopa, it will die.  If you step on one, it will retreat inside its shell.  You can then step on the shell again or kick it to set it in motion.  A moving shell will destroy any enemy that it hits, even a Spiky.  However, beware: a moving shell will also harm you if it hits you.

  If you hold down the S key and move onto a stopped shell, you will pick it up.  You can then carry the shell anywhere you like.  When you are carrying the shell, Mario looks like a raccoon.  Let go of the S key to release the shell and set it in motion again.

- [Spikies](https://www.mariowiki.com/Spiny) (also known as "spinies") are covered with spikes.  You cannot shoot a Spiky, and stepping on one will harm you.  The only way to kill a Spiky is to hit it with a moving Koopa shell.

You (Mario) are always in one of three states:

- Fire Mario: Tall, with a yellow hat.  This is the strongest state: only in this state can you shoot bullets.  You begin in this state.

- Super Mario: Tall, with a red hat.

- Small Mario: Short.  This is the weakest state.

Each time that you take damage by hitting a monster, you move to the next weaker state.  If you are in the short state and hit a monster, you die and the game is over.

As you run through the world, you may collect __coins__ to score points (see "Scoring", below).

If you collect a __flower__ or __mushroom__, you move to the next stronger state. (If you are already in the strongest state, then your state does not change, but you receive a bonus coin).

If you hit a __yellow brick__ from below, it may explode, or it may change to a solid brown brick and reveal a coin, flower, or mushroom.

Some bricks are marked with a __question mark__.  If you hit one of these, it will always reveal a coin, flower, or mushroom.

Sometimes [Piranha Plants](https://www.mariowiki.com/Piranha_Plant) will emerge out of green tubes.  Touching one of these will harm you.

In the default configuration, the world is 256 squares wide.  You want to reach the princess at the right side of the world before time runs out (you have 200 time units by default).  If you reach the princess, you receive a large point bonus and the game ends.  (It would be nice if the game continued with more levels, but currently it does not work this way.)

The world is randomly generated, and will sometimes include platforms or objects that are unreachable, since you cannot jump high enough to get to them.  (It would be nice if the random generator were smart enough to ensure that everything is always reachable, but it is not.)

Other kinds of monsters or goodies may be present if you run the game with non-default configuration options, but those are not documented here.  :)

## Hints

When you press A to jump, you will jump higher if you hold the A key down throughout your jump.

## Scoring

Your current score is displayed at the bottom of the screen.  You gain or lose points as follows:

- shoot a Goomba or Koopa: __0__ points
- step on a Goomba or Koopa: __10__ points
- kill a Goomba, Koopa or Spiky by hitting it with a shell: __10__ points
- collect a coin: __20__ points
- collect a mushroom: __100__ points
- collect a flower: __100__ points
- finish the level by reaching the Princess: __800__ points

## Project structure

**MarioAI4J**: The main project including the runnable class RunMario featuring a keyboard-controlled Mario.

**MarioAI4J-Agents**: Sample agents.  You can run each agent class directly to see how it behaves.

**MarioAI4J-Playground**: Here you can start coding your own Mario AI right away.  Just navigate to the MarioAgent class and fool around.  

**MarioAI4J-Tournament**: Lets you evaluate agents from the console (check the EvaluateAgentConsole class) producing detailed CSV reports.

## Agent API

Here is the [documentation](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/index.html) for the API you can use to build agents to play the game.

The Mario world consists of __tiles__, each of which is 16 x 16 pixels.  By default, the world is 256 tiles wide and 15 tiles high.  So it consists of 4096 x 240 pixels.

All terrain is aligned on tile boundaries.  Using the API, you can determine the terrain in each tile around Mario, represented by the
[Tile](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/benchmark/mario/engine/generalization/Tile.html) enum.  The most common tile types are

- BORDER_CANNOT_PASS_THROUGH (BI) - a solid block
- BORDER_HILL (BH) - a platform that Mario can stand on
- BREAKABLE_BRICK (BB) - a breakable brick
- COIN_ANIM (C) - a coin that Mario can collect
- FLOWER_POT (FP) - a tube that a piranha plant might emerge from
- QUESTION_BRICK (BQ) - a brick with a question mark

The two-letter abbreviations above appear if you enable the tile grid (see "Extra keyboard controls", below).

In the API, an __entity__ is anything that moves, such as the enemy monsters.  Mario can see the entities around him, each of which has an [EntityType](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/benchmark/mario/engine/generalization/EntityType.html).  The most common entity types are

- GOOMBA (G) - a [Goomba](https://en.wikipedia.org/wiki/Goomba)
- GREEN_KOOPA (GK) - a [Koopa](https://en.wikipedia.org/wiki/Koopa_Troopa)
- MARIO (M) - Mario himself
- SHELL_MOVING (SM) - a Koopa shell in motion
- SHELL_STILL (ST) - a Koopa shell that is stopped
- SPIKY (SP) - a [Spiky](https://www.mariowiki.com/Spiny)

The two-letter abbrevations above appear if you enable the entity grid (as described below).

Mario can see all tiles and entities around him that are contained in the __receptive grid__, which is a 19 x 19 grid of tiles centered at Mario's current position.

The API uses two coordinate systems: __pixel coordinates__ and __tile coordinates__.  In both systems, (0, 0) is the upper left-hand corner of the world.  For any entity that Mario can see, you can retrieve its absolute (X, Y) position in pixels.  More precisely, this is the position at the __horizontal center__ of the __bottom__ of the entity.  An entity with position (X, Y) is considered to belong to the tile with coordinates (X / 16, Y / 16).

In your agent, you could only consider each entity's tile coordinates, or you may want to consider its pixel coordinates for a finer degree of control.

[MarioAgent](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/MarioAgent.html)  is the class you will enhance to build your custom agent.  You will need to implement the [actionSelectionAI](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/MarioAgent.html#actionSelectionAI()) method to determine the action that Mario will take on each game tick.

As you can see in the documentation, [MarioAgent](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/MarioAgent.html) inherits from [MarioHijackAIBase](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/agents/controllers/MarioHijackAIBase.html), which in turn inherits from [MarioAIBase](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/agents/controllers/MarioAIBase.html).  This last class contains fields that provide essential information about the game state:

- The field __mario__ is a [MarioEntity](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/benchmark/mario/engine/generalization/MarioEntity.html) object with information about Mario himself, such as

  - mode - current mode (Small Mario, Super Mario or File Mario)
  - speed.x, speed.y - current velocity in pixels/tick
  - sprite.x, sprite.y - absolute position in pixel coordinates

- The field __t__ is a [Tiles](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/agents/controllers/modules/Tiles.html) object with information about tiles around Mario.  Each tile is represented by a [Tile](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/benchmark/mario/engine/generalization/Tile.html) indicating its type.

- The field __e__ is an [Entities](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/agents/controllers/modules/Entities.html) object with information about entities around Mario.  Each of these is represented by an [Entity](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/benchmark/mario/engine/generalization/Entity.html) object, which contains the following useful fields (among others):

  - dX, dY - position relative to Mario, in pixels
  - dTX, dTY - position relative to Mario, in tiles
  - speed.x, speed.y - current velocity in pixels/tick
  - sprite.x, sprite.y - absolute position in pixel coordinates
  - type - an [EntityType](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/benchmark/mario/engine/generalization/EntityType.html)

For more details, see the [API documentation](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/index.html).  It is still somewhat primitive, but I will try to improve it over time.  In the meantime, if you have questions then just [ask](mailto:dingle@ksvi.mff.cuni.cz).

## Extra keyboard controls 

Here are some extra keyboard controls that allows you to visualize / perform extra debugging information, which is extremely useful when developing a custom agent.  (Most of these also work when playing the game with the keyboard.)

- __space__ or __P__: pause the game
- __N__: when the game is paused, step forward by a single frame
- __H__: hijack Mario, i.e. you may control Mario manually and navigate him into a situation you wish to observe
- __G__: cycle between the display of several grids that indicate what is around Mario:

   * 0 = off
   * 1 = grid showing tile coordinates relative to Mario
   * 2 = grid showing tile types (see the [Tile](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/benchmark/mario/engine/generalization/Tile.html) enum)
   * 3 = grid showing entities (see the [EntityType](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/benchmark/mario/engine/generalization/EntityType.html) enum)
   * 4 = grid showing the highest threats on each tile (see the [EntityKind](https://ksvi.mff.cuni.cz/~dingle/2019/ai/mario/html/ch/idsia/benchmark/mario/engine/generalization/EntityKind.html) enum)
 
- __O__: freeze creatures, i.e. they will stop moving
- __E__: render extra debug information about Mario, see _MarioHijackAIBase.debugDraw(...)_ for details
- __L__: render sprite positions within the map
- __F__: Mario will start flying; good for quickly moving forward through the map
- __Z__: Toggle scale x2 of the visualization (scale x2 is broken on some systems, dunno why)
- __+__ / __-__: adjust simulator frames per second

## Physics of the game world

Here is some detailed information about how Mario and creatures move in the game world.

The game proceeds in a series of __ticks__.  By default, it runs at 24 ticks per second.

All enemies move at a constant horizontal velocity of __1.75 pixels/tick__.

Mario accelerates and decelerates as he moves left and right.  If you hold down the left or right arrow key, Mario's horizontal velocity increases until it reaches a maxmimum of __5.5 pixels/tick__.  If you additionally hold down the S (sprint) key, Mario will accelerate more quickly, and will reach a maximum velocity of __10.9 pixels/tick__.

You can accelerate leftward or rightward even in mid-air!  This means that while jumping you have some control over where you will land.  Of course, mid-air horizontal acceleration is not realistic, but this is a video game world.  :)

The height and duration of a jump depends on how long you hold down the A key.  If you tap the A key for the shortest possible jump, your jump will last __10 ticks__ and will reach a maximum height of __26.1 pixels__.  If you hold down the A key for the longest possible jump, your jump will last __17 ticks__ and will reach a maximum height of __66.5 pixels__.  Jumps of intermediate length are also possible.

If you are moving at a constant horizontal speed, the horizontal span of the jump is the jump duration times the velocity.  For example, if you are running at 5.5 pixels/second to the right and make the smallest possible jump, you will travel 10 * 5.5 = 55 pixels horizontally.  If you are sprinting at 10.9 pixels/second and make the largest possible jump, you will travel 17 * 10.9 = 185.3 pixels horizontally, a much greater distance.

For more detail, here is pseudocode explaining how Mario's position and velocity are updated on each tick:

```java
float x, y;    // absolute position in pixel coordinates
float xa = 0, ya = 0;  // current velocity in pixels/tick
int jumpTime = 0;

move() {
  if (key.isPressed(JUMP)) {
    if (onGround && mayJump) {
      jumpTime = 7;    // begin jumping
      ya = -1.9 * jumpTime;
    } else if (jumpTime > 0) {
      ya = -1.9 * jumpTime;
      jumpTime--;
    }
  }
    
  // Accelerate left or right
  float sideWaysSpeed = keys.isPressed(SPRINT) ? 1.2 : 0.6;
  if (key.isPressed(LEFT))
    xa -= sidewaysSpeed;
  if (key.isPressed(RIGHT))
    xa += sidewaysSpeed;
  
  // Update position based on current velocity
  x += xa;
  y += ya;
  
  // Damp velocity due to friction
  xa *= 0.89;
  ya *= 0.85;
  
  // Gravitational acceleration
  if (!onGround)
    ya += 3.0;
}
```

## Changelog

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

## Cheers!
