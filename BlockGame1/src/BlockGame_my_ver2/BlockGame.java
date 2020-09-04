package BlockGame_my_ver2;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame; //ctrl + shift+ o �ϸ� �ʿ��� ��Ű��? �� �ڵ����� ����

public class BlockGame extends JFrame {  //Ŭ���� �̸��� �빮�ڷ� �����ϴ� ���� �Ϲ����̴�.! gui�� �����ϱ� ���� �⺻������ ��ӹ޾ƾ� �ϴ� JFrame

	private Image screenImage;
	private Graphics screenGraphic;
	
	private Image introBackground;
	
	public BlockGame() {  //������, Ŭ������ ���� �̸�����...
		setTitle("*BlockGame**"); //���� �̸�
		setSize(Main.CANVAS_WIDTH, Main.CANVAS_HEIGHT); //���� â ũ�� ����
		setResizable(false); //���� â�� �� �� ���������, ����ڰ� ������ �� ����
		setLocationRelativeTo(null); //���� â�� ��ǻ�� ���߾ӿ� �߰� ��
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //���� â �����, ��ü ���α׷� ����
		setVisible(true); //���� â�� ���������� ��µ�
		                 //�� �⺻���� false �� ������ true�� �־���� ��
		introBackground = new ImageIcon(Main.class.getResource("../Images/introBackground.png")).getImage();
	
		Music introMusic = new Music("")
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
		g.drawImage(introBackground, 0, 0, null);
	    
		//���α׷��� ����Ǵ� �������� ��� paint �Լ��� �θ��鼭 �̹����� �����
		this.repaint();
	    
	}
}