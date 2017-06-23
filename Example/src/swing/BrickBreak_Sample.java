package swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GPoint;
import acm.graphics.GPolygon;
import acm.graphics.GRect;
import acm.graphics.GRoundRect;
import acm.program.GraphicsProgram;

/**
 * Implement the game of BrickBreak. - ���� ����
 * 
 * ���� �������� �����Ͻÿ�. - ������ ��ġ�� ���� �Ϸ�
 * 
 * ������ �浹�� üũ�Ͻÿ�. - ���� �浹 �̹� ���� �Ϸ�
 * 
 * �������� �浹�� üũ - getElementAt() �Լ��� �̿��� ��. ���⼭ ������ ������ �浹����� ������ ���ٰ� �Ǵ��ؼ�
 * BBbyKoo(breakPointX, breakPointY); �� ��� �ڼ��� ������ �ּ��� ���� ���� ���� ������
 * getElementAt�� �����Ͽ��� BBbygetElementAt(bx, by); �� �⺻ �������� ������ ��
 * 
 * ���ϰ��� �浹�� üũ�ϰ� �ε��� ��ġ�� ���� ���� �ӷ��� ����� ��. - �ܼ� â�� ����
 * ��Ÿ - changeMap()�� ���� �پ��� �� �߰� �̰� ���� ���� makeBricks �ּ�ó���ϰ� ����� ��
 * @author 12080854 ����ö
 * @since 2013-11-20
 * @version 1.1
 */

public class BrickBreak_Sample extends GraphicsProgram {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7017105895868624700L;
	
	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1)
			* BRICK_SEP)
			/ NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** Pause time between ball moves */
	private static final int DELAY = 20;

	private GRoundRect ball;
	private double dx; /* x �ӵ� */
	private double dy; /* y �ӵ� */
	private double paddleX; /* Paddle�� x ��ǥ */
	private double paddleY; /* Paddle�� y ��ǥ */
	private GRect[][] bricks = new GRect[NBRICK_ROWS][NBRICKS_PER_ROW];
	private int turn = NTURNS;
	private GLabel gl;
	private GRect paddle;
	private int[] scopeX = new int[31]; // ����
	private int[] scopeY = new int[11]; // ����

	public void run() {
		makeScope();
		makeFrame(); // frame ����
		 makeBricks(); // ��������
//		changeMap(); // �پ��� �� �߰�
		makePaddle(); // ���� ����
		makeBall(); // Ŭ���ϸ� �� ����
		makeLabel("You have " + turn + "turns remaining, Click to proceed");
		waitForClick();
		while (true) {
			ballBounce(); // �� ƨ��� �Լ�
			pause(DELAY); // 0.002�� �������� ������
			if (turn == 0) {
				makeLabel("FAIL !!! THE END");
				System.exit(1); // ���� ���� �� ���� �����
			}
			 checkBrick();
		}
	}

	/**
	 * ���� �ٱ����� �����ϱ�
	 */
	private void checkBrick() {
		int count = 0;
		for (int i = 0; i < NBRICK_ROWS; i++) {
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				if (bricks[j][i] == null)
					count++;
				if (count == 100)
					System.exit(1);// ������ �ٱ����� ����

			}
		}
	}

	/**
	 * getElementAt�� Ȱ���Ͽ� ������ �浹�� �����ϰ� ������ �����ϴ� �Լ�
	 * 
	 * 
	 */
	public void BBbygetElementAt(double bx, double by) {

		if (getElementAt(bx + BALL_RADIUS - BRICK_SEP - 1, by - 1) != null
				&& getElementAt(bx, by + BALL_RADIUS) != paddle) {
			remove(getElementAt(bx + BALL_RADIUS - BRICK_SEP, by - 1));
			if (getElementAt(bx + BALL_RADIUS + BRICK_SEP, by - 1) != null)
				remove(getElementAt(bx + BALL_RADIUS + BRICK_SEP, by - 1));
			// ���� ���� 2�� ��� ���� �¾��� ��� ������ 2���� �Ǿ���Ѵ�.

			dy = -dy; // ������ �±� ������ y�ӵ��� ���Ѵ�.
		} else if (getElementAt(bx - 1, by + BALL_RADIUS) != null
				&& getElementAt(bx + BALL_RADIUS, by + BALL_RADIUS * 2) != paddle) {
			remove(getElementAt(bx - 1, by + BALL_RADIUS));
			dx = -dx; // ������ �¾��� ��츦 ���� ��
		} else if (getElementAt(bx + 2 * BALL_RADIUS + 1, by + BALL_RADIUS) != null
				&& getElementAt(bx + BALL_RADIUS, by + BALL_RADIUS) != paddle) {
			remove(getElementAt(bx + 2 * BALL_RADIUS + 1, by + BALL_RADIUS));
			dx = -dx; // ������ �¾��� ��츦 ���� ��
		}

	}

	/**
	 * �� �Լ��� �浹�� ���̴� ������ Ȯ���ϱ� ���� �� �߱� �Լ��� �׽�Ʈ �뵵���� ����
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public void makeLine(int a, int b, int c, int d) {
		GLine tmp = new GLine(a, b, c, d);
		tmp.setVisible(true);
		tmp.setColor(Color.black);
		add(tmp);

	}

	/**
	 * ���� � ���Ҵ��� �˷��ְ� �ڵ����� �Ѿ��.
	 * 
	 * @param str
	 */
	public void makeLabel(String str) {
		gl = new GLabel(str);
		gl.setColor(Color.black);
		gl.setVisible(true);
		gl.setLocation(new GPoint((WIDTH / 2 - (str.length()) * 3), HEIGHT / 2));

		remove(paddle);
		makePaddle(); // ������ �ʱ�ȭ ���ش�.
		add(gl);
		try {
			Thread.sleep(2000);// 2�� �ִٰ� �ڵ����� ����

		} catch (Exception e) {
			// TODO: handle exception
		}
		remove(gl);
	}

	/**
	 * ������ ����� �Լ� addMouseMotionListener()�� ���� ���콺�� ��ǿ� ���� ������ ��ġ�� �����ȴ�.
	 * 
	 */
	public void makePaddle() {
		paddle = new GRect(APPLICATION_WIDTH / 2 - (PADDLE_WIDTH / 2),
				APPLICATION_HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH,
				PADDLE_HEIGHT); // ������ �ʱ� ��ġ�� ũ�⿡ ���� �����ϸ鼭 �Ҵ�
		paddle.setFilled(true); // ��ä��� ����
		paddle.setFillColor(Color.black); // �� ������
		paddleX = APPLICATION_WIDTH / 2 - (PADDLE_WIDTH / 2); // ���� ��ǥ ���� �̶�
																// (PADDLE_WIDTH
																// / 2) ���ִ� ������
																// �� ����븦 ���߱�
																// ���ؼ��̴�.
		paddleY = APPLICATION_HEIGHT - PADDLE_Y_OFFSET;
		paddle.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub

				paddleX = arg0.getX() - (PADDLE_WIDTH / 2); // ���콺�� x���̸� x�� �޾Ƽ�
															// ����
				paddleY = APPLICATION_HEIGHT - PADDLE_Y_OFFSET; // y�࿡ ���ؼ��� ������
																// �����Ǿ��ִ�.
				if (paddleX > WIDTH - PADDLE_WIDTH)
					paddleX = WIDTH - PADDLE_WIDTH; // ������ Width���� Ŭ �� ����.
				if (paddleX < 0)
					paddleX = 0; // ������ ���ϰ��� 0�̴�.
				paddle.setLocation(new GPoint(paddleX, paddleY)); // ���� ������ ������
																	// ���� ��ġ ����
			}
		});

		add(paddle);// ������ �߰�
	}

	/**
	 * �� ƨ��°� �Ѱ��ϴ� �Լ�
	 */
	private void ballBounce() {
		double bx = ball.getX();// �� ��ü�� x��ǥ ����
		double by = ball.getY();// �� ��ü�� y��ǥ ����

		if (paddleX - BALL_RADIUS < bx // ������ ������ �ǹ��Ѵ�.
				&& bx < paddleX + PADDLE_WIDTH - BALL_RADIUS
				&& paddleY - BALL_RADIUS * 2 < by
				&& paddleY - BALL_RADIUS * 2 + PADDLE_HEIGHT > by) {
			dy = -dy;
			if (bx < paddleX - BALL_RADIUS + PADDLE_WIDTH / 5) {
				dx = -6;
			}
			if (bx > paddleX - BALL_RADIUS + PADDLE_WIDTH / 5
					&& bx < paddleX - BALL_RADIUS + PADDLE_WIDTH / 5 * 2) {
				dx = -4;
			}
			if (bx > paddleX - BALL_RADIUS + PADDLE_WIDTH / 5 * 3
					&& bx < paddleX - BALL_RADIUS + PADDLE_WIDTH / 5 * 4) {
				dx = 4;
			}
			if (bx > paddleX - BALL_RADIUS + PADDLE_WIDTH / 5 * 4
					&& bx < paddleX - BALL_RADIUS + PADDLE_WIDTH) {
				dx = 6;
			}
		}
		// ������ getElementAt�� �ƴ� �����ϰ� ��ǥ�� ����Ͽ� ������ �� �浹 �Լ�
		// ������ �ּ� ó���ϰ� �ؿ� �ּ��� Ǯ�� �װ� ��� ��� �� �� �ִ�.
		if (by > BRICK_Y_OFFSET - BALL_RADIUS && by < scopeY[9]
				&& isExist(bx + BALL_RADIUS + dx, by + BALL_RADIUS - dy)) {
			BBbyKoo(bx + BALL_RADIUS + dx, by + BALL_RADIUS);
		}

		// BBbygetElementAt(bx, by);
		// �̰� ���� dy �ʱⰪ�� �ٲ� �־���Ѵ�. �ֳĸ� makeFrame��
		// ���� Frame�� �ϳ� ��Ƴ��� ������ �װ� remove�ϴ� �������� �ڿ������� dy = -dy�� �ٲ��.

		if (bx < 0 || bx > WIDTH - BALL_RADIUS * 2) {
			dx = -dx; // ���� �ε�ĥ ��� ������ �ٲ��.
		}
		if (by < 0) { // õ����� ����
			dy = -dy; // ���Ʒ� �ε�ĥ ��� �ٲ��.
		}
		if (by > HEIGHT - BALL_RADIUS) {
			// ���� ���� ������ ������
			turn--; // �� Ƚ�� ����
			remove(ball); // ���� ���ŵ�
			if (turn > 0) { // ���� �����ִٸ� ���� ���� ������ش�.
				makeLabel("You have " + turn
						+ " turns remaining, Click to proceed");// ���� �˷��ش�.

				makeBall();
			}
		}
		//System.out.println("���� �ӷ� : "
				//+ Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
		ball.move(dx, dy); // ���� �����̴� �Լ�
	}

	private void BBbyKoo(double breakPointX, double breakPointY) {

		int col = checkscopeX(breakPointX);// x���� 31�� ��������
		int row = checkscopeY(breakPointY); // y���� 11�� �������� ����� ���Ҵ�.

		if (breakBricks(row, col / 3)) {

			if (dx < 0) { // �ӵ��� ���� ���� ���°� �ٸ��Ƿ� �װ� ����
				// �̰� ���� ó�� ���κ��� ��� ���� col�� 1�� bricks���̿� �浹�� ���޾� �Ͼ�� ���� ������ ������
				// ����ó��
				if (breakPointX > scopeX[27] && !isExist2(row, col / 3 + 1)) {
					dx = -dx;
					dy = -dy;
				} else if (breakPointX > scopeX[col]
						&& !isExist2(row, col / 3 + 1)) {
					dx = -dx;

				} else {

					dy = -dy;
				}
				return;
			} else if (dx > 0) {

				if (breakPointX < scopeX[1] && !isExist2(row, col / 3 - 1)) {

					dx = -dx;
					dy = -dy;
				} else if (breakPointX < scopeX[col + 1]
						&& !isExist2(row, col / 3 - 1)) {

					dx = -dx;

				} else {
					dy = -dy;
				}
				return;
			} else {
		
					breakBricks(row, col / 3 + 1);
					if(isExist2(row+1, col / 3 + 1)) breakBricks(row+1,col / 3 + 1);// ��°� ó��
					if(isExist2(row+1, col / 3 )) breakBricks(row+1,col / 3 );// ��°� ó��

				dy = -dy;

				return;
			}
		}

	}

	/**
	 * ���������� ������ �Լ��� x�� 31�� ���� ������ �� ���ڸ� �˷��ش�.
	 * 
	 * @param bx
	 * @return
	 */
	private int checkscopeX(double bx) {
		int i;
		if (bx < scopeX[0])
			return 0;
		for (i = 0; i < 29; ++i) {
			if (scopeX[i + 1] > bx && bx >= scopeX[i])
				break;
		}
		return i;
	}

	/**
	 * ���������� ������ �Լ��� y�� 11�� ���� ������ �� ���ڸ� �˷��ش�.
	 * 
	 * @param bx
	 * @return
	 */
	private int checkscopeY(double by) {
		int i;
		for (i = 10; i >= 0; --i) {
			if (scopeY[i] <= by) {
				break;
			}

		}
		return i;
	}

	/**
	 * ���������� ������ �Լ��� ���� ������ �����. �� �ѹ� ȣ��ȴ�.
	 * 
	 * @param bx
	 * @return
	 */
	private void makeScope() {
		for (int i = 0; i < 10; i++) {
			scopeX[i * 3] = BRICK_SEP / 2 + BRICK_SEP + BRICK_WIDTH * i
					+ BRICK_SEP * i;
			scopeX[i * 3 + 1] = BRICK_SEP / 2 + BRICK_WIDTH - BRICK_SEP
					+ BRICK_WIDTH * i + BRICK_SEP * i;
			scopeX[i * 3 + 2] = BRICK_SEP / 2 + BRICK_WIDTH + BRICK_SEP / 2
					+ BRICK_WIDTH * i + BRICK_SEP * i;

		}
		scopeX[30] = WIDTH;
		for (int i = 0; i < 11; i++) {
			scopeY[i] = BRICK_Y_OFFSET + (BRICK_SEP + BRICK_HEIGHT) * i;
		}
	}

	/**
	 * �̰��� ������ ���ؼ� �����Ѵٸ� �� ������ ����ϱ� ���� �ۼ��� ���̴�. ���� �ö󰡴µ� ������ �����ϸ� ���ö󰡰� ������
	 * �ɾ��ش�.
	 */
	private boolean isExist(double bx, double by) {
		int countCol = (int) (bx - (BRICK_SEP / 2) + BALL_RADIUS)
				/ (BRICK_SEP + BRICK_WIDTH);
		int countRow = (int) (by - BRICK_Y_OFFSET) / (BRICK_SEP + BRICK_HEIGHT);
		if (countRow < 0)
			countRow = 0;
		if (countCol < 0)
			countCol = 0;
		if (countRow > 9)
			countRow = 9;
		if (countCol > 9)
			countCol = 9;
		if (bricks[countRow][countCol] != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �̰͵� ���� �����ѵ� �̰��� ������ ���� �浹�� �����ϱ� ���� �ۼ��� �Լ��̴�.
	 * 
	 * @param countRow
	 * @param countCol
	 * @return
	 */
	private boolean isExist2(int countRow, int countCol) {
		if (countRow < 0)
			countRow = 0;
		if (countCol < 0)
			countCol = 0;
		if (countRow > 9)
			countRow = 9;
		if (countCol > 9)
			countCol = 9;
		if (bricks[countRow][countCol] != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ������ �����ϴ� �Լ�
	 * 
	 * @param countRow
	 * @param countCol
	 * @return
	 */
	public boolean breakBricks(int countRow, int countCol) {
		if (countRow < 0)
			countRow = 0;
		if (countCol < 0)
			countCol = 0;
		if (countRow > 9)
			countRow = 9;
		if (countCol > 9)
			countCol = 9;
		if (bricks[countRow][countCol] != null) {
			remove(bricks[countRow][countCol]); // ������ â���� ���� ����
			bricks[countRow][countCol] = null; // ������ ���� null�� ����
			return true;
		} else
			return false;
	}

	/**
	 * ���� ����� �Լ� ���� GroundRect �Լ��� ����ؼ� ����
	 */
	public void makeBall() {

		ball = new GRoundRect(APPLICATION_WIDTH / 2 - BALL_RADIUS,
				APPLICATION_HEIGHT / 2, BALL_RADIUS * 2, BALL_RADIUS * 2,
				BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setFilled(true); //
		ball.setFillColor(Color.black);
		dx = 0; // ���� ���鶧 �ӵ� ���� x��ǥ �ӵ�
		dy = 3; // y��ǥ �ӵ�
		add(ball);
	}

	/**
	 * 
	 * �������� ����� �Լ� Windowũ�⵵ ���⼭ ��������.
	 * 
	 */
	public void makeFrame() {
		setSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT)); // windowũ��
																		// ����
		GPoint[] frameP = new GPoint[4];

		frameP[0] = new GPoint(0, 0);
		frameP[1] = new GPoint(APPLICATION_WIDTH, 0);
		frameP[2] = new GPoint(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		frameP[3] = new GPoint(0, APPLICATION_HEIGHT);
		GPolygon frame = new GPolygon(frameP); // �������� ����
		frame.setColor(Color.BLACK); // ������ ������ ������
		add(frame);

	}

	/**
	 * ������ ����� �Լ�
	 */
	public void makeBricks() {
		int x = 0;
		int y = BRICK_Y_OFFSET;
		GRect tmp;
		Color tmpColor;
		for (int row = 0; row < NBRICK_ROWS; row++) {
			x = BRICK_SEP / 2;
			for (int col = 0; col < NBRICKS_PER_ROW; col++) {
				switch (row / 2) { // ���� ���� ����
				case 0:
					tmpColor = Color.red;
					break;
				case 1:
					tmpColor = Color.orange;
					break;
				case 2:
					tmpColor = Color.yellow;
					break;
				case 3:
					tmpColor = Color.green;
					break;
				default:
					tmpColor = Color.blue;
					break;
				}
				tmp = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				x += (BRICK_SEP + BRICK_WIDTH);
				tmp.setFilled(true); // ��ä��� ����
				tmp.setColor(tmpColor); // �׵θ��� ä��
				tmp.setFillColor(tmpColor); // �� ä��
				bricks[row][col] = tmp; // �׸��� �ش� ��ġ�� �ʱ�ȭ
				add(bricks[row][col]); // �ϰ� add���ֱ�
			}
			y += (BRICK_HEIGHT + BRICK_SEP);
		}

	}

	public void changeMap() {
		waitForClick();

		final GLabel ask = new GLabel("Map select(keyboard) 1.default  2.zigzag 3.floar 4.triangle  5.Koo");
		ask.setVisible(true);
		ask.setLocation(new GPoint((WIDTH / 2 - (60) * 3), HEIGHT / 3));
		add(ask);
		this.addKeyListeners(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				
				switch (e.getKeyCode()) {
				case 49:
					makeBricks();
					break;
				case 50:
					makeBricks();
					zigzagMap();
					break;
				case 51:
					makeBricks();
					floarMap();
					break;
				case 52:
					makeBricks();
					triangleMap();
					break;
				case 53:
					makeBricks();
					kooMap();
					break;

				}
				remove(ask);

			}
		});

	}

	public void zigzagMap() {

		for (int row = 0; row < NBRICK_ROWS; row++) {

			for (int col = 0; col < NBRICKS_PER_ROW; col++) {

				if (row % 2 == 1 && col % 2 == 1) {
					remove(bricks[row][col]); // ������ â���� ���� ����
					bricks[row][col] = null;
				}
				if (row % 2 == 0 && col % 2 == 0) {
					remove(bricks[row][col]); // ������ â���� ���� ����
					bricks[row][col] = null;
				}
			}
		}

	}

	public void floarMap() {

		for (int row = 0; row < NBRICK_ROWS; row++) {

			for (int col = 0; col < NBRICKS_PER_ROW; col++) {

				if (row % 2 == 1 && col % 2 == 1) {
					remove(bricks[row][col]); // ������ â���� ���� ����
					bricks[row][col] = null;
				}
				if (row % 2 == 1 && col % 2 == 0) {
					remove(bricks[row][col]); // ������ â���� ���� ����
					bricks[row][col] = null;
				}
			}
		}

	}

	public void triangleMap() {
		int i, j;

		for (i = 0; i < 10; i++) {
			for (j = 0; j < 4 - i; j++) {
				remove(bricks[i][j]); // ������ â���� ���� ����
				bricks[i][j] = null;
			}
		}
		for (i = 0; i < 10; i++) {
			for (j = 9; j > 5 + i; j--) {
				remove(bricks[i][j]); // ������ â���� ���� ����
				bricks[i][j] = null;
			}
		}
	}

	public void kooMap() {

		int i, j;

		for (i = 0; i <= 9; i++) {
			if (i < 5) {
				for (j = 0; j < i; j++) {
					remove(bricks[i][j]); // ������ â���� ���� ����
					bricks[i][j] = null;
				}
				for (j = 9; j >= i + 5; j--) {
					remove(bricks[i][j]); // ������ â���� ���� ����
					bricks[i][j] = null;
				}
			} else {
				for (j = 9; j > i; j--) {
					remove(bricks[i][j]); // ������ â���� ���� ����
					bricks[i][j] = null;
				}
				for (j = 0; j < i - 5; j++) {
					remove(bricks[i][j]); // ������ â���� ���� ����
					bricks[i][j] = null;
				}
			}
		}
	}

}
