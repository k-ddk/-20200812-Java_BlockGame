package BlockGame_my_ver2;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame; //ctrl + shift+ o 하면 필요한 패키지? 가 자동으로 생김

public class BlockGame extends JFrame {  //클래스 이름은 대문자로 시작하는 것이 일반적이다.! gui를 구현하기 위해 기본적으로 상속받아야 하는 JFrame

	private Image screenImage;
	private Graphics screenGraphic;
	
	private Image introBackground;
	
	public BlockGame() {  //생성자, 클래스와 같은 이름으로...
		setTitle("*BlockGame**"); //게임 이름
		setSize(Main.CANVAS_WIDTH, Main.CANVAS_HEIGHT); //게임 창 크기 설정
		setResizable(false); //게임 창이 한 번 만들어지면, 사용자가 변경할 수 없음
		setLocationRelativeTo(null); //게임 창이 컴퓨터 정중앙에 뜨게 됨
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //게임 창 종료시, 전체 프로그램 종료
		setVisible(true); //게임 창이 정상적으로 출력됨
		                 //의 기본값은 false 기 때문에 true로 넣어줘야 함
		introBackground = new ImageIcon(Main.class.getResource("../Images/introBackground.png")).getImage();
	
		Music introMusic = new Music("")
	}
	public void paint(Graphics g) {
		
		//1280x720 크기의 이미지를 만들어서 넣어줌
		screenImage = createImage(Main.CANVAS_WIDTH, Main.CANVAS_HEIGHT);
		
		//screenImage를 이용해서 그래픽 개체를 얻어옴
		screenGraphic = screenImage.getGraphics();
		
		//직접 만들 새로운 함수
		screenDraw(screenGraphic); 
		
		//게임 창에 이미지가 나타나게 됨
		g.drawImage(screenImage, 0, 0, null);
	}
	
	public void screenDraw(Graphics g) {
		
		//introBackground가 screenImage에 그려질 수 있도록
		g.drawImage(introBackground, 0, 0, null);
	    
		//프로그램이 종료되는 순간까지 계속 paint 함수를 부르면서 이미지를 띄워줌
		this.repaint();
	    
	}
}