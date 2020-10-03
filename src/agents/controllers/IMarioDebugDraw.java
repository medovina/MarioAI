package agents.controllers;

import java.awt.Graphics;

import benchmark.mario.engine.LevelScene;
import benchmark.mario.engine.VisualizationComponent;
import benchmark.mario.environments.IEnvironment;

public interface IMarioDebugDraw {

	public void debugDraw(VisualizationComponent vis, LevelScene level, IEnvironment env, Graphics g);
	
}
