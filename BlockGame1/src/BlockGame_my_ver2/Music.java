package BlockGame_my_ver2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Music extends Thread {

	private Player player;

	private boolean isloop;
	private File file;
	private FileInputStream fis;
	private BufferedInputStream bis;

	public Music(String name, boolean isloop) { // 생성자 : 곡제목과 무한반복여부를 인자로
		try {
			this.isloop = isloop;

			file = new File(Main.class.getResource("../music/" + name).toURI());
			fis = new FileInputStream(file); // 파일을 버퍼에 담고
			bis = new BufferedInputStream(fis);
			player = new Player(bis);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int getTime() {
		if (player == null)
			return 0;
		return player.getPosition();
	}

	public void close() {
		isloop = false;
		player.close();
		this.interrupt();
	}

	@Override
	public void run() {
		try {
			do {
				player.play();
				fis= new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				player = new Player(bis);
			} while(isloop);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}