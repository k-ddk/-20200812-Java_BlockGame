package BlockGame_ver_2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BlockGame {

	// JFrame을 상속받는다
	static class MyFrame extends JFrame {

		// constant 상수부분 숫자보다는 단어로 설정해 놔서 쉽게 쓸 수 있도록
		// 상수의 이름을 지정할 때는 대문자_대문자 이런 식으로
		static int BALL_WIDTH = 20;
		static int BALL_HEIGHT = 20;
		static int BLOCK_ROWS = 5; // 블록은 5줄로
		static int BLOCK_COLUMNS = 10; // 한 줄에 열 개가 놓이도록
		static int BLOCK_WIDTH = 40;
		static int BLOCK_HEIGHT = 20;
		static int BLOCK_GAP = 3;
		static int BAR_WIDTH = 80;
		static int BAR_HEIGHT = 20;
		static int CANVAS_WIDTH = (BLOCK_WIDTH * BLOCK_COLUMNS) + (BLOCK_GAP * BLOCK_COLUMNS)+10;
		static int CANVAS_HEIGHT = 600;

		// variable
		// 변수만들고 클래스만들고 !
		static MyPanel myPanel = null; // 캔버스 역할
		static int score = 0;
		static Timer timer = null;
		// 공간만 만든거고 실제 객체를 생성한 건 아님
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		static Bar bar = new Bar();
		static Ball ball = new Ball();
		static int barXTarget = bar.x; // Target Value = 보관하기 위해..
		static int dir = 0; // 0:Up-Right, 1:Down-Right, 2:Up-Left, 3:Down-Left
		static int ballSpeed = 5;
		static boolean isGameFinish = false;

		static class Ball {
			int x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2;
			int y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
			int width = BALL_WIDTH;
			int height = BALL_HEIGHT;

			Point getCenter() {
				return new Point(x + (BALL_WIDTH / 2), y + (BALL_HEIGHT / 2));
			}

			Point getBottomCenter() {
				return new Point(x + (BALL_WIDTH / 2), y + (BALL_HEIGHT));
			}

			Point getTopCenter() {
				return new Point(x + (BALL_WIDTH / 2), y);
			}

			Point getLeftCenter() {
				return new Point(x, y + (BALL_HEIGHT / 2));
			}

			Point getRightCenter() {
				return new Point(x + (BALL_WIDTH), y + (BALL_HEIGHT / 2));
			}
		}

		static class Bar {
			int x = CANVAS_WIDTH / 2 - BAR_WIDTH / 2;
			int y = CANVAS_HEIGHT - 100;
			int width = BAR_WIDTH;
			int height = BAR_HEIGHT;
		}

		static class Block {
			int x = 0; // 블록은 여러 개니까 한 개의 값으로 초기화 하는 건 의미가 없음
			int y = 0;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; // 0:white, 1:yellow, 2:blue, 3:mazanta, 4:red
			boolean isHidden = false; // 충돌 이후 블록이 사라지도록
		}

		static class MyPanel extends JPanel { // 판넬 그리기
			// 생성자
			public MyPanel() {
				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				this.setBackground(Color.BLACK);
			}

			@Override // 이미 있는 함수..JPanel에..
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;

				drawUI(g2d);
			}

			private void drawUI(Graphics2D g2d) {
				// draw Blocks
				for (int i = 0; i < BLOCK_ROWS; i++) { // 위에서 공간만 생성해 둔 것에서 객체를 하나씩 만드는 중
					for (int j = 0; j < BLOCK_COLUMNS; j++) {
						if (blocks[i][j].isHidden) {
							continue; // 그리지 않고 루프 순환..
						}
						if (blocks[i][j].color == 0) {
							g2d.setColor(Color.WHITE);
						} else if (blocks[i][j].color == 1) {
							g2d.setColor(Color.YELLOW);
						} else if (blocks[i][j].color == 2) {
							g2d.setColor(Color.BLUE);
						} else if (blocks[i][j].color == 3) {
							g2d.setColor(Color.MAGENTA);
						} else if (blocks[i][j].color == 4) {
							g2d.setColor(Color.RED);
						}
						// 실제로 그리는 부분, fillRect = 네모를 그린다~
						g2d.fillRect(blocks[i][j].x, blocks[i][j].y, blocks[i][j].width, blocks[i][j].height);
					}

					// draw score
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
					g2d.drawString("score : " + score, CANVAS_WIDTH / 2 - 30, 20);
					if (isGameFinish) {
						g2d.setColor(Color.RED);
						g2d.drawString(" Game Finished!", CANVAS_WIDTH / 2 - 55, 50);
					}
					// draw Ball
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_HEIGHT); // 타원을 그리는 함수, 가로 세로가 같으면 원이 됨..

					// draw Bar
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
				}
			}
		}

		// 생성자..
		public MyFrame(String title) {
			super(title);
			this.setVisible(true);
			this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
			this.setLocation(400, 300); // 위치 설정 안 해주면 왼쪽 위에 뜸..
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창이 닫히게 해줌

			initData();

			myPanel = new MyPanel();
			this.add("Center", myPanel);

			setKeyListener();
			startTimer();
		}

		public void initData() {
			for (int i = 0; i < BLOCK_ROWS; i++) { // 위에서 공간만 생성해 둔 것에서 객체를 하나씩 만드는 중
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block();
					blocks[i][j].x = BLOCK_WIDTH * j + BLOCK_GAP * j;
					blocks[i][j].y = 100 + BLOCK_HEIGHT * i + BLOCK_GAP * i;
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4 - i;
					blocks[i][j].isHidden = false;
				}
			}
		}

		public void setKeyListener() {
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) { // 키보드를 치면 Key Event 객체변수를 통해 들어옴
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						System.out.println("Pressed Left Key");
						barXTarget -= 20;
						if (bar.x < barXTarget) { // 계속 키보드를 누른 경우
							barXTarget = bar.x; // 현재 바의 값으로 초기화 해줘서 밀려나가는 현상 방지
						}
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						System.out.println("Pressed Right Key");
						barXTarget += 20;
						if (bar.x > barXTarget) {
							barXTarget = bar.x;
						}
					}
				}
			});
		}

		public void startTimer() {
			timer = new Timer(20, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) { // Timer Event
					movement();
					checkCollision(); // Wall, Bar
					checkCollisionBlock(); // Blocks 50
					myPanel.repaint(); // Redraw!
					
					//블럭 모두 깨기 성공한 것 확인하기..
					isGameFinish();
				}
			});
			timer.restart();
		}
		public void isGameFinish() {
			
			int count=0;
			for(int i=0; i<BLOCK_ROWS; i++) {
				for (int j=0; j<BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if (block.isHidden)
						count++;
				}
			}
			if (count==BLOCK_ROWS*BLOCK_COLUMNS) {
				//게임 완료된 상황!
				isGameFinish = true;
				
			}
		}
		public void movement() {
			if (bar.x < barXTarget) {
				bar.x += 5;
			} else if (bar.x > barXTarget) {
				bar.x -= 5;
			}

			if (dir == 0) {
				ball.x += ballSpeed;
				ball.y -= ballSpeed;
			} else if (dir == 1) {
				ball.x += ballSpeed;
				ball.y += ballSpeed;
			} else if (dir == 2) {
				ball.x -= ballSpeed;
				ball.y -= ballSpeed;
			} else if (dir == 3) {
				ball.x -= ballSpeed;
				ball.y += ballSpeed;
			}
		}

		public boolean duplRect(Rectangle rect1, Rectangle rect2) {
			return rect1.intersects(rect2); // 두 개의 사각형이 겹치는지 여부를 검사해주는 자바에서 지원해주는 함수
		}

		public void checkCollision() {
			if (dir == 0) {
				// Wall
				if (ball.y < 0) { // wall upper
					dir = 1;
				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) { // wall right
					dir = 2;
				}
				// Bar
				// none..
			} else if (dir == 1) {
				// Wall
				if (ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) { // wall bottom
					dir = 0;
					
					//공을 BAR로 잡지 못하면 공의 위치를 리셋, 점수는 0으로 초기화
					dir=0;
					ball.x = CANVAS_WIDTH/2 - BALL_WIDTH/2;
					ball.y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2;
					score=0;
				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) { // wall right
					dir = 3;
				}
				// Bar
				if (ball.getBottomCenter().y >= bar.y) {
					if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
							new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir = 0;
					}
				}
			} else if (dir == 2) {
				// Wall
				if (ball.y < 0) { // wall upper
					dir = 3;
				}
				if (ball.x < 0) { // wall left
					dir = 0;
				}
				// Bar
				// none..
			} else if (dir == 3) {
				// Wall
				if (ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) { // wall bottom
					dir = 2;
					
					//공의 위치를 리셋
					dir=0;
					ball.x = CANVAS_WIDTH/2 - BALL_WIDTH/2;
					ball.y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2;
					score=0;
				}
				if (ball.x < 0) { // wall left
					dir = 1;
				}
				// Bar
				if (ball.getBottomCenter().y >= bar.y) {
					if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
							new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir = 2;
					}
				}

			}
		}

		public void checkCollisionBlock() {
			for (int i = 0; i < BLOCK_ROWS; i++) { // 모든 블럭에 대해 처리해 주어야 하니까
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if (block.isHidden == false) { // 충돌이 안 된 것들.. false.
						if (dir == 0) { // 오른쪽 위
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block 아래쪽에 부딪힌 경우
									dir = 1;
								} else {
									// 블록의 왼쪽에 부딪힌 경우
									dir = 2;
								}
								block.isHidden = true; // 부딪혔으므로 사라짐
								if (block.color==0) {
									score +=10;
								} else if (block.color==1) {
									score+=20;
								} else if (block.color==2) {
									score+=30;
								} else if (block.color==3) {
									score+=40;
								} else if (block.color==4) {
									score+=50;
								}
							}
						} else if (dir == 1) { // 오른쪽 아래
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block top collision
									dir = 0;
								} else {
									// block left collision
									dir = 3;
								}
								block.isHidden = true; // 부딪혔으므로 사라짐
								if (block.color==0) {
									score +=10;
								} else if (block.color==1) {
									score+=20;
								} else if (block.color==2) {
									score+=30;
								} else if (block.color==3) {
									score+=40;
								} else if (block.color==4) {
									score+=50;
								}
							}
						} else if (dir == 2) { // 왼쪽 위
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block bottom collision
									dir = 3;
								} else {
									// block right collision
									dir = 0;
								}
								block.isHidden = true; // 부딪혔으므로 사라짐
								if (block.color==0) {
									score +=10;
								} else if (block.color==1) {
									score+=20;
								} else if (block.color==2) {
									score+=30;
								} else if (block.color==3) {
									score+=40;
								} else if (block.color==4) {
									score+=50;
								}
							}

						} else if (dir == 3) { // 왼쪽 아래
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block top collision
									dir = 2;
								} else {
									// block right collision
									dir = 1;
								}
								block.isHidden = true; // 부딪혔으므로 사라짐
								if (block.color==0) {
									score +=10;
								} else if (block.color==1) {
									score+=20;
								} else if (block.color==2) {
									score+=30;
								} else if (block.color==3) {
									score+=40;
								} else if (block.color==4) {
									score+=50;
								}
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {

		// 이너클래스 생성..
		new MyFrame("Block Game");
	}

}
