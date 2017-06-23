package swing;

import java.awt.Color;

import acm.graphics.GRect;

public class BrickCrush {
	public static final int APP_WIDTH = 400;
	public static final int APP_HEIGHT= 600;
	
	private static final int WIDTH = APP_WIDTH;
	private static final int HEIGHT = APP_HEIGHT;
	
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
	
	private static final int PADDLE_Y_OFFSET = 30;
	
	private static final int BRICKS_PER_ROW = 10;
	private static final int BRICK_ROW = 10;
	
	private static final int BRICK_INTERVAL = 4;
	
	private static final int BRICK_WIDTH = 
			(WIDTH - (BRICKS_PER_ROW - 1) * BRICK_INTERVAL)/BRICKS_PER_ROW;
	private static final int BRICK_HEIGHT = 8;
	
	private static final int BALL_RADIUS = 10;
	
	private static final int BRICK_Y_OFFSET = 70;
	
	private static final int TURN = 3;
	
	private static final int DELAY = 20;
	private GRect[][] bricks;
	
	public void makeBrick(){
		int x = 0;
		int y = BRICK_Y_OFFSET;
		GRect brick;
		Color brickColor;
		
		for (int row = 0 ; row < BRICKS_PER_ROW ; row++ ) {
			y += (BRICK_HEIGHT + BRICK_INTERVAL);
			x = BRICK_INTERVAL / 2;
			
			for (int col = 0 ; col < BRICKS_PER_ROW ; col++) {
				switch (row / 2) {
				case 0:
					brickColor = Color.red;
					break;
				case 1:
					brickColor = Color.orange;
					break;
				case 2:
					brickColor = Color.yellow;
					break;
				case 3:
					brickColor = Color.green;
					break;
				default:
					brickColor = Color.blue;
					break;
				}
				brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				x += (BRICK_INTERVAL + BRICK_WIDTH);
				brick.setFilled(true);
				brick.setColor(brickColor);
				brick.setFillColor(brickColor);
				
				bricks[row][col] = brick;
				//add(bricks[row][col]);
			}
		}
	}
}
