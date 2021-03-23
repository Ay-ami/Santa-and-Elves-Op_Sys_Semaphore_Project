//import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;


public class Santa implements Runnable{

	enum SantaState {SLEEPING, READY_FOR_CHRISTMAS, WOKEN_UP_BY_ELVES, WOKEN_UP_BY_REINDEER};
	private SantaState state;
	private boolean kill;
	private SantaScenario scenario;
	
	public Santa(SantaScenario scenario) {
		this.state = SantaState.SLEEPING;
		this.scenario = scenario;
		setKill(false); // this is so that the run() method is allowed to spin, it stops when it is "killed"
	}
	
	
	@Override
	public void run() {
		while(this.kill == false) { 
			// wait a day...
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(state) {
			case SLEEPING: // if sleeping, continue to sleep
				break;
			case WOKEN_UP_BY_ELVES: 
				// go through the elves list and fix the problem of the ones who are at Santa's door
				for(Elf elf : scenario.elves) {
					if ( elf.getState() == Elf.ElfState.AT_SANTAS_DOOR)
					{
						elf.setState(Elf.ElfState.WORKING);
					}
				}
				scenario.elfSem.release(scenario.maxElfPermits); // we can release the permits now
				scenario.numElves = 0;
				
				//santa can go back to sleep now
				state = SantaState.SLEEPING;	
					
				break;
			case WOKEN_UP_BY_REINDEER: 
				// FIXME: assemble the reindeer to the sleigh then change state to ready 
				break;
			case READY_FOR_CHRISTMAS: // nothing more to be done
				break;
			}
		}
	}

	
	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Santa : " + state);
		//System.out.println("Santa killstate: " + this.kill);
	}
	
	public void setKill(boolean x)
	{
		this.kill = x;
		//System.out.println("Santa setKill() invoked");
	}

	public void wakeUpSanta()
	{
		this.state = SantaState.WOKEN_UP_BY_ELVES;
	}

	public boolean ifSantaAsleep() 
	{
		if ( this.state == SantaState.SLEEPING )
		{
			return true;
		}
		else{
			return false;
		}
	}
	
}
