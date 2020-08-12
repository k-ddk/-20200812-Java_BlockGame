package BlockGame_ver_1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BlockGame {

	static class MyFrame extends JFrame {

		// constant �����... �� �� �빮�ڷ�!
		// �׶� �׶� ���ڷ� ���� �� ���ٴ� �̸����� �ϴ°� ���Ƽ� !
		static int BALL_WIDTH = 20; // ���� ũ�� ����
		static int BALL_HEIGHT = 20;

		static int BLOCK_ROWS = 5; // ����� �� ��
		static int BLOCK_COLUMNS = 10; // ����� ���� ���� 10���� ���̵���..
		static int BLOCK_WIDTH = 40; // ��� �Ѱ��� �ʺ�
		static int BLOCK_HEIGHT = 20; // ��� �Ѱ��� ����
		static int BLOCK_GAP = 3; // ��ϵ� ���� ����

		static int BAR_WIDTH = 80; // ����ڰ� �����̴� ���� ũ��
		static int BAR_HEIGHT = 20;

		// 400 = (��� �Ѱ� �ʺ�40)*(��� Į��10) �ε� ���� + ��ϵ� ���� ��! �̶� ������ ���� �ʿ�����Ƿ� ���ֱ�
		static int CANVAS_WIDTH = 400 + (BLOCK_GAP * BLOCK_COLUMNS) - BLOCK_GAP; // ĵ���� ũ��
		static int CANVAS_HEIGHT = 600;

		// variable
		static MyPanel myPanel = null; // ĵ������ �׸� ��
		static int score = 0; // ������ ǥ���� ����
		static Timer timer = null;
		// ��� �̶�� Ŭ������ 2���迭.. ���� ������ ������� ���� ��ü�� ��������� �ƴ�
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		static Bar bar = new Bar(); // Bar��� Ŭ������ ��ü
		static Ball ball = new Ball();
		static int barXTarget = bar.x; // Target Value
		static int dir = 0; // ���� �����̴� ����.. 0: Up-Right, 1: Down-Right, 2: Up-Left, 3: Down-Left
		static int ballSpeed = 5; // ���� �ӵ�-> ���߿� ���̵� ���� �� �̰Ű� ���� ������??
		static boolean isGameFinished = false;
		
		static class Ball {
			int x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2; // ���� ��ġ! ù ȭ�鿡���� ����� ��ġ�ϵ���!
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
			int x; // ���� �������ϱ� ��ġ�� �� �ٸ��� ������ �ʱ�ȭ ���� �ʴ´�~
			int y;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; // 0:���, 1:�����, 2:�Ķ���, 3:mazenta, 4:������<- �� ���� ���� ������ �ٸ�
			boolean isHidden = false; // �浹 ���� ����� ���������
		}

		// ui�� ����� ���ִ� ĵ���� ������ ~
		static class MyPanel extends JPanel { // JPanel�� ��ӹ޴� Ŭ���� MyPanel,
			// ������
			public MyPanel() {
				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				this.setBackground(Color.BLACK);
			}

			@Override // �̹� JPanel �ȿ� �ִ� ���� �������̵�~
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;

				drawUI(g2d);
			}

			// �ۿ��� ���� �Ⱥ����� �Ǵϱ� private����
			private void drawUI(Graphics2D g2d) {
				// draw Blocks
				for (int i = 0; i < BLOCK_ROWS; i++) {
					for (int j = 0; j < BLOCK_COLUMNS; j++) {
						if (blocks[i][j].isHidden) {
							continue; // �����̶�� ��Ÿ�� �ʿ䰡 �����Ƿ�
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

					// �������� ���� �׷����� ���� ������ �׸���
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
					if (isGameFinished)
						g2d.drawString("score : " + score, CANVAS_WIDTH / 2 - 30, 20);

					// �� �׸���
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_HEIGHT);

					// �� �׸���
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
				}

			}
		}

		// ������
		public MyFrame(String title) {
			super(title);
			this.setVisible(true); // ȭ���� ��Ÿ������ true��!
			this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
			this.setLocation(400, 100); // â�� �ߴ� ��ġ!
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �� �ɼ��� �־�� â�� ����

			initData();

			myPanel = new MyPanel(); // �г��� ����� �̰Ÿ� ��ü �����ӿ� �־������..
			this.add("Center", myPanel);

			setKeyListener(); // Ű����� �ٸ� �������� �ϹǷ�
			startTimer();
		}

		public void initData() { // ������� ������ �ʱ�ȭ ���ִ� �Լ�
			// ��ĭ ¥���� ������ ���� ������ �������
			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block(); // ������� ������ Block �̶�� Ŭ������ ~
					blocks[i][j].x = BLOCK_WIDTH * j + BLOCK_GAP * j;
					blocks[i][j].y = 100 + BLOCK_HEIGHT * i + BLOCK_GAP * i;
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4 - i; // ���� �ִ� ��
					blocks[i][j].isHidden = false; // �浹�ϰ� ������ ���� ����� �� �ֵ���
				}
			}
		}

		public void setKeyListener() {
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) { // Ű�� ������ ��
					if (e.getKeyCode() == KeyEvent.VK_LEFT) { // ���� Ű�� ������ ��
						System.out.println("Pressed Left Key");
						barXTarget -= 20; // �������� �̸�ŭ �̵�
						if (bar.x < barXTarget) { // ���� x���� �̹� �� ������ Ű�� �ݺ��ؼ� ������ ���..
							barXTarget = bar.x; // �з������� ���� ����
						}
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // ������ Ű�� ������ ��
						System.out.println("Pressed Right Key");
						barXTarget += 20; // ���������� �̸�ŭ �̵�
						if (bar.x > barXTarget) {
							barXTarget = bar.x; // �з������� ���� ����
						}
					}
				}
			});

		}

		public void startTimer() {
			timer = new Timer(20, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) { // �̹� ���ǵ� �Լ�..
					movement(); // �����̴� ���� �����ϱ� ���� ���� �Լ�
					checkCollision(); // ������ �浹ó���� ���� ���� �Լ�
					checkCollisionBlock(); // 50���� ������ �浹...
					myPanel.repaint(); // ������ ���� ui�� ���� ������Ʈ ����� �ϴϱ� redraw ���ִ� �Լ�
				
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
		public void movement() { // ������ �����̴� �� �����ϴ� �Լ�
			// ���� ������ ����
			if (bar.x < barXTarget) {
				bar.x += 5; // �ε巴�� �����̴� ����
			} else if (bar.x > barXTarget) {
				bar.x -= 5;
			}

			// ���� ������ ����
			if (dir == 0) { // ������ ��
				ball.x += ballSpeed;
				ball.y -= ballSpeed; // ���� �ö� �� y���� -
			} else if (dir == 1) { // ������ �Ʒ�
				ball.x += ballSpeed;
				ball.y += ballSpeed;
			} else if (dir == 2) { // ���� ��
				ball.x -= ballSpeed;
				ball.y -= ballSpeed;
			} else if (dir == 3) { // ���� �Ʒ�
				ball.x -= ballSpeed;
				ball.y += ballSpeed;
			}
		}

		public boolean dupleRect(Rectangle rect1, Rectangle rect2) { // �浹���� üũ���ִ� �Լ�
			return rect1.intersects(rect2); // ��ħ���θ� �˻����ִ� �̹� �����ϴ� �Լ� (�ߺ��Ǵ���=duplicated ����)
		}

		public void checkCollision() {
			if (dir == 0) {
				// ���� �浹
				if (ball.y < 0) { // ������ �ε��� ���
					dir = 1;
				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) { // ������ ���� �ε��� ���
					dir = 2;
				}

				// �ٿ� �浹->�ϴ� ���� �̶��� ����

			} else if (dir == 1) { // ������ �Ʒ�
				// ��
				if (ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) { // �Ʒ��� ���� �ε����� ��
					dir = 0;
					
					//���� �Ʒ��� ������ ���� ��� ������ �������ֱ�
					dir = 0;
					ball.x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2; // ���� ��ġ! ù ȭ�鿡���� ����� ��ġ�ϵ���!
					ball.y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
					score=0;
				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) {
					dir = 3;
				}

				// **���� �׷��� ���� ����������� �����δ� �簢��
				// ��
				if (ball.getBottomCenter().y >= bar.y) { // �� �簢����, �� �簢���� ��ġ���� ��~
					if (dupleRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
							new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir = 0;
					}
				}
			} else if (dir == 2) { // ���� ��
				// ��
				if (ball.y < 0) { // ���� ���� �ε����� ��
					dir = 3;
				}
				if (ball.x < 0) {
					dir = 0;
				}

				// ��-> �ö󰡴� ���̱� ������ �ٿ� �浹���� ����.
			} else if (dir == 3) { // ���� �Ʒ�
				// ��
				if (ball.y > CANVAS_HEIGHT - BALL_HEIGHT - BALL_HEIGHT) { // �Ʒ��� ���� �ε����� ��
					dir = 2;
					
					//���� �Ʒ��� ������ ���� ��� ������ �������ֱ�
					dir = 0;
					ball.x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2; // ���� ��ġ! ù ȭ�鿡���� ����� ��ġ�ϵ���!
					ball.y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
					score=0;
				}
				if (ball.x < 0) {
					dir = 1;
				}

				// ��
				if (ball.getBottomCenter().y >= bar.y) { // �� �簢����, �� �簢���� ��ġ���� ��~ (���� �簢���� ��ħ Ȯ���ϴ� ���� �� �ٸ� �˰����� �����)
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
					Block block = blocks[i][j]; // �� �ϳ��� �켱 ������
					if (block.isHidden == false) {
						if (dir == 0) {
							// ��ġ���� ���θ� Ȯ��
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
								block.isHidden = true; // �ε��� ���� �������
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
								block.isHidden = true; // �ε��� ���� �������
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
								block.isHidden = true; // �ε��� ���� �������
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
								block.isHidden = true; // �ε��� ���� �������
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
