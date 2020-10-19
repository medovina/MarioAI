# MarioAI 0.4

![alt tag](https://github.com/medovina/MarioAI/raw/master/MarioAI4J.png)

## Overview

This is a Java implementation of the classic video game Super Mario Bros.  You can play it live from the keyboard, but it is primarily intended for experimenting with algorithms that play the game automatically. You can write your own agent that plays the game using the API described below.

## History

Nintendo released the original [Super Mario Bros.](https://en.wikipedia.org/wiki/Super_Mario_Bros.) game for the Nintendo Entertainment System in 1985.  It quickly became a classic and influenced many other games.  (If you have a ROM for the original game, you can play it using the [Nestopia](https://github.com/0ldsk00l/nestopia) emulator.)

Some time around 2006, Markus Persson (creator of Minecraft) wrote a Java program called Infinite Mario Bros., which was like the original Super Mario Bros., but with a random level generator.  The original Infinite Mario Bros. web pages have vanished, but the Java code is freely available and lives on. 

Around 2009, Julian Togelius and Sergey Karakovskiy modified Infinite Mario Bros. by adding an API for computer agents that play the game automatically.  This modified version was called [MarioAI](https://code.google.com/archive/p/marioai/).  It was the basic for a series of [tournaments](http://www.marioai.org/) for AI players for the game.

The code in this repository is derived from [MarioAI 0.2.0](https://code.google.com/archive/p/marioai/), with various improvements by Jakub Gemrot and [Adam Dingle](https://ksvi.mff.cuni.cz/~dingle/) from the Faculty of Mathematics and Physics at Charles University in Prague.

Meanwhile, a [new version of MarioAI (0.8)](https://github.com/amidos2006/Mario-AI-Framework) has recently appeared (in 2019).  Perhaps we will merge in the changes from that version at some point.

## Building the game

This version of MarioAI works with Java 11 or later, and possibly with older Java versions as well.

The project builds using [Maven](https://maven.apache.org/).  You should easily be able to load it into Eclipse, IntelliJ, or Visual Studio Code.

## Playing the game

To play the game on Linux or macOS, run

```
$ ./mario
```

Or, on Windows:

```
> .\mario
```

By default, the game is controlled by the keyboard and runs at the standard difficulty level.  Various options are available; type './mario -help' to see them.

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

Your current score is displayed at the top of the screen.  You gain or lose points as follows:

- shoot a Goomba or Koopa: __10__ points
- step on a Goomba or Koopa: __20__ points
- kill a Goomba, Koopa or Spiky by hitting it with a shell: __30__ points
- collect a coin: __10__ points
- collect a mushroom: __100__ points
- collect a flower: __100__ points
- finish the level by reaching the Princess: __500__ points

## Difficulty levels
You can run the game at various levels of difficulty:

- LEVEL_0_FLAT: flat ground, empty
- LEVEL_1_JUMPING: hills, no creatures
- LEVEL_2_GOOMBAS: adds Goombas
- LEVEL_3_TUBES: adds tubes with dangerous flowers
- LEVEL_4_SPIKIES: adds Spikies
- LEVEL_5_KOOPAS: adds Koopas (green turtles)
- LEVEL_6_FULL_GAME: the normal game with coins, mushrooms, flowers, etc.
- LEVEL_7_FULL_GAME_HARD: an extra challenge
- LEVEL_8_FULL_GAME_EXTRA_HARD: even tougher

By default, the game runs at difficulty level 6.  You can specify a different level on the command line using the -level option.

Levels 0 - 5 are __training levels__ that have fewer features than the normal game.  They are probably not so interesting to play from the keyboard.  However, if you are writing an agent that plays the game, you may wish to try it on each of these levels in succession.

## Agent API

The package agents.examples contains several sample agents, which you may wish to study as a starting point.

Here is the [documentation](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html) for the API you can use to build agents to play the game.

The Mario world consists of __tiles__, each of which is 16 x 16 pixels.  By default, the world is 256 tiles wide and 15 tiles high.  So it consists of 4096 x 240 pixels.

All terrain is aligned on tile boundaries.  Using the API, you can determine the terrain in each tile around Mario, represented by the
[Tile](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#Tile) enum.  The most common tile types are

- BORDER_CANNOT_PASS_THROUGH (BI) - a solid block
- BORDER_HILL (BH) - a platform that Mario can stand on
- BREAKABLE_BRICK (BB) - a breakable brick
- COIN_ANIM (C) - a coin that Mario can collect
- FLOWER_POT (FP) - a tube that a piranha plant might emerge from
- QUESTION_BRICK (BQ) - a brick with a question mark

The two-letter abbreviations above appear if you enable the tile grid (see "Extra keyboard controls", below).

In the API, an __entity__ is anything that moves, such as the enemy monsters.  Mario can see the entities around him, each of which has an [EntityType](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#EntityType).  The most common entity types are

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

MyAgent is the class you will enhance to build your custom agent.  You will need to implement the [actionSelectionAI](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#MarioAIBase) method to determine the action that Mario will take on each game tick.  In this method, create a MarioInput object to represent the set of pressed keys, then call .press() once for each key that you want to press.

As you can see in the documentation, MyAgent inherits from [MarioAIBase](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#MarioAIBase).  This class contains fields that provide essential information about the game state:

- The field __mario__ is a [MarioEntity](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#MarioEntity) object with information about Mario himself, such as

  - mode - current mode (Small Mario, Super Mario or File Mario)
  - speed.x, speed.y - current velocity in pixels/tick
  - sprite.x, sprite.y - absolute position in pixel coordinates

- The field __tiles__ is a [Tiles](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#Tiles) object with information about tiles around Mario.  Each tile is represented by a [Tile](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#Tile) indicating its type.

- The field __entities__ is an [Entities](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#Entities) object with information about entities around Mario.  Each of these is represented by an [Entity](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#Entity) object, which contains the following useful fields (among others):

  - dX, dY - position relative to Mario, in pixels
  - dTX, dTY - position relative to Mario, in tiles
  - speed.x, speed.y - current velocity in pixels/tick
  - sprite.x, sprite.y - absolute position in pixel coordinates
  - type - an [EntityType](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#EntityType)

In your actionSelectionAI() method, you will probably want to press the JUMP button only when

- mario.mayJump is true, in which case you can begin a jump, or possibly
- when mario.isJumping() is true, meaning that Mario is moving upward, and pressing JUMP will make him go higher.

If you press the button when neither of these conditions is true, then you will not jump, and you will not be able to initiate another jump until you release the button and press it again.

The SPEED button is used both for sprinting and shooting.  If you hold it down (i.e. mark it as pressed on every tick) then Mario will sprint, but will not shoot repeatedly.  Each time you want to shoot, you must release the button and then press it again on a subsequent tick.  You may wish to study the sample ShooterAgent for one idea about how to implement this.

For more details, see the [API documentation](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html).

## Evaluating an agent

You can run any agent from the command line.  For example, to run MyAgent in LEVEL_2_GOOMBAS:

```
$ ./mario MyAgent -level 2
```

The -sim option will run a series of random games without visualization, and will report statistics about an agent's average performance over these games.  For example, to run 50 games of MyAgent in LEVEL_2_GOOMBAS:

```
$ ./mario MyAgent -level 2 -sim 50
```

You can also specify a range of levels, in which case the given number of games will be simulated at each level.  For example, to run 50 games in each level from LEVEL_1_JUMPING through LEVEL_3_TUBES:

```
$ ./mario MyAgent -level 1-3 -sim 50
```

If you want to see the outcome of each individual simulated game, add the -v (verbose) option:

```
$ ./mario MyAgent -level 1-3 -sim 50 -v
```

In this output you will see the random seed that was used for each game.  If you'd like to repeat any individual game, you can rerun that game with its particular seed.  For example, suppose that you see that your agent did poorly in the game with seed 12 on level 2.  You can rerun that game like this:

```
$ ./mario MyAgent -level 2 -seed 12
```

## Extra keyboard controls 

Here are some extra keyboard controls that allows you to visualize / perform extra debugging information, which is useful when developing a custom agent.

- __space__ or __P__: pause the game
- __E__: render extra debug information about Mario
- __F__: Mario will start flying; good for quickly moving forward through the map
- __G__: cycle between the display of several grids that indicate what is around Mario:

   * 0 = off
   * 1 = grid showing tile coordinates relative to Mario
   * 2 = grid showing tile types (see the [Tile](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#Tile) enum)
   * 3 = grid showing entities (see the [EntityType](https://ksvi.mff.cuni.cz/~dingle/2020-1/ai_1/mario/api.html#EntityType) enum)
 
- __H__: hijack Mario, i.e. you may control Mario manually and navigate him into a situation you wish to observe
- __L__: display entity positions within the map.  Press once to show positions relative to Mario,
         or twice to show absolute positions.
- __N__: when the game is paused, step forward by a single frame
- __O__: freeze creatures, i.e. they will stop moving
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
