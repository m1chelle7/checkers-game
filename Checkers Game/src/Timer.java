
public class Timer extends Game{
	
	// This method changes the timer.
	public static void timerUpdate() {

		
		if(numSecOnes != 9) {
			numSecOnes++;
		}
		
		else {
			numSecOnes = 0;
			
			if (numSecTens != 5) {
				numSecTens++;
			}
			
			else {
				numSecTens = 0;
				
				if (numMinOnes != 9) {
					numMinOnes++;
				}
				
				else {
					numMinOnes = 0;
					
					if (numMinTens != 9) {
						numMinTens++;
					}
					
					else if (numMinTens == 9 && numMinOnes == 9 && numSecTens == 5 && numSecOnes == 9) {
						System.out.println("hi");
					}
				}
			}
		}
		
		timer = numMinTens + "" + numMinOnes + ":" + numSecTens + "" + numSecOnes;
	
	}

}
