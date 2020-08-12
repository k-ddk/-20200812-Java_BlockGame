package BlockGame_ver_1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BlockGame {

	static class MyFrame extends JFrame {

		// constant 상수들... 은 다 대문자로!
		// 그때 그때 숫자로 쓰는 것 보다는 이름으로 하는게 나아서 !
		static int BALL_WIDTH = 20; // 공의 크기 지정
		static int BALL_HEIGHT = 20;

		static int BLOCK_ROWS = 5; // 블록의 줄 수
		static int BLOCK_COLUMNS = 10; // 블록은 가로 세로 10개가 놓이도록..
		static int BLOCK_WIDTH = 40; // 블록 한개의 너비
		static int BLOCK_HEIGHT = 20; // 블록 한개의 높이
		static int BLOCK_GAP = 3; // 블록들 간의 간격

		static int BAR_WIDTH = 80; // 사용자가 움직이는 바의 크기
		static int BAR_HEIGHT = 20;

		// 400 = (블록 한개 너비40)*(블록 칼럼10) 인데 여기 + 블록들 간의 갭! 이때 마지막 갭은 필요없으므로 빼주기
		static int CANVAS_WIDTH = 400 + (BLOCK_GAP * BLOCK_COLUMNS) - BLOCK_GAP; // 캔버스 크기
		static int CANVAS_HEIGHT = 600;

		// variable
		static MyPanel myPanel = null; // 캔버스를 그릴 판
		static int score = 0; // 점수를 표현할 변수
		static Timer timer = null;
		// 블록 이라는 클래스의 2차배열.. 블럭의 공간만 만들어진 상태 객체가 만들어진건 아님
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		static Bar bar = new Bar(); // Bar라는 클래스의 객체
		static Ball ball = new Ball();
		static int barXTarget = bar.x; // Target Value
		static int dir = 0; // 공이 움직이는 방향.. 0: Up-Right, 1: Down-Right, 2: Up-Left, 3: Down-Left
		static int ballSpeed = 5; // 공의 속도-> 나중에 난이도 높일 때 이거가 점점 빠르게??
		static boolean isGameFinished = false;
		
		static class Ball {
			int x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2; // 공의 위치! 첫 화면에서는 가운데에 위치하도록!
			int y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
			int width = BALL_WIDTH;
			int height = BALL_HEIGHT;

			Point getCenter() {
				return new Point(x + (BALL_WIDTH / 2), y + (BALL_HEIGHT / 2));
			}

			Point getBottomCenter() {
				return new Point(x + (BALL_WIDTH / 2), y + (BALL_HEIGHT / 2));
			}

			Point getTopCenter() {
				return new Point(x + (BALL_WIDTH / 2), y);
			}

			Point getLeftCenter() {
				return new Point(x, y + (BALL_HEIGHT / 2));
			}

			Point getRightCenter() {
				return new Point(x + (BALL_WIDTH / 2), y + (BALL_HEIGHT / 2));
			}

		}

		static class Bar {
			int x = CANVAS_WIDTH / 2 - BAR_WIDTH / 2;
			int y = CANVAS_HEIGHT - 100;
			int width = BAR_WIDTH;
			int height = BAR_HEIGHT;
		}

		static class Block {
			int x; // 블럭은 여러개니까 위치가 다 다르기 떄문에 초기화 하지 않는다~
			int y;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; // 0:흰색, 1:노랑색, 2:파란색, 3:mazenta, 4:빨간색<- 공 색에 따라 점수가 다름
			boolean isHidden = false; // 충돌 이후 블록이 사라지도록
		}

		// ui를 드로잉 해주는 캔버스 역할을 ~
		static class MyPanel extends JPanel { // JPanel을 상속받는 클래스 MyPanel,
			// 생성자
			public MyPanel() {
				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				this.setBackground(Color.BLACK);
			}

			@Override // 이미 JPanel 안에 있는 것의 오버라이드~
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;

				drawUI(g2d);
			}

			// 밖에서 굳이 안보여도 되니까 private으로
			private void drawUI(Graphics2D g2d) {
				// draw Blocks
				for (int i = 0; i < BLOCK_ROWS; i++) {
					for (int j = 0; j < BLOCK_COLUMNS; j++) {
						if (blocks[i][j].isHidden) {
							continue; // 히든이라면 나타날 필요가 없으므로
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
						g2d.fillRect(blocks[i][j].x, blocks[i][j].y, blocks[i][j].width, blocks[i][j].height);

					}

					// 위에서는 블럭을 그렸으니 이제 점수판 그리기
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
					if (isGameFinished)
						g2d.drawString("score : " + score, CANVAS_WIDTH / 2 - 30, 20);

					// 공 그리기
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_HEIGHT);

					// 바 그리기
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
				}

			}
		}

		// 생성자
		public MyFrame(String title) {
			super(title);
			this.setVisible(true); // 화면이 나타나도록 true값!
			this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
			this.setLocation(400, 100); // 창이 뜨는 위치!
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 이 옵션을 넣어야 창이 닫힘

			initData();

			myPanel = new MyPanel(); // 패널을 만들고 이거를 전체 프레임에 넣어줘야함..
			this.add("Center", myPanel);

			setKeyListener(); // 키보드로 바를 움직여야 하므로
			startTimer();
		}

		public void initData() { // 상수들을 실제로 초기화 해주는 함수
			// 열칸 짜리의 블럭들은 전부 지정을 해줘야함
			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block(); // 만들어진 공간에 Block 이라는 클래스를 ~
					blocks[i][j].x = BLOCK_WIDTH * j + BLOCK_GAP * j;
					blocks[i][j].y = 100 + BLOCK_HEIGHT * i + BLOCK_GAP * i;
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4 - i; // 색깔 주는 중
					blocks[i][j].isHidden = false; // 충돌하고 나서야 블럭이 사라질 수 있도록
				}
			}
		}

		public void setKeyListener() {
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) { // 키가 눌렸을 때
					if (e.getKeyCode() == KeyEvent.VK_LEFT) { // 왼쪽 키가 눌렸을 때
						System.out.println("Pressed Left Key");
						barXTarget -= 20; // 왼쪽으로 이만큼 이동
						if (bar.x < barXTarget) { // 바의 x값이 이미 더 작은데 키를 반복해서 누르는 경우..
							barXTarget = bar.x; // 밀려나가는 현상 방지
						}
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // 오른쪽 키가 눌렸을 때
						System.out.println("Pressed Right Key");
						barXTarget += 20; // 오른쪽으로 이만큼 이동
						if (bar.x > barXTarget) {
							barXTarget = bar.x; // 밀려나가는 현상 방지
						}
					}
				}
			});

		}

		public void startTimer() {
			timer = new Timer(20, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) { // 이미 정의된 함수..
					movement(); // 움직이는 것을 구현하기 위해 만든 함수
					checkCollision(); // 벽과의 충돌처리를 위해 만든 함수
					checkCollisionBlock(); // 50개의 벽돌에 충돌...
					myPanel.repaint(); // 끝나고 나서 ui를 새로 업데이트 해줘야 하니까 redraw 해주는 함수
				
					//Game success!
					isGameFinished();
					
				}
			});
			timer.restart();
		}

		public void isGameFinished() {
			
			int count=0;
			for (int i=0; i<BLOCK_ROWS; i++) {
				for (int j=0; j<BLOCK_COLUMNS; j++) {
					Block block= blocks[i][j];
					if(block.isHidden)
						count++;
				}
			}
			if (count==BLOCK_ROWS*BLOCK_COLUMNS) {
				
				timer.stop();
				
			}
		}
		public void movement() { // 실제로 움직이는 것 구현하는 함수
			// 바의 움직임 구현
			if (bar.x < barXTarget) {
				bar.x += 5; // 부드럽게 움직이는 정도
			} else if (bar.x > barXTarget) {
				bar.x -= 5;
			}

			// 공의 움직임 구현
			if (dir == 0) { // 오른쪽 위
				ball.x += ballSpeed;
				ball.y -= ballSpeed; // 위로 올라갈 때 y값은 -
			} else if (dir == 1) { // 오른쪽 아래
				ball.x += ballSpeed;
				ball.y += ballSpeed;
			} else if (dir == 2) { // 왼쪽 위
				ball.x -= ballSpeed;
				ball.y -= ballSpeed;
			} else if (dir == 3) { // 왼쪽 아래
				ball.x -= ballSpeed;
				ball.y += ballSpeed;
			}
		}

		public boolean dupleRect(Rectangle rect1, Rectangle rect2) { // 충돌여부 체크해주는 함수
			return rect1.intersects(rect2); // 겹침여부를 검사해주는 이미 존재하는 함수 (중복되는지=duplicated 인지)
		}

		public void checkCollision() {
			if (dir == 0) {
				// 벽에 충돌
				if (ball.y < 0) { // 윗벽에 부딪힌 경우
					dir = 1;
				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) { // 오른쪽 벽에 부딪힌 경우
					dir = 2;
				}

				// 바에 충돌->하는 경우는 이때는 없음

			} else if (dir == 1) { // 오른쪽 아래
				// 벽
				if (ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) { // 아래쪽 벽에 부딪혔을 때
					dir = 0;
					
					//공이 아래로 떨어져 버린 경우 게임을 리셋해주기
					dir = 0;
					ball.x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2; // 공의 위치! 첫 화면에서는 가운데에 위치하도록!
					ball.y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
					score=0;
				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) {
					dir = 3;
				}

				// **공이 그려질 때는 공모양이지만 실제로는 사각형
				// 바
				if (ball.getBottomCenter().y >= bar.y) { // 공 사각형과, 바 사각형이 겹치는지 비교~
					if (dupleRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
							new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir = 0;
					}
				}
			} else if (dir == 2) { // 왼쪽 위
				// 벽
				if (ball.y < 0) { // 위쪽 벽에 부딪혔을 때
					dir = 3;
				}
				if (ball.x < 0) {
					dir = 0;
				}

				// 바-> 올라가는 중이기 때문에 바에 충돌할일 없음.
			} else if (dir == 3) { // 왼쪽 아래
				// 벽
				if (ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) { // 아래쪽 벽에 부딪혔을 때
					dir = 2;
					
					//공이 아래로 떨어져 버린 경우 게임을 리셋해주기
					dir = 0;
					ball.x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2; // 공의 위치! 첫 화면에서는 가운데에 위치하도록!
					ball.y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
					score=0;
				}
				if (ball.x < 0) {
					dir = 1;
				}

				// 바
				if (ball.getBottomCenter().y >= bar.y) { // 공 사각형과, 바 사각형이 겹치는지 비교~ (원고 사각형의 겹침 확인하는 것은 또 다른 알고리즘을 써야함)
					if (dupleRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
							new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir = 2;
					}
				}
			}
		}

		public void checkCollisionBlock() {
			// 0: Up-Right, 1: Down-Right, 2: Up-Left, 3: Down-Left
			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j]; // 블럭 하나를 우선 가져옴
					if (block.isHidden == false) {
						if (dir == 0) {
							// 겹치는지 여부를 확인
							if (dupleRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
								if (ball.x > block.x + 2 && 
								    ball.getRightCenter().x <= block.x + block.width - 2) {
									// block bottom collision
									dir = 1;
								} else {
									// block left collision
									dir = 2;
								}
								block.isHidden = true; // 부딪힌 블럭은 감춰버림
								if (block.color==0)
									score+=10;
								else if (block.color==1)
									score+=20;
								else if (block.color==2)
									score+=30;
								else if (block.color==3)
									score+=40;
								else if (block.color==4)
									score+=50;
							}
						} else if (dir == 1) {
							if (dupleRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
								if (ball.x > block.x + 2 && 
									ball.getRightCenter().x <= block.x + block.width - 2) {
									// block top collision
									dir = 0;
								} else {
									// block left collision
									dir = 3;
								}
								block.isHidden = true; // 부딪힌 블럭은 감춰버림
								if (block.color==0)
									score+=10;
								else if (block.color==1)
									score+=20;
								else if (block.color==2)
									score+=30;
								else if (block.color==3)
									score+=40;
								else if (block.color==4)
									score+=50;
							}

						} else if (dir == 2) {
							if (dupleRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block bottom collision
									dir = 3;
								} else {
									// block right collision
									dir = 0;
								}
								block.isHidden = true; // 부딪힌 블럭은 감춰버림
								if (block.color==0)
									score+=10;
								else if (block.color==1)
									score+=20;
								else if (block.color==2)
									score+=30;
								else if (block.color==3)
									score+=40;
								else if (block.color==4)
									score+=50;
							}

						} else if (dir == 3) {
							if (dupleRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// block top collision
									dir = 2;
								} else {
									// block left collision
									dir = 1;
								}
								block.isHidden = true; // 부딪힌 블럭은 감춰버림
								if (block.color==0)
									score+=10;
								else if (block.color==1)
									score+=20;
								else if (block.color==2)
									score+=30;
								else if (block.color==3)
									score+=40;
								else if (block.color==4)
									score+=50;
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {

		new MyFrame("Block Game");

	}

}
