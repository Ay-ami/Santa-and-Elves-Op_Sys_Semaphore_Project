import java.util.Random;


public class Reindeer implements Runnable {

	public enum ReindeerState {AT_BEACH, AT_WARMING_SHED, AT_THE_SLEIGH};
	private ReindeerState state;
	private SantaScenario scenario;
	private Random rand = new Random();
	private boolean kill;

	/**
	 * The number associated with the reindeer
	 */
	private int number;
	
	public Reindeer(int number, SantaScenario scenario) {
		this.number = number;
		this.scenario = scenario;
		this.state = ReindeerState.AT_BEACH;
		setKill(false); // this is so that the run() method is allowed to spin, it stops when it is "killed"
	}

	@Override
	public void run() {
		while(kill == false) {
			// wait a day
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// see what we need to do:
			switch(state) {
			case AT_BEACH: { // if it is December, the reindeer might think about returning from the beach
				if (scenario.isDecember) {
					if (rand.nextDouble() < 0.1) {
						state = ReindeerState.AT_WARMING_SHED;
					}
				}
				break;			
			}
			case AT_WARMING_SHED: 
				// if all the reindeer are home, wake up santa
				break;
			case AT_THE_SLEIGH: 
				// keep pulling
				break;
			}
		}
	};
	
	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Reindeer " + number + " : " + state);
		//System.out.println("Reindeer killstate: " + this.kill);
	}
	public void setKill(boolean x)
	{
		this.kill = x;
		//System.out.println("Reindeer setKill() invoked");
	}
}
