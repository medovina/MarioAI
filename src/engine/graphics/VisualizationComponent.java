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

package engine.graphics;


import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.VolatileImage;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JFrame;

import agents.IAgent;
import agents.controllers.IMarioDebugDraw;
import engine.core.Entity;
import engine.core.EntityType;
import engine.core.LevelScene;
import engine.core.MarioEnvironment;
import engine.core.Tile;
import engine.helper.MarioLog;
import engine.input.MarioCheatKey;
import engine.level.BgLevelGenerator;
import engine.level.Level;
import engine.sprites.*;
import options.SimulatorOptions;
import options.VisualizationOptions;
import options.SimulatorOptions.ReceptiveFieldMode;

public class VisualizationComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	public int width, height, scale;

	public VolatileImage thisVolatileImage;
	public Graphics thisVolatileImageGraphics;
	public Graphics thisGraphics;

	private MarioEnvironment marioEnvironment;
	private LevelRenderer layer;
	private BgRenderer[] bgLayer = new BgRenderer[2];

	private Mario mario;
    private Level level;
    
    int xCam, yCam;

	final private static DecimalFormat df = new DecimalFormat("00");
	final private static DecimalFormat df2 = new DecimalFormat("000");

	private long tm = System.currentTimeMillis();
	private long tm0;
	int delay;
	private KeyListener prevHumanKeyBoardAgent;
	private static VisualizationComponent marioVisualComponent = null;

    private Scale2x scale2x;
    Font smallFont = new Font(Font.SANS_SERIF, Font.PLAIN, 8);

	private VisualizationComponent(MarioEnvironment marioEnvironment) {
		this.marioEnvironment = marioEnvironment;
		adjustFPS();

		this.setFocusable(true);
		this.setEnabled(true);
		this.width = VisualizationOptions.getViewportWidth();
        this.height = VisualizationOptions.getViewportHeight();
        this.scale = VisualizationOptions.getScale();

		Dimension size = new Dimension(width * scale, height * scale);

		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		setFocusable(true);

        if (scale > 1)
		    scale2x = new Scale2x(width, height, scale);

		SimulatorOptions.registerMarioVisualComponent(this);
	}

	public static VisualizationComponent getInstance(MarioEnvironment marioEnvironment) {
		if (marioVisualComponent == null) {
			marioVisualComponent = new VisualizationComponent(marioEnvironment);
			marioVisualComponent.CreateMarioComponentFrame(marioVisualComponent);
		}
		return marioVisualComponent;
	}

	private static JFrame marioComponentFrame = null;

	public void CreateMarioComponentFrame(VisualizationComponent m) {
		if (marioComponentFrame == null) {
			marioComponentFrame = new JFrame(SimulatorOptions.getBenchmarkName());
			marioComponentFrame.setContentPane(m);
			m.init();
            marioComponentFrame.pack();
            marioComponentFrame.setLocationRelativeTo(null);  // center window on screen
			marioComponentFrame.setResizable(false);
			marioComponentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		marioComponentFrame.setVisible(true);
		m.postInitGraphics();
	}

	public void setLocation(Point location) {
		marioComponentFrame.setLocation(location.x, location.y);
	}

	public void setAlwaysOnTop(boolean b) {
		marioComponentFrame.setAlwaysOnTop(b);
	}

	public void reset() {
		adjustFPS();
		tm = System.currentTimeMillis();
		this.tm0 = tm;
	}
	
	public void tick() {
		this.render(thisVolatileImageGraphics);

		if (mario.keys.isPressed(MarioCheatKey.CHEAT_KEY_WIN))
			mario.win();

		if (!this.hasFocus() && (tm - tm0) / (delay + 1) % 42 < 20) {
			String msgClick = "CLICK TO PLAY";
			drawString(thisVolatileImageGraphics, msgClick,
					160 - msgClick.length() * 4, 110, 2);
		}

        if (scale > 1)
            thisGraphics.drawImage(scale2x.scale(thisVolatileImage), 0, 0, null);
        else
            thisGraphics.drawImage(thisVolatileImage, 0, 0, null);

		// Delay depending on how far we are behind.
		if (delay > 0) {
			try {
				tm += delay;
				Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
			} catch (InterruptedException ignored) {
			}
		}
	}

	private int recordIndicator = 20;

	public void render(Graphics g) {
		xCam = (int) mario.x - 160;
		yCam = (int) mario.y - 120;

        if (xCam < 0)
            xCam = 0;
        if (yCam < 0)
            yCam = 0;
        if (xCam > level.length * LevelScene.cellSize - SimulatorOptions.VISUAL_COMPONENT_WIDTH)
            xCam = level.length * LevelScene.cellSize - SimulatorOptions.VISUAL_COMPONENT_WIDTH;
        if (yCam > level.height * LevelScene.cellSize - SimulatorOptions.VISUAL_COMPONENT_HEIGHT)
            yCam = level.height * LevelScene.cellSize - SimulatorOptions.VISUAL_COMPONENT_HEIGHT;

		for (int i = 0; i < bgLayer.length; i++) {
			bgLayer[i].setCam(xCam, yCam);
			bgLayer[i].render(g);
		}

		g.translate(-xCam, -yCam);
		
		for (Sprite sprite : marioEnvironment.getLevelScene().sprites)
			if (sprite.layer == 0)
				sprite.render(g);

		g.translate(xCam, yCam);

		layer.setCam(xCam, yCam);
		layer.render(g, marioEnvironment.getTick());

		g.translate(-xCam, -yCam);

		Sprite mario = null;
		
		for (Sprite sprite : marioEnvironment.getLevelScene().sprites) {
			// Mario, creatures
			if (sprite.layer == 1) {
				sprite.render(g);
				if (sprite.kind == Sprite.KIND_MARIO) {
					mario = sprite;
				}
			}
        }
        
        int labels = SimulatorOptions.showLabels;
        if (labels != SimulatorOptions.LABEL_NONE) {
            boolean relative = labels == SimulatorOptions.LABEL_RELATIVE;
            g.setFont(smallFont);
            FontMetrics fm = g.getFontMetrics();

            for (Sprite sprite : marioEnvironment.getLevelScene().sprites) {
                if (sprite instanceof Sparkle ||
                    sprite instanceof Mario && relative)
                    continue;

                String s = String.format("%.1f, %.1f",
                    relative ? sprite.x - mario.x : sprite.x,
                    relative ? sprite.y - mario.y : sprite.y);
                g.drawString(s, (int) sprite.x - fm.stringWidth(s) / 2,
                                (int) sprite.y - sprite.yPicO);
            }
        }
		
		// Mario Grid Visualization Enable
		if (SimulatorOptions.receptiveFieldMode != ReceptiveFieldMode.NONE) {
			renderReceptiveField(mario, g);
		}

		g.translate(xCam, yCam);
        g.setColor(Color.BLACK);
        
        drawStringDropShadow(g,	" SCORE", 0, 0, 2);
        String scoreString =
            String.format("%6s", Integer.toString(marioEnvironment.getScore()));
        drawStringDropShadow(g, scoreString, 0, 1, 2);

		g.drawImage(Art.level[0][2], 122, 10, 10, 10, null);
		drawStringDropShadow(g, "x" + df.format(Mario.coins), 16, 1, 4);

		g.drawImage(Art.items[1][0], 164, 10, 11, 11, null);
		drawStringDropShadow(g, "x" + df.format(Mario.flowersDevoured), 22, 1,
				4);

		if (SimulatorOptions.isRecording) {
			--recordIndicator;
			if (recordIndicator >= 0) {
				g.setColor(Color.RED);
				g.fillOval(303, 4, 13, 13);// 19 * 8 + 5, 39, 10, 10);
				g.setColor(Color.black);
				g.drawOval(303, 4, 13, 13);// 19 * 8 + 5, 39, 10, 10);
			} else if (recordIndicator == -20)
				recordIndicator = 20;
		}
		if (SimulatorOptions.isReplaying) {
			g.setColor(new Color(0, 200, 0));
			g.fillPolygon(new int[] { 303, 303, 316 }, new int[] { 16, 4, 10 },
					3);
			g.setColor(Color.black);
			g.drawPolygon(new int[] { 303, 303, 316 }, new int[] { 16, 4, 10 },
					3);
		}

		drawStringDropShadow(g, "TIME", 33, 0, 7);
		int time = marioEnvironment.getTimeLeft();

		drawStringDropShadow(g, " " + df2.format(time), 33, 1, time < 0 ? 3
				: time < 50 ? 1 : time < 100 ? 4 : 7);

		if (marioEnvironment.getAgent() instanceof IMarioDebugDraw) {
			((IMarioDebugDraw)marioEnvironment.getAgent()).debugDraw(
                this, marioEnvironment.getLevelScene(), marioEnvironment, g);
		}

	}
	
	private Font numFont = new Font("Arial", Font.PLAIN, 8);

	private void renderReceptiveField(Sprite mario, Graphics og) {			
		og.drawString("Matrix View", mario.xPic - 40, mario.yPic - 20);
		int height = SimulatorOptions.receptiveFieldHeight;

		int rows = SimulatorOptions.receptiveFieldHeight;
		int columns = SimulatorOptions.receptiveFieldWidth;

		int marioCol = SimulatorOptions.marioEgoCol;
		int marioRow = SimulatorOptions.marioEgoRow;
		
		int marioX = (((int)mario.x) / 16) * 16 + 8;
		int marioY = (((int)mario.y) / 16) * 16 + 16;

		int cellHeight = 16;
		int k;
		// horizontal lines
		og.setColor(Color.BLACK);
		for (k = -marioRow - 1 ; k < rows
				- marioRow; k++) {
			og.drawLine(
					(int) marioX - marioCol * cellHeight - 8,
					(int) (marioY + k * cellHeight), (int) marioX
							+ (columns - marioCol) * cellHeight - 8,
					(int) (marioY + k * cellHeight));
		}

		// vertical lines
		int cellWidth = 16;
		for (k = -marioCol - 1 ; k < columns - marioCol ; k++) {
			og.drawLine((int) (marioX + k * cellWidth + 8), (int) marioY - marioRow
					* cellHeight - 16, (int) (marioX + k
					* cellWidth + 8), (int) marioY + (height - marioRow)
					* cellHeight - 16);
		}
		
		switch (SimulatorOptions.receptiveFieldMode) {
		case NONE:
			break;
		case GRID:			
			og.setColor(Color.DARK_GRAY);
			og.setFont(numFont);
			for (int row = 0; row < SimulatorOptions.receptiveFieldHeight; ++row) {
				for (int col = 0; col < SimulatorOptions.receptiveFieldWidth; ++col) {
					if (row == marioRow && col == marioCol) continue;
					int x = (int)(marioX + cellWidth * (col - marioCol) - 8);
					int y = (int)(marioY + cellHeight * (row - marioRow) - 16);
					og.drawString(String.valueOf(col - marioCol), x+1, y+8);
					og.drawString(String.valueOf(row - marioRow), x+1, y+16);
				}
			}
			break;
		case GRID_TILES:			
			for (int row = 0; row < SimulatorOptions.receptiveFieldHeight; ++row) {
				for (int col = 0; col < SimulatorOptions.receptiveFieldWidth; ++col) {
					Tile tile = marioEnvironment.getTileField()[row][col];
					if (tile == Tile.NOTHING) continue;
					
					int x = (int)(marioX + cellWidth * (col - marioCol) - 8);
					int y = (int)(marioY + cellHeight * (row - marioRow) - 8);
					
					drawString(og, tile.getDebug(), x, y, 7);
				}
			}
			break;
		case GRID_ENTITIES:			
			for (Entity entity : marioEnvironment.getEntities()) {
				EntityType type = entity.type;
				if (type == EntityType.NOTHING) continue;
				
				int x = (int)(marioX + cellWidth * entity.dTX - 8);
				int y = (int)(marioY + cellHeight * entity.dTY - 8);
				
				drawString(og, type.getDebug(), x, y, 7);
			}
			break;
		}
		
		{
			// MARIO
			int x = (int)(marioX - 8);
			int y = (int)(marioY - 8);
			
			drawString(og, "G" + SimulatorOptions.receptiveFieldMode.getCode(), x, y - 8, 4);
			drawString(og, EntityType.MARIO.getDebug(), x, y, 7);
		}
	}

	public static void drawStringDropShadow(Graphics g, String text, int x,	int y, int color) {
		drawString(g, text, x * 8 + 5, y * 8 + 5, 0);
		drawString(g, text, x * 8 + 4, y * 8 + 4, color);
	}

	public static void drawString(Graphics g, String text, int x, int y, int color) {
		char[] ch = text.toCharArray();
		for (int i = 0; i < ch.length; i++)
			g.drawImage(Art.font[ch[i] - 32][color], x + i * 8, y, null);
    }
    
    /* Draw a line from (x1, y1) to (x2, y2), which are in pixel coordinates relative to Mario. */
    public void drawLine(Graphics g, int x1, int y1, int x2, int y2, Color color) {
        g.translate((int) mario.x - xCam, (int) mario.y - yCam);

        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);

        g.translate(xCam - (int) mario.x, yCam - (int) mario.y);
    }

    /* Draw a rectangle with corners at (x1, y1) and (x2, y2), which are
     * in pixel coordinates relative to Mario. */
    public void drawRect(Graphics g, int x1, int y1, int x2, int y2, Color color) {
        g.translate((int) mario.x - xCam, (int) mario.y - yCam);

        g.setColor(color);
        g.drawRect(x1, y1, x2 - x1, y2 - y1);

        g.translate(xCam - (int) mario.x, yCam - (int) mario.y);
    }

	private static GraphicsConfiguration graphicsConfiguration;

	public void init() {
		graphicsConfiguration = getGraphicsConfiguration();
		Art.init(graphicsConfiguration);
	}

	public void postInitGraphics() {
		this.thisVolatileImage = this.createVolatileImage(
				SimulatorOptions.VISUAL_COMPONENT_WIDTH,
				SimulatorOptions.VISUAL_COMPONENT_HEIGHT);
		this.thisGraphics = getGraphics();
		this.thisVolatileImageGraphics = this.thisVolatileImage.getGraphics();
	}

	public void postInitGraphicsAndLevel() {
		if (graphicsConfiguration != null) {

			this.mario = marioEnvironment.getMarioSprite();
			this.level = marioEnvironment.getLevel();
			layer = new LevelRenderer(level, graphicsConfiguration, this.width,
					this.height);
			for (int i = 0; i < bgLayer.length; i++) {
				int scrollSpeed = 4 >> i;
				int w = ((level.length * 16) - SimulatorOptions.VISUAL_COMPONENT_WIDTH)
						/ scrollSpeed + SimulatorOptions.VISUAL_COMPONENT_WIDTH;
				int h = ((level.height * 16) - SimulatorOptions.VISUAL_COMPONENT_HEIGHT)
						/ scrollSpeed + SimulatorOptions.VISUAL_COMPONENT_HEIGHT;
				Level bgLevel = BgLevelGenerator.createLevel(w / 32 + 1,
						h / 32 + 1, i == 0, marioEnvironment.getLevelType());
				bgLayer[i] = new BgRenderer(bgLevel, graphicsConfiguration,
						SimulatorOptions.VISUAL_COMPONENT_WIDTH,
						SimulatorOptions.VISUAL_COMPONENT_HEIGHT, scrollSpeed);
			}
		} else
			throw new Error(
					"[Mario AI : ERROR] : Graphics Configuration is null. Graphics initialization failed");
	}

	public void adjustFPS() {
		int fps = SimulatorOptions.FPS;
		delay = (fps > 0) ? (fps >= SimulatorOptions.MaxFPS) ? 0 : (1000 / fps)
				: 100;
	}

	// This method is here solely for the displaying information in order to reduce
	// amount of info passed between Env and VisComponent

	public void setAgent(IAgent agent) {
		if (agent instanceof KeyListener) {
			if (prevHumanKeyBoardAgent != null) {
				MarioLog.trace("[MarioVisualComponent] ~ Unregistering OLD agent's KeyListener callback...");
				this.removeKeyListener(prevHumanKeyBoardAgent);
			}
			MarioLog.trace("[MarioVisualComponent] ~ Registering agent's KeyListener callback...");			
			
			this.prevHumanKeyBoardAgent = (KeyListener) agent;
			this.addKeyListener(this.prevHumanKeyBoardAgent);
		} else {
			if (prevHumanKeyBoardAgent != null) {
				MarioLog.trace("[MarioVisualComponent] ~ Unregistering OLD agent's KeyListener callback...");
				this.removeKeyListener(prevHumanKeyBoardAgent);
				this.prevHumanKeyBoardAgent = null;
			}
		}
	}
}
