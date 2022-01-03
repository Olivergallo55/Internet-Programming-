/*
 * @author Oliver Gallo olga7031 Assignment 1.1 and 1.3 for IPROG course, about threading and packaging to jar file
 */

/*
 * Multitråding is the main class running the requirements for assignment  
 */
public class Main {
	
	/*
	 * T1 class extends thread and runs while the end method is called, which brakes the loop
	 */
	public static void main(String[] args) {  
		
		
		try {
			System.out.println("\nThread 1 starts\n");
			T1 t1 = new T1(); 
			t1.start();
			
			Thread.sleep(5000L);
			
			System.out.println("\nThread 2 starts\n");
			Thread t2 = new Thread(new T2());
			t2.start();
			
			Thread.sleep(5000);
			
			System.out.println("\nThread one dies now\n");
			t1.kill();
			Thread.sleep(5000);
			
			t2.interrupt();
			System.out.println("\nThread two dies now\n");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/*
 * T1 class extends thread and runs while the end method is called, which brakes the loop
 */
class T1 extends Thread{
	
	boolean kill; //variable to control the thread
	
	
	public void run() {
		
		kill = true;
		
		while(kill) {
			try {
				System.out.println("Tråd T1: Tråd 1");
				Thread.sleep(1000L);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void kill() {
		kill = false;
	}
}

/*
 * T2 class implements runnable and runs till the end method is called, then interrupts the loop
 */
class T2 implements Runnable{
	
	
	//private Thread t2 = new Thread(this); //Initialize a variable for the thread 
	boolean kill; 
	
	public T2() {
		//t2.start(); 
	}

	@Override
	public void run() {
	
		kill = true; 
		
		while(kill) { 
			
			try {
			System.out.println("Tråd T2: Tråd 2");
			Thread.sleep(1000L);
			
			
			}catch(InterruptedException ie) {kill = false;}//ends loop if thread is interrupted
		}
	}
}