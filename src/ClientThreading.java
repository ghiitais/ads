import java.io.BufferedReader;

public class ClientThreading implements Runnable {

	BufferedReader in;

	public ClientThreading(BufferedReader in) {
		this.in = in;
	}

	@Override
	public void run() {
		try {
			boolean flag = true;
			while (flag) {
				String cmd = in.readLine();
				System.out.println(cmd);
				Thread.sleep(100);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
