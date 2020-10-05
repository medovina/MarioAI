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

package tools;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import engine.sprites.Mario;
import engine.sprites.Mario.MarioMode;
import utils.MarioLog;

public final class EvaluationInfo implements Cloneable {
	
	public static enum EvaluationResult {
		LEVEL_TIMEDOUT,
		MARIO_DIED,
		VICTORY,
		SIMULATION_RUNNING
	}
	
	private static final int MagicNumberUnDef = -42;

	public static final int numberOfElements = 14;

	// ordered in alphabetical order;
	public int distancePassedCells = MagicNumberUnDef;
	public int distancePassedPhys = MagicNumberUnDef;
	public int flowersDevoured = MagicNumberUnDef;
	public int killsByFire = MagicNumberUnDef;
	public int killsByShell = MagicNumberUnDef;
	public int killsByStomp = MagicNumberUnDef;
	public int killsTotal = MagicNumberUnDef;
	public MarioMode marioMode = null;
	public int marioStatus = MagicNumberUnDef;
	public int mushroomsDevoured = MagicNumberUnDef;
	public int greenMushroomsDevoured = MagicNumberUnDef;
	public int coinsGained = MagicNumberUnDef;
	public int timeLeft = MagicNumberUnDef;
	public int timeSpent = MagicNumberUnDef;
	public int hiddenBlocksFound = MagicNumberUnDef;

	private String taskName = "NoTaskNameSpecified";

	public int totalNumberOfCoins = MagicNumberUnDef;
	public int totalNumberOfHiddenBlocks = MagicNumberUnDef;
	public int totalNumberOfMushrooms = MagicNumberUnDef;
	public int totalNumberOfFlowers = MagicNumberUnDef;
	public int totalNumberOfCreatures = MagicNumberUnDef; // including spiky
															// flowers
	public int levelLength = MagicNumberUnDef;

	public int collisionsWithCreatures = MagicNumberUnDef;

	private static final int[] retFloatArray = new int[EvaluationInfo.numberOfElements];
	private static final int[] zeros = new int[EvaluationInfo.numberOfElements];
	public String Memo = "";

	private static final DecimalFormat df = new DecimalFormat("#.##");

	public int[][] marioTrace;
	public String marioTraceFileName;

	private long evaluationStarted = 0;
	private long evaluationFinished = 0;
	private long evaluationLasted = 0;

	public EvaluationInfo() {
		System.arraycopy(EvaluationInfo.zeros, 0, retFloatArray, 0,
				EvaluationInfo.numberOfElements);
	}
	
	public EvaluationResult getResult() {
		switch (marioStatus) {
		case Mario.STATUS_RUNNING:
			if (timeLeft <= 0) {
				return EvaluationResult.LEVEL_TIMEDOUT;
			} else {
				return EvaluationResult.SIMULATION_RUNNING;
			}
			
		case Mario.STATUS_WIN:
			return EvaluationResult.VICTORY;
			
		case Mario.STATUS_DEAD:
			return EvaluationResult.MARIO_DIED;
			
		default:
			throw new RuntimeException("Invalid evaluation state. Cannot determine result.");
		}

	}

	public float computeDistancePassed() {
		return distancePassedPhys;
	}

	public int computeKillsTotal() {
		return this.killsTotal;
	}

	public String toString() {
		evaluationFinished = System.currentTimeMillis();
		evaluationLasted = evaluationFinished - evaluationStarted;
		// store mario trace:
		try {
			if (marioTraceFileName != null && !marioTraceFileName.equals("")) {
				try (PrintWriter pw = new PrintWriter(new FileWriter(
						marioTraceFileName)) ) {

					for (int j = 0; j < marioTrace[0].length; ++j)

					{
						for (int i = 0; i < marioTrace.length; ++i) {
							pw.print(spaceFormat(marioTrace[i][j]));
						}
						pw.println();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {			
		}

		return      "Evaluation Results for Task : "
				+ taskName
				+ "\n          Evaluation lasted : "
				+ Long.toString(evaluationLasted)
				+ " ms"
				+ "\n               Mario Status : "
				+ ((marioStatus == Mario.STATUS_WIN) ? "WIN!" : "Loss...")
				+ "\n                 Mario Mode : "
				+ marioMode.name()
				+ "\n  Collisions with creatures : "
				+ collisionsWithCreatures
				+ "\n       Passed (Cells, Phys) : "
				+ distancePassedCells
				+ " of "
				+ levelLength
				+ ", "
				+ df.format(distancePassedPhys)
				+ " of "
				+ df.format(levelLength * 16)
				+ " ("
				+ distancePassedCells * 100 / levelLength
				+ "% passed)"
				+ "\n   Time Spent(marioseconds) : "
				+ timeSpent
				+ "\n    Time Left(marioseconds) : "
				+ timeLeft
				+ "\n               Coins Gained : "
				+ coinsGained
				+ " of "
				+ totalNumberOfCoins
				+ " ("
				+ coinsGained * 100
						/ (totalNumberOfCoins == 0 ? 1 : totalNumberOfCoins)
				+ "% collected)"
				+ "\n        Hidden Blocks Found : "
				+ hiddenBlocksFound
				+ " of "
				+ totalNumberOfHiddenBlocks
				+ " ("
				+ hiddenBlocksFound
						* 100
						/ (totalNumberOfHiddenBlocks == 0 ? 1
								: totalNumberOfHiddenBlocks)
				+ "% found)"
				+ "\n         Mushrooms Devoured : "
				+ mushroomsDevoured
				+ " of "
				+ totalNumberOfMushrooms
				+ " found ("
				+ mushroomsDevoured
						* 100
						/ (totalNumberOfMushrooms == 0 ? 1
								: totalNumberOfMushrooms)
				+ "% collected)"
				+ "\n           Flowers Devoured : "
				+ flowersDevoured
				+ " of "
				+ totalNumberOfFlowers
				+ " found ("
				+ flowersDevoured
						* 100
						/ (totalNumberOfFlowers == 0 ? 1 : totalNumberOfFlowers)
				+ "% collected)"
				+ "\n                kills Total : "
				+ killsTotal
				+ " of "
				+ totalNumberOfCreatures
				+ " found ("
				+ killsTotal
						* 100
						/ (totalNumberOfCreatures == 0 ? 1
								: totalNumberOfCreatures) + "%)"
				+ "\n              kills By Fire : " + killsByFire
				+ "\n             kills By Shell : " + killsByShell
				+ "\n             kills By Stomp : " + killsByStomp
				+ ((Memo.equals("")) ? "" : "\nMEMO INFO: " + Memo);
	}

	private String spaceFormat(int i) {
		String r = "" + ((i == 0) ? "." : i);
		while (r.length() < 4)
			r += " ";
		return r;
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

	public void reset() {
		evaluationStarted = System.currentTimeMillis();
	}
	
	public String getCSVHeader() {
		return "result;marioMode;timeLeft;timeSpent;marioStatus;distancePassedCells;distancePassedPhys;flowersDevoured;killsByFire;killsByShell;killsByStomp;killsTotal;mushroomsDevoured"
			   +  ";greenMushroomsDevoured;coinsGained;hiddenBlocksFound;totalNumberOfCoins;totalNumberOfHiddenBlocks;totalNumberOfMushrooms;totalNumberOfFlowers;totalNumberOfCreatures"
			   + ";levelLength;collisionsWithCreatures";
	}
	
	public String getCSV() {
		return getResult() + ";" + marioMode + ";" + timeLeft + ";" + timeSpent + ";" + marioStatus + ";" + distancePassedCells
			   + ";" + distancePassedPhys + ";" + flowersDevoured + ";" + killsByFire + ";" + killsByShell + ";" + killsByStomp + ";" + killsTotal
			   + ";" + mushroomsDevoured + ";" + greenMushroomsDevoured + ";" + coinsGained + ";" + hiddenBlocksFound 
			   + ";" + totalNumberOfCoins + ";" + totalNumberOfHiddenBlocks + ";" + totalNumberOfMushrooms + ";" + totalNumberOfFlowers 
			   + ";" + totalNumberOfCreatures + ";" + levelLength + ";" + collisionsWithCreatures;		
	}
}
