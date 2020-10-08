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

package engine.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import agents.AgentOptions;
import agents.IAgent;
import engine.graphics.VisualizationComponent;
import engine.helper.MarioLog;
import engine.input.MarioInput;
import engine.level.Level;
import engine.sprites.Mario;
import engine.sprites.Sprite;
import options.AIOptions;
import options.SimulatorOptions;
import options.VisualizationOptions;
import tournament.EvaluationInfo;

public final class MarioEnvironment implements IEnvironment {

	private int prevRFH = -1;
	private int prevRFW = -1;
	
	private MarioEntity mario;
	
	/**
	 * Tiles are stored in [row][col] manner!
	 * 
	 * Allocated in {@link #reset(AIOptions)}.
	 * new byte[receptiveFieldHeight][receptiveFieldWidth]
	 * 
	 * For interpretation of respective values consult {@link TileGeneralizer}
	 * and read {@link TileGeneralizer#generalize(byte, int)}.
	 */
	private Tile[][] tileField; 
	
	/**
	 * Entities are stored in [row][col] manner!
	 * 
	 * Allocated in {@link #reset(AIOptions)}.
	 * new byte[receptiveFieldHeight][receptiveFieldWidth]
	 */
	private List<Entity>[][] entityField;    
	
	public List<Entity> entities;

	private final LevelScene levelScene;
	private VisualizationComponent marioVisualComponent;
	private IAgent agent;

	private static final MarioEnvironment ourInstance = new MarioEnvironment();
	private static final EvaluationInfo evaluationInfo = new EvaluationInfo();

	DecimalFormat df = new DecimalFormat("######.#");

	public static MarioEnvironment getInstance() {
		return ourInstance;
	}

	private MarioEnvironment() {
		MarioLog.fine(SimulatorOptions.getBenchmarkName());
		levelScene = new LevelScene();
	}

	public void reset(IAgent agent) {
		if (agent == null) {
			MarioLog.error("MarioEnvironment.reset(agent): agent is NULL! Invalid.");
			throw new RuntimeException("Agent is null, environment cannot be reset.");
		}
		this.agent = agent;
		reset();		
	}

	@SuppressWarnings("unchecked")
	public void reset() {
		if (agent == null) {
			MarioLog.error("MarioEnvironment.reset(): no agent bound to the environment, cannot reset. Use MarioEnvironment.reset(agent) first!");
			throw new RuntimeException("Agent is null, environment cannot be reset, use MarioEnvironment.reset(agent) first!");
		}
		
		mario = new MarioEntity();

		mario.receptiveFieldWidth  = AIOptions.getReceptiveFieldWidth();
		mario.receptiveFieldHeight = AIOptions.getReceptiveFieldHeight();

		if (mario.receptiveFieldHeight != this.prevRFH || mario.receptiveFieldWidth != this.prevRFW) {
			tileField   = new Tile[mario.receptiveFieldHeight][mario.receptiveFieldWidth];
			entityField = new List[mario.receptiveFieldHeight][mario.receptiveFieldWidth];
			for (int row = 0; row < mario.receptiveFieldHeight; ++row) {
				for (int col = 0; col < mario.receptiveFieldWidth; ++col) {
					entityField[row][col] = new ArrayList<Entity>();
				}
			}
			this.prevRFH = mario.receptiveFieldHeight;
			this.prevRFW = mario.receptiveFieldWidth;
		}

		mario.egoRow = AIOptions.getMarioEgoRow();
		mario.egoCol = AIOptions.getMarioEgoCol();
		
		if (mario.egoRow == 9 && mario.receptiveFieldWidth != 19)
			mario.egoRow = mario.receptiveFieldWidth / 2;
		if (mario.egoCol == 9 && mario.receptiveFieldHeight != 19)
			mario.egoCol = mario.receptiveFieldHeight / 2;

		mario.zLevelTiles = AIOptions.getTileGeneralizationZLevel();
		mario.zLevelEntities = AIOptions.getEntityGeneralizationZLevel();
		
		if (VisualizationOptions.isVisualization()) {
			if (marioVisualComponent == null)
				marioVisualComponent = VisualizationComponent.getInstance(this);
			levelScene.reset();
			marioVisualComponent.reset();
			marioVisualComponent.postInitGraphicsAndLevel();
			marioVisualComponent.setAgent(agent);
			marioVisualComponent.setLocation(VisualizationOptions.getViewLocation());
			marioVisualComponent.setAlwaysOnTop(VisualizationOptions.isViewAlwaysOnTop());
		} else
			levelScene.reset();

		entities = new ArrayList<Entity>();

		evaluationInfo.reset();
		agent.reset(new AgentOptions(this));
	}

	public void tick() {
		levelScene.tick();			
		updateMario();
		computeTiles(mario.zLevelTiles);
		computeEntities(mario.zLevelEntities);
		if (SimulatorOptions.isVisualization) {
			marioVisualComponent.tick();
		}
	}

	private void updateMario() {
		mario.sprite = levelScene.mario;
		mario.speed.x = levelScene.mario.x - levelScene.mario.xOld;
		mario.speed.y = levelScene.mario.y - levelScene.mario.yOld;
		if (levelScene.isMarioOnGround()) mario.speed.y = 0;
		mario.height = levelScene.mario.y;
		mario.mode = levelScene.mario.getMode();
		mario.onGround = levelScene.isMarioOnGround();
		mario.mayJump = levelScene.isMarioAbleToJump();
		mario.carrying = levelScene.isMarioCarrying();
		mario.mayShoot = levelScene.isMarioAbleToShoot();
		mario.killsTotal = levelScene.getKillsTotal();
		mario.killsByFire = levelScene.getKillsByFire();
		mario.killsByStomp =  levelScene.getKillsByStomp();
		mario.killsByShell = levelScene.getKillsByShell();
		mario.timeLeft = levelScene.getTimeLeft();
		mario.timeSpent = levelScene.getTimeSpent();
		mario.status = levelScene.getMarioStatus();
		mario.state = levelScene.getMarioState();
		mario.inTileX = (int)mario.sprite.x - mario.sprite.mapX * LevelScene.cellSize; 
		mario.inTileY = (int)mario.sprite.y - mario.sprite.mapY * LevelScene.cellSize; 

	}
	
	private void computeTiles(int ZLevel) {
		int mCol = mario.egoCol;
		int mRow = mario.egoRow;
		for (int y = levelScene.mario.mapY - mRow, row = 0; y <= levelScene.mario.mapY + (mario.receptiveFieldHeight - mRow - 1); y++, row++) {
			for (int x = levelScene.mario.mapX - mCol, col = 0; x <= levelScene.mario.mapX + (mario.receptiveFieldWidth - mCol - 1); x++, col++) {
				if (x >= 0 && x < levelScene.level.length && y >= 0 && y < levelScene.level.height) {
					tileField[row][col] = TileGeneralizer.generalize(levelScene.level.map[x][y],	ZLevel);
				} else {
					tileField[row][col] = Tile.NOTHING;
				}
			}
		}
	}

	public Tile[][] getTileField() {
		return tileField;
	}
	
	private void computeEntities(int ZLevel) {
		for (int w = 0; w < entityField.length; w++)
			for (int h = 0; h < entityField[0].length; h++)
				entityField[w][h].clear();
		entities.clear();
		for (Sprite sprite : levelScene.sprites) {
			if (sprite.isDead() || sprite.kind == levelScene.mario.kind)
				continue;
			// IS SPRITE WITHIN RECEPTIVE FIELD?
			if (sprite.mapX >= 0
					&& sprite.mapX >= levelScene.mario.mapX - mario.egoCol
					&& sprite.mapX <= levelScene.mario.mapX + (mario.receptiveFieldWidth - mario.egoCol - 1)
					&& sprite.mapY >= 0
					&& sprite.mapY >= levelScene.mario.mapY - mario.egoRow
					&& sprite.mapY <= levelScene.mario.mapY + (mario.receptiveFieldHeight - mario.egoRow - 1)
			) {
				// YES IT IS!
				
				int row = sprite.mapY - levelScene.mario.mapY + mario.egoRow;
				int col = sprite.mapX - levelScene.mario.mapX + mario.egoCol;

				EntityType entityType = EntityGeneralizer.generalize(sprite.kind, ZLevel); 
				if (entityType == EntityType.SHELL_STILL || entityType == EntityType.SHELL_MOVING) {
					if (Math.abs(sprite.x - sprite.xOld) > 0.001 || Math.abs(sprite.y - sprite.yOld) > 0.001) {
						entityType = EntityType.SHELL_MOVING;
					} else {
						entityType = EntityType.SHELL_STILL;
					}
				}
				
				Entity entity = new Entity(sprite, entityType, col - mario.egoCol, row - mario.egoRow, levelScene.mario.x - sprite.x, levelScene.mario.y - sprite.y, sprite.y);
				entity.speed.x = entity.sprite.x - entity.sprite.xOld;
				entity.speed.y = entity.sprite.y - entity.sprite.yOld;
				
				entityField[row][col].add(entity);
				
				entities.add(entity);
			}
		}
	}

	public List<Entity>[][] getEntityField() {		
		return entityField;
	}

	public List<String> getObservationStrings(boolean Enemies, boolean LevelMap) {
		List<String> ret = new ArrayList<String>();
		if (levelScene.level != null && levelScene.mario != null) {
			ret.add("Total levelScene length = " + levelScene.level.length);
			ret.add("Total levelScene height = " + levelScene.level.height);
			ret.add("Physical Mario Position (x,y): ("
					+ df.format(levelScene.mario.x) + ","
					+ df.format(levelScene.mario.y) + ")");
			ret.add("Mario Observation (Receptive Field)   Width: "
					+ mario.receptiveFieldWidth + " Height: " + mario.receptiveFieldHeight);
			ret.add("X Exit Position: " + levelScene.level.xExit);
			int MarioXInMap = (int) levelScene.mario.x / LevelScene.cellSize; // TODO:
																				// !!H!
																				// doublcheck
																				// and
																				// replace
																				// with
																				// levelScene.mario.mapX
			int MarioYInMap = (int) levelScene.mario.y / LevelScene.cellSize; // TODO:
																				// !!H!
																				// doublcheck
																				// and
																				// replace
																				// with
																				// levelScene.mario.mapY
			ret.add("Calibrated Mario Position (x,y): (" + MarioXInMap + ","
					+ MarioYInMap + ")\n");

			Tile[][] levelScene = getTileField();
			if (LevelMap) {
				ret.add("~ZLevel: Z" + mario.zLevelTiles + " map:\n");
				for (int x = 0; x < levelScene.length; ++x) {
					String tmpData = "";
					for (int y = 0; y < levelScene[0].length; ++y)
						tmpData += levelSceneCellToString(levelScene[x][y].getCode());
					ret.add(tmpData);
				}
			}

			List<Entity>[][] enemiesObservation = null;
			if (Enemies) {
				enemiesObservation = getEntityField();
				ret.add("~ZLevel: Z" + mario.zLevelEntities + " Enemies Observation:\n");
				for (int x = 0; x < enemiesObservation.length; x++) {
					String tmpData = "";
					for (int y = 0; y < enemiesObservation[0].length; y++) {
						// if (x >=0 && x <= level.xExit)
						if (enemiesObservation[x][y].size() > 0) {
							tmpData += enemiesObservation[x][y].get(0).type.getCode();
						} else {
							tmpData += EntityType.NOTHING.getCode();
						}
					}
					ret.add(tmpData);
				}
			}

		} else {
			ret.add("~[MarioAI ERROR] level : " + levelScene.level + " mario : " + levelScene.mario);
		}
		return ret;
	}

	private String levelSceneCellToString(int el) {
		String s = "";
		if (el == 0 || el == 1)
			s = "##";
		s += (el == levelScene.mario.kind) ? "#M.#" : el;
		while (s.length() < 4)
			s += "#";

		return s + " ";
	}

	@Override
	public List<Entity> getEntities() {
		return entities;
	}

	public void performAction(MarioInput action) {
		levelScene.performAction(action);
	}

	public boolean isLevelFinished() {
		return levelScene.isLevelFinished();
	}

	public EvaluationInfo getEvaluationInfo() {
		computeEvaluationInfo();
		return evaluationInfo;
	}

	public MarioEntity getMario() {
		return mario;
	}
	
	public Mario getMarioSprite() {
		return levelScene.mario;
	}

	public int getTick() {
		return levelScene.tickCount;
	}

	public int getLevelDifficulty() {
		return levelScene.getLevelDifficulty();
	}

	public long getLevelSeed() {
		return levelScene.getLevelSeed();
	}

	public int getLevelType() {
		return levelScene.getLevelType();
	}

	public int getLevelLength() {
		return levelScene.getLevelLength();
	}

	public int getLevelHeight() {
		return levelScene.getLevelHeight();
	}

	public int getTimeLeft() {
		return levelScene.getTimeLeft();
	}

	public Level getLevel() {
		return levelScene.level;
	}

	private void computeEvaluationInfo() {
        evaluationInfo.marioStatus = levelScene.getMarioStatus();
        evaluationInfo.score = levelScene.getScore();
		evaluationInfo.flowersDevoured = Mario.flowersDevoured;
		evaluationInfo.distancePassedPhys = (int) levelScene.mario.x;
		evaluationInfo.distancePassedCells = levelScene.mario.mapX;
		evaluationInfo.timeSpent = levelScene.getTimeSpent();
		evaluationInfo.timeLeft = levelScene.getTimeLeft();
		evaluationInfo.coinsGained = Mario.coins;
		evaluationInfo.totalNumberOfCoins = Level.counters.coinsCount;
		evaluationInfo.totalNumberOfHiddenBlocks = Level.counters.hiddenBlocksCount;
		evaluationInfo.totalNumberOfFlowers = Level.counters.flowers;
		evaluationInfo.totalNumberOfMushrooms = Level.counters.mushrooms;
		evaluationInfo.totalNumberOfCreatures = Level.counters.creatures;
		evaluationInfo.marioMode = levelScene.getMarioMode();
		evaluationInfo.mushroomsDevoured = Mario.mushroomsDevoured;
		evaluationInfo.killsTotal = levelScene.getKillsTotal();
		evaluationInfo.killsByStomp = levelScene.getKillsByStomp();
		evaluationInfo.killsByFire = levelScene.getKillsByFire();
		evaluationInfo.killsByShell = levelScene.getKillsByShell();
		evaluationInfo.hiddenBlocksFound = Mario.hiddenBlocksFound;
		evaluationInfo.collisionsWithCreatures = Mario.collisionsWithCreatures;
		evaluationInfo.Memo = levelScene.memo;
		evaluationInfo.levelLength = levelScene.level.length;
		evaluationInfo.greenMushroomsDevoured = Mario.greenMushroomsDevoured;
	}

	public IAgent getAgent() {
		return this.agent;				
	}
	
	public void setAgent(IAgent agent) {
		this.agent = agent;
	}

	public int getScore() {
		return levelScene.getScore();
	}

	public int getTimeSpent() {
		return levelScene.getTimeSpent();
	}

	public byte[][] getScreenCapture() {
		return null;
	}

	@Override
	public LevelScene getLevelScene() {
		return levelScene;
	}
	
	@Override
	public VisualizationComponent getVisualization() {
		return marioVisualComponent;
	}

}
