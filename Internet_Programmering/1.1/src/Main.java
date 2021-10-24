/*
 *  Assignment 1.1, 1.2 and 1.3 for IPROG course, about threading 
 * @author Oliver Gallo olga7031
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
			T1 t1 = new T1();
			t1.start();
			Thread.sleep(5000); 
			
			T2 t2 = new T2();
			Thread.sleep(5000);
			
			t1.end();
			Thread.sleep(5000);
			
			t2.end();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/*
 * T1 class extends thread and runs while the end method is called, which brakes the loop
 */
class T1 extends Thread{
	
	boolean check; //variable to control the thread
	
	
	@Override
	public void run() {
		
		check = true;
		
		while(check) {
			try {
			Thread.sleep(5000);
			System.out.println("Tråd T1: Tråd 1");
			
			
			}catch(InterruptedException ie) {}
		}
	}
	
	public void end() {
		check = false;
	}
}

/*
 * T3 class implements runnable and runs till the end method is called, then interrupts the loop
 */
class T2 implements Runnable{
	
	
	private Thread t2 = new Thread(this); //Initialize a variable for the thread 
	boolean check; 
	
	public T2() {
		t2.start();
	}

	@Override
	public void run() {
	
		check = true; 
		
		while(check) {
			
			try {
			System.out.println("Tråd T2: Tråd 2");
			Thread.sleep(5000);
			
			
			}catch(InterruptedException ie) {check = false;}//ends loop if thread is interrupted
		}
	}
	
	public void end() {
		t2.interrupt(); 
	}
}


