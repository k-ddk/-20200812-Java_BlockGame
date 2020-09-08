package BlockGame_my_ver2;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame; //ctrl + shift+ o �ϸ� �ʿ��� ��Ű��? �� �ڵ����� ����

public class BlockGame extends JFrame {  //Ŭ���� �̸��� �빮�ڷ� �����ϴ� ���� �Ϲ����̴�.! gui�� �����ϱ� ���� �⺻������ ��ӹ޾ƾ� �ϴ� JFrame

	private Image screenImage;
	private Graphics screenGraphic;
	
	private ImageIcon startButtonDefaultImage = new ImageIcon(Main.class.getResource("../images/start_button.jpg"));
	private ImageIcon startButtonHoverImage = new ImageIcon(Main.class.getResource("../images/start_button(hover).png"));
	
	private Image background = new ImageIcon(Main.class.getResource("../images/introBackground.png"))
			.getImage();
	
	private JButton startButton = new JButton(startButtonDefaultImage);
	
	
	public BlockGame() {  //������, Ŭ������ ���� �̸�����...
		setTitle("*BlockGame**"); //���� �̸�
		setSize(Main.CANVAS_WIDTH, Main.CANVAS_HEIGHT); //���� â ũ�� ����
		setResizable(false); //���� â�� �� �� ���������, ����ڰ� ������ �� ����
		setLocationRelativeTo(null); //���� â�� ��ǻ�� ���߾ӿ� �߰� ��
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //���� â �����, ��ü ���α׷� ����
		setVisible(true); //���� â�� ���������� ��µ�
		                 //�� �⺻���� false �� ������ true�� �־���� ��
		setLayout(null);
		
		startButton.setBounds(220, 300, 164, 66);
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
		startButton.setFocusPainted(false);
		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				startButton.setIcon(startButtonHoverImage);
				startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				startButton.setIcon(startButtonDefaultImage);
				startButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				startButton.setVisible(false);
				background = new ImageIcon(Main.class.getResource("../images/Default_background(black).png")).getImage();
				
			}
		});
		add(startButton);
		
		Music introMusic = new Music("AdhesiveWombat - Night Shade.mp3", true);
		introMusic.start();
	}
	public void paint(Graphics g) {
		
		//1280x720 ũ���� �̹����� ���� �־���
		screenImage = createImage(Main.CANVAS_WIDTH, Main.CANVAS_HEIGHT);
		
		//screenImage�� �̿��ؼ� �׷��� ��ü�� ����
		screenGraphic = screenImage.getGraphics();
		
		//���� ���� ���ο� �Լ�
		screenDraw(screenGraphic); 
		
		//���� â�� �̹����� ��Ÿ���� ��
		g.drawImage(screenImage, 0, 0, null);
	}
	
	public void screenDraw(Graphics g) {
		
		//introBackground�� screenImage�� �׷��� �� �ֵ���
		g.drawImage(background, 0, 0, null);
	    
		//���α׷��� ����Ǵ� �������� ��� paint �Լ��� �θ��鼭 �̹����� �����
		this.repaint();
	    
	}
}