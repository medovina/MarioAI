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

import options.MarioOptions.IntOption;

/**
 * @author Sergey Karakovskiy
 * @see utils.ParameterContainer
 * @see options.SimulationOptions
 * 
 * Read AI values from {@link MarioOptions}.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */

public final class AIOptions extends SimulationOptions {

	/**
	 * Auto-adjusts certain AI options.
	 * <br/><br/>
	 * Currently do not do anything, reserved for future use.
	 */
	public static void reset() {		
		SimulatorOptions.receptiveFieldWidth = getReceptiveFieldWidth();
		SimulatorOptions.receptiveFieldHeight = getReceptiveFieldHeight();
		if (getMarioEgoCol() == 9 && SimulatorOptions.receptiveFieldWidth != 19)
			SimulatorOptions.marioEgoCol = SimulatorOptions.receptiveFieldWidth / 2;
		else
			SimulatorOptions.marioEgoCol = getMarioEgoCol();
		if (getMarioEgoRow() == 9
				&& SimulatorOptions.receptiveFieldHeight != 19)
			SimulatorOptions.marioEgoRow = SimulatorOptions.receptiveFieldHeight / 2;
		else
			SimulatorOptions.marioEgoRow = getMarioEgoRow();


	}

	public static int getReceptiveFieldWidth() {
		return MarioOptions.getInstance().getInt(IntOption.AI_RECEPTIVE_FIELD_WIDTH);
	}
	
	public static int getReceptiveFieldHeight() {
		return MarioOptions.getInstance().getInt(IntOption.AI_RECEPTIVE_FIELD_HEIGHT);
	}
	
	public static int getMarioEgoCol() {
		return MarioOptions.getInstance().getInt(IntOption.AI_MARIO_EGO_COLUMN);
	}
	
	public static int getMarioEgoRow() {
		return MarioOptions.getInstance().getInt(IntOption.AI_MARIO_EGO_ROW);
	}
	
	public static int getTileGeneralizationZLevel() {
		return MarioOptions.getInstance().getInt(IntOption.AI_TILE_GENERALIZATION_ZLEVEL);
	}
	
	public static int getEntityGeneralizationZLevel() {
		return MarioOptions.getInstance().getInt(IntOption.AI_ENTITY_GENERALIZATION_ZLEVEL);
	}
}
