package agents.controllers;

import java.awt.Graphics;

import engine.core.IEnvironment;
import engine.core.LevelScene;
import engine.graphics.VisualizationComponent;

public interface IMarioDebugDraw {

	public void debugDraw(VisualizationComponent vis, LevelScene level, IEnvironment env, Graphics g);
	
}
