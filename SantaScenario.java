import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*; 


public class SantaScenario {

	public Santa santa;
	public List<Elf> elves;
	public List<Reindeer> reindeers;
	public boolean isDecember;

	public int maxElfPermits = 3;
	public int numElves = 0;
	Semaphore elfSem = new Semaphore(maxElfPermits, true);

	
	public static void main(String args[]) throws InterruptedException {
		SantaScenario scenario = new SantaScenario();
		scenario.isDecember = false;
		
		/* create the participants*/
		// Santa
		scenario.santa = new Santa(scenario);
		Thread th = new Thread(scenario.santa);
		th.start();
		// The elves: in this case: 10
		scenario.elves = new ArrayList<>();
		for(int i = 0; i != 10; i++) {
			Elf elf = new Elf(i+1, scenario);
			scenario.elves.add(elf);
			th = new Thread(elf);
			th.start();
		}
		// The reindeer: in this case: 0
		scenario.reindeers = new ArrayList<>();
		for(int i=0; i != 0; i++) {
			Reindeer reindeer = new Reindeer(i+1, scenario);
			scenario.reindeers.add(reindeer);
			th = new Thread(reindeer);
			th.start();
		}
		
		/* now, start the passing of time*/
			for(int day = 1; day < 500; day++) {
				if (day == 370) // even past 370, this keeps going to 500 afterwards but nothing else should be visibly changing
				{
					scenario.santa.setKill(true);
					for(Elf elf: scenario.elves) {
						elf.setKill(true);
					}
					for(Reindeer reindeer: scenario.reindeers) {
						reindeer.setKill(true);
					}
					break; /////////////////////////////
					
				}
				// wait a day
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// turn on December
				if (day > (365 - 31)) {
					scenario.isDecember = true;
				}
				
				// print out the state:
				System.out.println("***********  Day " + day + " *************************");
				scenario.santa.report();
				for(Elf elf: scenario.elves) {
					elf.report();
				}
				for(Reindeer reindeer: scenario.reindeers) {
					reindeer.report();
				}
			} //end of for
			//System.out.println("end of program");
			
	}// end of main
	
}
