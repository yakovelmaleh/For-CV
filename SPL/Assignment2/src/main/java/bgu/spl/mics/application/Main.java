package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import bgu.spl.mics.application.passiveObjects.Input;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;

/** This is the Main class of the application. You should parse the input file, 
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		try {
			Input input = parse(args[0]);
			Ewoks.getInstance(input.getEwoks());
			Thread[] t=new Thread[5];
			t[0]=new Thread(new LeiaMicroservice(input.getAttacks()));
			t[1]=new Thread(new HanSoloMicroservice());
			t[2]=new Thread(new C3POMicroservice());
			t[3]=new Thread(new R2D2Microservice(input.getR2D2()));
			t[4]=new Thread(new LandoMicroservice(input.getLando()));
			for (Thread thread: t){
				thread.start();
			}
			for (Thread thread: t)
			{
				thread.join();
			}
			FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter(args[1]);
				Gson gson = new Gson();
				gson.toJson(Diary.getInstance(), fileWriter);
			}
			catch (IOException e){
				System.out.println("IO Exception");
			}
			catch (Exception e){e.printStackTrace();}
			finally {
				if (fileWriter!=null)
					fileWriter.close();
			}
		}
		catch (Exception e){
			e.printStackTrace();
			System.out.println("no file found");
		}
	}
	private static Input parse(String filePath) throws IOException {
		Gson gson=new Gson();
		try(Reader reader= new FileReader(filePath)) {
			return gson.fromJson(reader, Input.class);
		}
	}
}
