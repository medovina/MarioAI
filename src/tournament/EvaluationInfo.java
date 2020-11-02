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

package tournament;

import engine.helper.MarioLog;
import engine.sprites.Mario;
import engine.sprites.MarioMode;

public final class EvaluationInfo implements Cloneable {
	
	public static enum EvaluationResult {
		LEVEL_TIMEDOUT,
		MARIO_DIED,
		VICTORY,
		SIMULATION_RUNNING
    }
    
    static final String[] ResultDescription = { "timed out", "died", "victory", "still running" };
	
	private static final int MagicNumberUnDef = -42;

	public int marioStatus = MagicNumberUnDef;
    public int score = MagicNumberUnDef;
	public int distancePassedPhys = MagicNumberUnDef;
	public int flowersDevoured = MagicNumberUnDef;
	public int killsTotal = MagicNumberUnDef;
	public MarioMode marioMode = null;
	public int mushroomsDevoured = MagicNumberUnDef;
	public int coinsGained = MagicNumberUnDef;
	public int timeLeft = MagicNumberUnDef;
	public int timeSpent = MagicNumberUnDef;
	public int levelLength = MagicNumberUnDef;

	public EvaluationResult getResult() {
		switch (marioStatus) {
		case Mario.STATUS_RUNNING:
			return EvaluationResult.SIMULATION_RUNNING;
			
		case Mario.STATUS_WIN:
			return EvaluationResult.VICTORY;
			
		case Mario.STATUS_DEAD:
			return timeLeft <= 0 ? EvaluationResult.LEVEL_TIMEDOUT : EvaluationResult.MARIO_DIED;
			
		default:
			throw new RuntimeException("Invalid evaluation state. Cannot determine result.");
		}

	}

	public int computeKillsTotal() {
		return this.killsTotal;
    }
    
    public float completionFraction() {
        return distancePassedPhys / (levelLength * 16.0f);
    }

    public String summary() {
        return String.format("%10s, distance = %5.1f%%, score = %d",
            ResultDescription[getResult().ordinal()], 100.0 * completionFraction(), score);
    }

	public EvaluationInfo clone() {
		try {
			// TODO:!H!:double check the validity of this change!
			return (EvaluationInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			MarioLog.error(e.toString());
			return null;
		}
	}

	public String getCSVHeader() {
        return "result;distance;score";
	}
	
	public String getCSV() {
        return String.format("%s;%.4f;%d", getResult(), completionFraction(), score);
	}
}
