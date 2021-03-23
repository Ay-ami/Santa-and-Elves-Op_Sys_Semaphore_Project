import java.util.Random;

public class Elf implements Runnable {

	enum ElfState {
		WORKING, TROUBLE, AT_SANTAS_DOOR
	};

	private ElfState state;
	/**
	 * The number associated with the Elf
	 */
	private int number;
	private Random rand = new Random();
	private SantaScenario scenario;
	private boolean kill;

	public Elf(int number, SantaScenario scenario) {
		this.number = number;
		this.scenario = scenario;
		this.state = ElfState.WORKING;
		setKill(false); // this is so that the run() method is allowed to spin, it stops when it is "killed"
	}


	public ElfState getState() {
		return state;
	}

	/**
	 * Santa might call this function to fix the trouble
	 * @param state
	 */
	public void setState(ElfState state) {
		this.state = state;
	}


	@Override
	public void run() {
		while (this.kill == false) {
			// wait a day
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// remember: this elf's number is actually 1 less than their "elves" list placement, which is why we use (this.number - 1)
			Elf currentElf = scenario.elves.get( this.number - 1 );
			
			switch (state) {
			case WORKING: {
				// at each day, there is a 1% chance that an elf runs into
				// trouble.
				if (rand.nextDouble() < 0.01) {
					state = ElfState.TROUBLE;
					
					// try to aquire a permit if available
					try {
						scenario.elfSem.acquire();
						scenario.numElves++; // = scenario.maxElfPermits - scenario.elfSem.availablePermits(); // number of elves in trouble = maximum permits - how many permits are left					
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
					// notice we never call scenario.elfSem.release(), that happens after Santa is done fixing the problem
				}
				break;
			}
			case TROUBLE:
				if ( scenario.numElves < 3 )
				{
					// wait for turn
				}
				else {
					currentElf.setState(Elf.ElfState.AT_SANTAS_DOOR);
				}
				
				break;
			case AT_SANTAS_DOOR:
				// wake up santa if he isn't already awake and if there are 3 elves in trouble
				if ( scenario.santa.ifSantaAsleep() == true && scenario.numElves == scenario.maxElfPermits )
				{
					scenario.santa.wakeUpSanta();
				}
				break;
			}
		}
	}

	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Elf " + number + " : " + state);
	}
	
	public void setKill(boolean x)
	{
		this.kill = x;
	}

	public int getNumber()
	{
		return this.number;
	}

	

}
