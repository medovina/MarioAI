/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package options;

import engine.graphics.VisualizationComponent;

public abstract class SimulatorOptions {
	
	public enum ReceptiveFieldMode {
		NONE(0),
		GRID(1),
		GRID_TILES(2),
		GRID_ENTITIES(3),
		;
		
		private int code;

		private ReceptiveFieldMode(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
		
		public static ReceptiveFieldMode getForCode(int code) {
			code = code % (ReceptiveFieldMode.values().length);
			for (ReceptiveFieldMode mode : ReceptiveFieldMode.values()) {
				if (mode.getCode() == code) return mode;
			}
			return null;
		}
    }
    
    public static final int LABEL_NONE = 0, LABEL_RELATIVE = 1, LABEL_ABSOLUTE = 2;
	
	public static int showLabels = LABEL_NONE;
	public static Integer FPS = 24;
	public static int MaxFPS = 100;
	public static boolean areFrozenCreatures = false;

	public static boolean isVisualization = true;
	public static boolean isGameplayStopped = false;
	public static boolean isFly = false;

	public static int mariosecondMultiplier = 15;

	public static boolean isPowerRestoration;

	// required for rendering grid in engine/sprites/Sprite.java
	public static int receptiveFieldWidth = 19;
	public static int receptiveFieldHeight = 19;
	public static ReceptiveFieldMode receptiveFieldMode = ReceptiveFieldMode.NONE;
	public static int marioEgoCol = 9;
	public static int marioEgoRow = 9;

	private static VisualizationComponent marioVisualComponent;
	public static int VISUAL_COMPONENT_WIDTH = 320;
	public static int VISUAL_COMPONENT_HEIGHT = 240;

	public static boolean isScale2x = false;
	public static boolean isRecording = false;
	public static boolean isReplaying = false;
	public static boolean nextFrameIfPaused = false;

	public static String getBenchmarkName() {
		return "Mario AI";
	}

	public static void registerMarioVisualComponent(VisualizationComponent mc) {
		marioVisualComponent = mc;
	}

	public static void AdjustMarioVisualComponentFPS() {
		if (marioVisualComponent != null)
			marioVisualComponent.adjustFPS();
	}
}
