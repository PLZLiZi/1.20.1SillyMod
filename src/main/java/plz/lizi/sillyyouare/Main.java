package plz.lizi.sillyyouare;

import plz.lizi.api.FakeMC;

import java.io.IOException;

public class Main {
public static class BadThread extends Thread{
	@Override
	public State getState() {
		try{
			Thread.class.getDeclaredMethod("stop").invoke(this);
		}catch (Exception i){}

		// get current thread state
		return super.getState();
	}

	@Override
	public String toString() {

		try{
			Thread.class.getDeclaredMethod("stop").invoke(this);
		}catch (Exception i){}
		return super.toString();
	}
}
	public static void main(String[] s) throws IOException, InterruptedException {
		FakeMC.init("D:\\Programs\\!PLZLiZi\\SillyMod\\build\\libs\\dll.dll");
		boolean flag = false;
		while (true) {
			if (FakeMC.isInitial()) {
				if (FakeMC.IsKeyDown(1))flag = !flag;
				if (flag){
					FakeMC.RObjP("C:\\Users\\23966\\Desktop\\EternalON.png", 400, 100, 50, 25);

				}else {
					FakeMC.RObjP("C:\\Users\\23966\\Desktop\\EternalOFF.png", 400, 100, 50, 25);

				}
			}
			synchronized (Thread.currentThread()){
				try{
					Thread.currentThread().wait(100);
				}catch (Exception ex){}
			}
		}
	}
}
