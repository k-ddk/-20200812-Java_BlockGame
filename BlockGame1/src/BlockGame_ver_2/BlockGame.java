package BlockGame_ver_2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BlockGame {
 
	//JFrame�� ��ӹ޴´�
	static class MyFrame extends JFrame {
		
		//constant ����κ� ���ں��ٴ� �ܾ�� ������ ���� ���� �� �� �ֵ���
		//����� �̸��� ������ ���� �빮��_�빮�� �̷� ������
		static int BALL_WIDTH=20;
		static int BALL_HEIGHT=20;
		static int BLOCK_ROWS = 5;  //����� 5�ٷ�
		static int BLOCK_COLUMNS = 10;  //�� �ٿ� �� ���� ���̵���
		static int BLOCK_WIDTH = 40;
		static int BLOCK_HEIGHT = 20;
		static int BLOCK_GAP = 3;
		static int BAR_WIDTH = 80;
		static int BAR_HEIGHT = 20;
		static int CANVAS_WIDTH = 400 + (BLOCK_GAP*BLOCK_COLUMNS)-BLOCK_GAP;
		static int CANVAS_HEIGHT = 600;
		
		//variable 
		//��������� Ŭ��������� !
		static MyPanel myPanel = null;  //ĵ���� ����
		static int score = 0;
		static Timer timer = null;
		//������ ����Ű� ���� ��ü�� ������ �� �ƴ�
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		static Bar bar = new Bar();
		static Ball ball = new Ball();
		static int barXTarget = bar.x;  //Target Value = �����ϱ� ����..
		static int dir=0;  //0:Up-Right, 1:Down-Right, 2:Up-Left, 3:Down-Left
		static int ballSpeed = 5;
		
		static class Ball {
			int x = CANVAS_WIDTH/2-BALL_WIDTH/2;
			int y = CANVAS_HEIGHT/2-BALL_HEIGHT/2;
			int width = BALL_WIDTH;
			int height = BALL_HEIGHT;
			
			Point getCenter() {
				return new Point(x+)
			}
		}
		
		static class Bar {
			int x = CANVAS_WIDTH/2-BAR_WIDTH/2;
			int y = CANVAS_HEIGHT-100;
			int width = BAR_WIDTH;
			int height = BAR_HEIGHT;
		}
		
		static class Block {
			int x = 0;  //����� ���� ���ϱ� �� ���� ������ �ʱ�ȭ �ϴ� �� �ǹ̰� ����
			int y = 0;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0;  //0:white, 1:yellow, 2:blue, 3:mazanta, 4:red
			boolean isHidden = false;  //�浹 ���� ����� ���������
		}
		
		static class MyPanel extends JPanel {  //�ǳ� �׸���
			//������
			public MyPanel() {
				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				this.setBackground(Color.BLACK);	
			}
			@Override  //�̹� �ִ� �Լ�..JPanel��..
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D)g;
				
				drawUI(g2d);
			}
			private void drawUI(Graphics2D g2d) {
				//draw Blocks
				for (int i=0; i<BLOCK_ROWS; i++) {  //������ ������ ������ �� �Ϳ��� ��ü�� �ϳ��� ����� ��
					for (int j=0; j<BLOCK_COLUMNS; j++) {
						if (blocks[i][j].isHidden) {
							continue;  //�׸��� �ʰ� ���� ��ȯ..
						}
						if (blocks[i][j].color==0) {
							g2d.setColor(Color.WHITE);
						}
						else if (blocks[i][j].color==1) {
							g2d.setColor(Color.YELLOW);
						}
						else if (blocks[i][j].color==2) {
							g2d.setColor(Color.BLUE);
						}
						else if (blocks[i][j].color==3) {
							g2d.setColor(Color.MAGENTA);
						}
						else if (blocks[i][j].color==4) {
							g2d.setColor(Color.RED);
						}
						//������ �׸��� �κ�, fillRect = �׸� �׸���~
						g2d.fillRect(blocks[i][j].x, blocks[i][j].y, 
								blocks[i][j].width, blocks[i][j].height);
					}
					
					//draw score
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
					g2d.drawString("score : " + score, CANVAS_WIDTH/2-30, 20);
					
					//draw Ball
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_HEIGHT);  //Ÿ���� �׸��� �Լ�, ���� ���ΰ� ������ ���� ��..
				
					//draw Bar
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
				}
			}
		}
		
		
		//������..
		public MyFrame(String title) {
			super(title);
			this.setVisible(true);
			this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
			this.setLocation(400, 300);  //��ġ ���� �� ���ָ� ���� ���� ��..
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //â�� ������ ����
			
			initData();
			
			myPanel = new MyPanel();
			this.add("Center", myPanel);
			
			setKeyListener();
			startTimer();
		}
		public void initData() {
			for (int i=0; i<BLOCK_ROWS; i++) {  //������ ������ ������ �� �Ϳ��� ��ü�� �ϳ��� ����� ��
				for (int j=0; j<BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block();
					blocks[i][j].x = BLOCK_WIDTH*j+BLOCK_GAP*j;
					blocks[i][j].y = 100+BLOCK_HEIGHT*i+BLOCK_GAP*i;
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4-i;
					blocks[i][j].isHidden = false;
				}
			}
		}
		public void setKeyListener() {
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {  //Ű���带 ġ�� Key Event ��ü������ ���� ����
					if(e.getKeyCode()==KeyEvent.VK_LEFT) {
						System.out.println("Pressed Left Key");
						barXTarget -=20;
						if (bar.x<barXTarget) {  //��� Ű���带 ���� ���
							barXTarget = bar.x;  //���� ���� ������ �ʱ�ȭ ���༭ �з������� ���� ����
						}
					}
					else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
						System.out.println("Pressed Right Key");
						barXTarget +=20;
						if (bar.x>barXTarget) {
							barXTarget = bar.x;
						}
					}
				}
			});
		}
		public void startTimer() {
			timer = new Timer(20, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {  //Timer Event
					movement();
					checkCollision();  //Wall, Bar
					checkCollisionBlock();  //Blocks 50
					myPanel.repaint();  //Redraw!
				}
			});
			timer.restart();
		}
		public void movement() {
			if (bar.x<barXTarget) {
				bar.x +=5;
			}else if (bar.x>barXTarget) {
				bar.x -=5;
			}
			
			if (dir==0) {
				ball.x +=ballSpeed;
				ball.y -=ballSpeed;
			}else if (dir==1) {
				ball.x +=ballSpeed;
				ball.y +=ballSpeed;
			}else if (dir==2) {
				ball.x -=ballSpeed;
				ball.y -=ballSpeed;
			}else if (dir==3) {
				ball.x -=ballSpeed;
				ball.y +=ballSpeed;
			}
		}
		public void checkCollision() {
			if (dir==0) {
				//Wall
				if (ball.y<0) {  //wall upper
					dir=1;
				}
				if (ball.x>CANVAS_WIDTH-BALL_WIDTH) {  //wall right
					dir=2;
				}
				//Bar
				//none..
			}else if (dir==1) {
				//Wall
				if (ball.y>CANVAS_HEIGHT-BALL_HEIGHT-BALL_HEIGHT) {  //wall bottom
					dir=0;
				}
				if (ball.x>CANVAS_WIDTH-BALL_WIDTH) {  //wall right
					dir=3;
				}
				//Bar
				if (ball.)
			}else if (dir==2) {
				//Wall
				if (ball.y<0) {  //wall upper
					dir=3;
				}
				if (ball.x<0) {  //wall left
					dir=0;
				}
				//Bar
				//none..
			}else if (dir==3) {
				//Wall
				if (ball.y>CANVAS_HEIGHT-BALL_HEIGHT-BALL_HEIGHT) {  //wall bottom
					dir=2;
				}
				if (ball.x<0) {  //wall left
					dir=1;
				}
				//Bar
			}
		}
		public void checkCollisionBlock() {
			
		}
	}
	
	public static void main(String[] args) {
		
		//�̳�Ŭ���� ����..
		new MyFrame("Block Game");
	}

}
