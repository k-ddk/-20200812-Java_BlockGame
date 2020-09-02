package BlockGame_my_ver2;

public class Main {

	//public static 이란 모든 프로젝트 내에서 공유하는 변수임을 의미
	//final은 한 번 선언된 후, 바뀌지 않음을 의미
	public static final int CANVAS_WIDTH = 440; //전체 너비
	public static final int CANVAS_HEIGHT = 600; //전체 높이
	
	public static void main(String[] args) {
		
		new BlockGame();
		
	}

}
