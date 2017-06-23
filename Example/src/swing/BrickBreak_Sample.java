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
 * Implement the game of BrickBreak. - 최종 구현
 * 
 * 공의 움직임을 구현하시오. - 라켓의 위치별 구현 완료
 * 
 * 벽과의 충돌을 체크하시오. - 벽과 충돌 이미 구현 완료
 * 
 * 벽돌과의 충돌을 체크 - getElementAt() 함수를 이용할 것. 여기서 기존의 구현한 충돌방식이 오히려 낫다고 판단해서
 * BBbyKoo(breakPointX, breakPointY); 를 사용 자세한 내용은 주석을 참조 또한 기존 조건인
 * getElementAt도 구현하였슴 BBbygetElementAt(bx, by); 이 기본 조건으로 구현된 것
 * 
 * 라켓과의 충돌을 체크하고 부딪힌 위치에 따라 공의 속력을 계산할 것. - 콘솔 창에 구현
 * 기타 - changeMap()을 통해 다양한 맵 추가 이거 사용시 위의 makeBricks 주석처리하고 사용할 것
 * @author 12080854 구본철
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
	private double dx; /* x 속도 */
	private double dy; /* y 속도 */
	private double paddleX; /* Paddle의 x 좌표 */
	private double paddleY; /* Paddle의 y 좌표 */
	private GRect[][] bricks = new GRect[NBRICK_ROWS][NBRICKS_PER_ROW];
	private int turn = NTURNS;
	private GLabel gl;
	private GRect paddle;
	private int[] scopeX = new int[31]; // 범위
	private int[] scopeY = new int[11]; // 범위

	public void run() {
		makeScope();
		makeFrame(); // frame 생성
		 makeBricks(); // 벽돌생성
//		changeMap(); // 다양한 맵 추가
		makePaddle(); // 라켓 생성
		makeBall(); // 클릭하면 볼 생성
		makeLabel("You have " + turn + "turns remaining, Click to proceed");
		waitForClick();
		while (true) {
			ballBounce(); // 볼 튕기는 함수
			pause(DELAY); // 0.002초 간격으로 보여줌
			if (turn == 0) {
				makeLabel("FAIL !!! THE END");
				System.exit(1); // 만일 턴이 다 쓰면 종료됨
			}
			 checkBrick();
		}
	}

	/**
	 * 벽돌 다깨지면 종료하기
	 */
	private void checkBrick() {
		int count = 0;
		for (int i = 0; i < NBRICK_ROWS; i++) {
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				if (bricks[j][i] == null)
					count++;
				if (count == 100)
					System.exit(1);// 벽돌이 다깨지면 종료

			}
		}
	}

	/**
	 * getElementAt를 활용하여 벽돌의 충돌을 인지하고 벽돌을 삭제하는 함수
	 * 
	 * 
	 */
	public void BBbygetElementAt(double bx, double by) {

		if (getElementAt(bx + BALL_RADIUS - BRICK_SEP - 1, by - 1) != null
				&& getElementAt(bx, by + BALL_RADIUS) != paddle) {
			remove(getElementAt(bx + BALL_RADIUS - BRICK_SEP, by - 1));
			if (getElementAt(bx + BALL_RADIUS + BRICK_SEP, by - 1) != null)
				remove(getElementAt(bx + BALL_RADIUS + BRICK_SEP, by - 1));
			// 만일 벽돌 2개 가운데 공이 맞았을 경우 삭제는 2개가 되어야한다.

			dy = -dy; // 가운대로 맞기 때문에 y속도만 변한다.
		} else if (getElementAt(bx - 1, by + BALL_RADIUS) != null
				&& getElementAt(bx + BALL_RADIUS, by + BALL_RADIUS * 2) != paddle) {
			remove(getElementAt(bx - 1, by + BALL_RADIUS));
			dx = -dx; // 옆으로 맞았을 경우를 위한 것
		} else if (getElementAt(bx + 2 * BALL_RADIUS + 1, by + BALL_RADIUS) != null
				&& getElementAt(bx + BALL_RADIUS, by + BALL_RADIUS) != paddle) {
			remove(getElementAt(bx + 2 * BALL_RADIUS + 1, by + BALL_RADIUS));
			dx = -dx; // 옆으로 맞았을 경우를 위한 것
		}

	}

	/**
	 * 이 함수는 충돌시 꺽이는 정도를 확인하기 위한 줄 긋기 함수로 테스트 용도에서 사용됨
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
	 * 공이 몇개 남았는지 알려주고 자동으로 넘어간다.
	 * 
	 * @param str
	 */
	public void makeLabel(String str) {
		gl = new GLabel(str);
		gl.setColor(Color.black);
		gl.setVisible(true);
		gl.setLocation(new GPoint((WIDTH / 2 - (str.length()) * 3), HEIGHT / 2));

		remove(paddle);
		makePaddle(); // 라켓을 초기화 해준다.
		add(gl);
		try {
			Thread.sleep(2000);// 2초 있다가 자동으로 시작

		} catch (Exception e) {
			// TODO: handle exception
		}
		remove(gl);
	}

	/**
	 * 라켓을 만드는 함수 addMouseMotionListener()를 통해 마우스의 모션에 따라서 라켓의 위치가 조정된다.
	 * 
	 */
	public void makePaddle() {
		paddle = new GRect(APPLICATION_WIDTH / 2 - (PADDLE_WIDTH / 2),
				APPLICATION_HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH,
				PADDLE_HEIGHT); // 라켓의 초기 위치와 크기에 대해 생성하면서 할당
		paddle.setFilled(true); // 색채우기 가능
		paddle.setFillColor(Color.black); // 색 검은색
		paddleX = APPLICATION_WIDTH / 2 - (PADDLE_WIDTH / 2); // 라켓 좌표 설정 이때
																// (PADDLE_WIDTH
																// / 2) 해주는 이유는
																// 정 가운대를 맞추기
																// 위해서이다.
		paddleY = APPLICATION_HEIGHT - PADDLE_Y_OFFSET;
		paddle.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub

				paddleX = arg0.getX() - (PADDLE_WIDTH / 2); // 마우스가 x축이면 x를 받아서
															// 설정
				paddleY = APPLICATION_HEIGHT - PADDLE_Y_OFFSET; // y축에 대해서는 라켓이
																// 고정되어있다.
				if (paddleX > WIDTH - PADDLE_WIDTH)
					paddleX = WIDTH - PADDLE_WIDTH; // 라켓은 Width보다 클 수 없다.
				if (paddleX < 0)
					paddleX = 0; // 라켓의 최하값은 0이다.
				paddle.setLocation(new GPoint(paddleX, paddleY)); // 위의 정해진 값으로
																	// 라켓 위치 설정
			}
		});

		add(paddle);// 라켓을 추가
	}

	/**
	 * 볼 튕기는걸 총괄하는 함수
	 */
	private void ballBounce() {
		double bx = ball.getX();// 볼 객체의 x좌표 저장
		double by = ball.getY();// 볼 객체의 y좌표 저장

		if (paddleX - BALL_RADIUS < bx // 라켓의 조건을 의미한다.
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
		// 이쪽은 getElementAt이 아닌 순수하게 좌표로 계산하여 구현한 공 충돌 함수
		// 이쪽을 주석 처리하고 밑에 주석을 풀면 그것 대로 사용 할 수 있다.
		if (by > BRICK_Y_OFFSET - BALL_RADIUS && by < scopeY[9]
				&& isExist(bx + BALL_RADIUS + dx, by + BALL_RADIUS - dy)) {
			BBbyKoo(bx + BALL_RADIUS + dx, by + BALL_RADIUS);
		}

		// BBbygetElementAt(bx, by);
		// 이거 사용시 dy 초기값을 바꿔 주어야한다. 왜냐면 makeFrame을
		// 통해 Frame을 하나 깔아놓기 때문에 그걸 remove하는 과정에서 자연스럽게 dy = -dy로 바뀐다.

		if (bx < 0 || bx > WIDTH - BALL_RADIUS * 2) {
			dx = -dx; // 옆에 부딪칠 경우 방향이 바뀐다.
		}
		if (by < 0) { // 천장까지 제한
			dy = -dy; // 위아로 부딛칠 경우 바뀐다.
		}
		if (by > HEIGHT - BALL_RADIUS) {
			// 만일 공이 떨어저 나가면
			turn--; // 턴 횟수 감소
			remove(ball); // 공은 제거됨
			if (turn > 0) { // 턴이 남아있다면 새로 공을 만들어준다.
				makeLabel("You have " + turn
						+ " turns remaining, Click to proceed");// 턴을 알려준다.

				makeBall();
			}
		}
		System.out.println("공의 속력 : "
				+ Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
		ball.move(dx, dy); // 공은 움직이는 함수
	}

	private void BBbyKoo(double breakPointX, double breakPointY) {

		int col = checkscopeX(breakPointX);// x축을 31개 구역으로
		int row = checkscopeY(breakPointY); // y축을 11개 구역으로 나누어서 보았다.

		if (breakBricks(row, col / 3)) {

			if (dx < 0) { // 속도에 따른 벽돌 깨는게 다르므로 그걸 구현
				// 이건 예외 처리 끝부분일 경우 벽과 col이 1인 bricks사이에 충돌이 연달아 일어나서 보기 안좋기 때문에
				// 예외처리
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
					if(isExist2(row+1, col / 3 + 1)) breakBricks(row+1,col / 3 + 1);// 어설픈거 처리
					if(isExist2(row+1, col / 3 )) breakBricks(row+1,col / 3 );// 어설픈거 처리

				dy = -dy;

				return;
			}
		}

	}

	/**
	 * 내부적으로 구현한 함수로 x의 31개 범위 구역중 그 숫자를 알려준다.
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
	 * 내부적으로 구현한 함수로 y의 11개 범위 구역중 그 숫자를 알려준다.
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
	 * 내부적으로 구현한 함수로 현재 범위를 만든다. 딱 한번 호출된다.
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
	 * 이것은 간단히 말해서 존재한다면 그 공간을 통과하기 위해 작성한 것이다. 만일 올라가는데 벽돌이 존재하면 못올라가게 조건을
	 * 걸어준다.
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
	 * 이것도 위에 동등한데 이것은 벽돌이 옆에 충돌시 제어하기 위해 작성한 함수이다.
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
	 * 벽돌을 제거하는 함수
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
			remove(bricks[countRow][countCol]); // 윈도우 창에서 벽돌 제거
			bricks[countRow][countCol] = null; // 벽돌의 값을 null로 설정
			return true;
		} else
			return false;
	}

	/**
	 * 공을 만드는 함수 공은 GroundRect 함수를 사용해서 만듬
	 */
	public void makeBall() {

		ball = new GRoundRect(APPLICATION_WIDTH / 2 - BALL_RADIUS,
				APPLICATION_HEIGHT / 2, BALL_RADIUS * 2, BALL_RADIUS * 2,
				BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setFilled(true); //
		ball.setFillColor(Color.black);
		dx = 0; // 공을 만들때 속도 설정 x좌표 속도
		dy = 3; // y좌표 속도
		add(ball);
	}

	/**
	 * 
	 * 프레임을 만드는 함수 Window크기도 여기서 정해진다.
	 * 
	 */
	public void makeFrame() {
		setSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT)); // window크기
																		// 설정
		GPoint[] frameP = new GPoint[4];

		frameP[0] = new GPoint(0, 0);
		frameP[1] = new GPoint(APPLICATION_WIDTH, 0);
		frameP[2] = new GPoint(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		frameP[3] = new GPoint(0, APPLICATION_HEIGHT);
		GPolygon frame = new GPolygon(frameP); // 프레임을 생성
		frame.setColor(Color.BLACK); // 프레임 색상은 검은색
		add(frame);

	}

	/**
	 * 벽돌을 만드는 함수
	 */
	public void makeBricks() {
		int x = 0;
		int y = BRICK_Y_OFFSET;
		GRect tmp;
		Color tmpColor;
		for (int row = 0; row < NBRICK_ROWS; row++) {
			x = BRICK_SEP / 2;
			for (int col = 0; col < NBRICKS_PER_ROW; col++) {
				switch (row / 2) { // 벽돌 색깔 설정
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
				tmp.setFilled(true); // 색채우기 가능
				tmp.setColor(tmpColor); // 테두리색 채움
				tmp.setFillColor(tmpColor); // 색 채움
				bricks[row][col] = tmp; // 그리고 해당 위치로 초기화
				add(bricks[row][col]); // 하고 add해주기
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
					remove(bricks[row][col]); // 윈도우 창에서 벽돌 제거
					bricks[row][col] = null;
				}
				if (row % 2 == 0 && col % 2 == 0) {
					remove(bricks[row][col]); // 윈도우 창에서 벽돌 제거
					bricks[row][col] = null;
				}
			}
		}

	}

	public void floarMap() {

		for (int row = 0; row < NBRICK_ROWS; row++) {

			for (int col = 0; col < NBRICKS_PER_ROW; col++) {

				if (row % 2 == 1 && col % 2 == 1) {
					remove(bricks[row][col]); // 윈도우 창에서 벽돌 제거
					bricks[row][col] = null;
				}
				if (row % 2 == 1 && col % 2 == 0) {
					remove(bricks[row][col]); // 윈도우 창에서 벽돌 제거
					bricks[row][col] = null;
				}
			}
		}

	}

	public void triangleMap() {
		int i, j;

		for (i = 0; i < 10; i++) {
			for (j = 0; j < 4 - i; j++) {
				remove(bricks[i][j]); // 윈도우 창에서 벽돌 제거
				bricks[i][j] = null;
			}
		}
		for (i = 0; i < 10; i++) {
			for (j = 9; j > 5 + i; j--) {
				remove(bricks[i][j]); // 윈도우 창에서 벽돌 제거
				bricks[i][j] = null;
			}
		}
	}

	public void kooMap() {

		int i, j;

		for (i = 0; i <= 9; i++) {
			if (i < 5) {
				for (j = 0; j < i; j++) {
					remove(bricks[i][j]); // 윈도우 창에서 벽돌 제거
					bricks[i][j] = null;
				}
				for (j = 9; j >= i + 5; j--) {
					remove(bricks[i][j]); // 윈도우 창에서 벽돌 제거
					bricks[i][j] = null;
				}
			} else {
				for (j = 9; j > i; j--) {
					remove(bricks[i][j]); // 윈도우 창에서 벽돌 제거
					bricks[i][j] = null;
				}
				for (j = 0; j < i - 5; j++) {
					remove(bricks[i][j]); // 윈도우 창에서 벽돌 제거
					bricks[i][j] = null;
				}
			}
		}
	}

}
