package agents.controllers;

import java.awt.Graphics;

import engine.LevelScene;
import engine.VisualizationComponent;
import environments.IEnvironment;

public interface IMarioDebugDraw {

	public void debugDraw(VisualizationComponent vis, LevelScene level, IEnvironment env, Graphics g);
	
}
