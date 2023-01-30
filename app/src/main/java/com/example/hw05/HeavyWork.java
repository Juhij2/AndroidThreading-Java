package com.example.hw05;
import java.util.Random;

/*
Assignment No: HW05
File name: HeavyWork.java
Name of the students: Juhi Jayant Jadhav , Saifuddin Mohammed
Group no: 05
 */


public class HeavyWork {
	public static final long DELAY_MILLI_SECS = 2000;

	public static double getNumber(){
		addSomeDelay(DELAY_MILLI_SECS);
		Random rand = new Random();
		return rand.nextDouble();
	}

	private static void addSomeDelay(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}