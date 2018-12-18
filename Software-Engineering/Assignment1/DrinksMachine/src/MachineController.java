//Thomas Coupe, 201241037

package ac.liv.csc.comp201;

import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.Drinks;
import ac.liv.csc.comp201.model.IMachineController;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;

public class MachineController  extends Thread implements IMachineController {
	
	private boolean running=true;
	
	private IMachine machine;
	private static final String[] coinCodes = {"ab", "ac", "ba", "bc", "bd", "ef"};
	private static final int[] coinValues = {1, 5, 10, 20, 50, 100};
	private static final String version="1.22";
	private int[] inputCode = {-1, -1, -1, -1};
	private Drinks drink = new Drinks();
	private String keyCodeString;
	private Cup cup;
	private int state = 1;
	
	public void startController(IMachine machine) {
		this.machine=machine;				// Machine that is being controlled
		/*machine.getKeyPad().setCaption(0,"Cup");
		machine.getKeyPad().setCaption(1,"Water heater on");
		machine.getKeyPad().setCaption(2,"Water heater off");		
		machine.getKeyPad().setCaption(3,"Hot Water On");
		machine.getKeyPad().setCaption(4,"Hot Water Off");		
		machine.getKeyPad().setCaption(5,"Dispense coffee");
		machine.getKeyPad().setCaption(6,"Dispense milk");
		machine.getKeyPad().setCaption(7,"Cold water on");
		machine.getKeyPad().setCaption(8,"Cold water off");
		machine.getKeyPad().setCaption(9,"Reject coins");
		*/
		machine.getKeyPad().setCaption(7,"Delete Code");
		machine.getKeyPad().setCaption(8,"Enter Code");
		machine.getKeyPad().setCaption(9,"Reject Coin");
		super.start();
	}
	
	
	public MachineController() {
					
	}
	
	
	private synchronized void runStateMachine() {
		//System.out.println("Running state machine");
		
		// TO DO 
		// Add your code to run the drinks machine
		// in here
		// There is some basic code to show
		// you the working of the machine
		cup=machine.getCup();
		if (cup!=null) {

		}
		int keyCode=machine.getKeyPad().getNextKeyCode();
	//	System.out.println("Key is "+keyCode);
		String coinCode=machine.getCoinHandler().getCoinKeyCode();
		if (coinCode!=null) {
			System.out.println("Got coin code .."+coinCode);
			machine.getDisplay().setTextString("Got coin code .."+coinCode);
			for (int i = 0; i < coinValues.length; i++) {
				if(coinCodes[i] == coinCode) {
					machine.setBalance(machine.getBalance()+coinValues[i]);
					machine.getDisplay().setTextString("Balance: "+Integer.toString(machine.getBalance()));
					break;
				}
			}
		}
		switch (keyCode) {
			case 0 :
				System.out.println("Vending cup");
				inputKeyCode(keyCode);
				break;
			case 1 :
				inputKeyCode(keyCode);							
				break;
			case 2 :
				inputKeyCode(keyCode);
				break;
			case 3 :
				inputKeyCode(keyCode);
				break;
			case 4 :
				inputKeyCode(keyCode);
				break;
			case 5 :
				inputKeyCode(keyCode);
				break;
			case 6 :
				inputKeyCode(keyCode);
				break;			
			case 7 :
				deleteKeyCode(keyCode);
				break;
			case 8 :
				decodeInput(inputCode);

			case 9 :
				coinReturn();
				System.out.println("Returning coins");
				System.out.println("Current balance: "+machine.getBalance());
				
		}
		if (machine.getBalance() >= 110) {
			if(inputCode[3] != -1) {
				makeDrinks();
			}
		}
	}
	
	
	public void run() {
		// Controlling thread for coffee machine
		int counter=1;
		while (running) {
			//machine.getDisplay().setTextString("Running drink machine controller "+counter);
			counter++;
			try {
				Thread.sleep(10);		// Set this delay time to lower rate if you want to increase the rate
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runStateMachine();
			maintainHeater(state);
			//decodeInput(inputCode);
		}		
	}


	public void updateController() {
		//runStateMachine();
	}
	
	public void stopController() {
		running=false;
	}
//this method returns the highest value coin possible when the user requests to reject coins
//from the machine. the method iterates backwards through the CoinCodes array checking if
//the coins are available
	public void coinReturn() { 
		for(int i = coinValues.length - 1; i >= 0; i--) {
			while(true) {
				if(machine.getCoinHandler().coinAvailable(coinCodes[i])) {
					if (machine.getBalance() - coinValues[i] >= 0) {
						machine.setBalance(machine.getBalance() - coinValues[i]);
						machine.getCoinHandler().dispenseCoin(coinCodes[i]);
					}
					else {
						break;
					}
				}
				else {
					System.out.println("Coin not available");
					break;
				}
			}
		}
	}
	//this method takes an int (keyCode number) and adds that int to the inputCode array
	//if there is a -1 value in the array, it is replaced with the int from the parameters
	public void inputKeyCode(int n) {
		for (int i = 0; i < inputCode.length; i++) {
			if((int) inputCode[i] == -1) {
				inputCode[i] = n;
				break;	
			}
		}
		update_display();
	}
//this method maintains what temperature the water heater should be at.
	public void maintainHeater(int state) {
		switch (state) {
	        case 1: //resting state
	            if (machine.getWaterHeater().getTemperatureDegreesC() < 80) {
	                machine.getWaterHeater().setHeaterOn();
	            }
	            else {
	                machine.getWaterHeater().setHeaterOff();
	            }
	            break;
	        case 2: //normal drink temp
	        	machine.getWaterHeater().setHeaterOn();
	            if(machine.getWaterHeater().getTemperatureDegreesC() >= 95.9) {
	            	machine.getWaterHeater().setHeaterOff();
	            }
	            break;
	        case 3: //hot choc temp
	        	machine.getWaterHeater().setHeaterOn();
	        	if(machine.getWaterHeater().getTemperatureDegreesC() <= 90.0) {
	        		machine.getWaterHeater().setHeaterOn();
	        	}
	        	else {
	        		machine.getWaterHeater().setHeaterOff();
	        	}
	        	break;
	    }
    }
//this method updates the display with the keycode input into the drinks machine.
//this tells the user what code they have already inputted so far.
	public void update_display() {
		String str = "";
		for (int i = 0; i < inputCode.length; i++) {
			if (inputCode[i] == -1) {
				break;
			}
			str += Integer.toString(inputCode[i]);
		}
		machine.getDisplay().setTextString(str);		
	}
//deletes the last value of the inputCode array and updates to the display so the user can see.
	public void deleteKeyCode(int n) {
		for (int i = inputCode.length - 1; i >= 0; i--) {
			if((int) inputCode[i] != -1) {
				inputCode[i] = -1;
				break;
			}
		}
		update_display();
	}
	
//this method check what cup to give the user based on the first number input into the keypad
//if the first number is not a 5 or 6, Cupsize defaults to small.
	public void decodeInput(int[] code) {
		// checking cup size selected by the user
		if (code.length == 4) {
			if (code[0] == 5) {
				drink.setCup("medium");
			} else if (code[0] == 6) {
				drink.setCup("large");
			} else {
				if(code[3] == -1) {
					//re-arranging the values in the array to assign the first value of the array 
					//to -1 to avoid errors later on in the program.
					int tmp = code[0];
					int tmp2 = code[1];
					int tmp3 = code[2];
					code[0] = code[3];
					code[1] = tmp;
					code[2] = tmp2;
					code[3] = tmp3;	
				}
			}

			if (drink.getDrinkName() == "medium")
				machine.vendCup(Cup.MEDIUM_CUP);
			else if (drink.getDrinkName() == "large")
				machine.vendCup(Cup.LARGE_CUP);
			else 
				machine.vendCup(Cup.SMALL_CUP);
		} else {
			System.out.println("No cup size specified, small cup selected");
			drink.setCup("small");
		}
	}
//make a seperate function to make the drinks
//call the decode function in the makedrinks
	public void makeDrinks() {
 		if (inputCode[1] == 1) {
			if (inputCode[2] == 0 && inputCode[3] == 1) {
				if (machine.getBalance() >= drink.getBlackCoffee()) {
					state = 2;
					machine.getWaterHeater().setHotWaterTap(true);
					machine.getHoppers().setHopperOn(Hoppers.COFFEE);
					//cup is being set to null for some reason and never runs correctly.
					if (cup != null)
					{
						System.out.println("cup is not null");
						if (cup.getCoffeeGrams() >= drink.getCoffeeAmount()) {
							machine.getHoppers().setHopperOff(Hoppers.COFFEE);
							System.out.println(machine.getHoppers().getHopperLevelsGrams(Hoppers.COFFEE));
							System.out.println("testing coffee");
						}
					}
					state = 1;
					machine.setBalance((machine.getBalance() - drink.getBlackCoffee()));
				} else {
					System.out.println("You have not inserted enough coins");
				}

			} else if (inputCode[2] == 0 && inputCode[3] == 2) {
				if (machine.getBalance() >= drink.getBlackSugarCoffee()) {
					state = 2;
					machine.getWaterHeater().setHotWaterTap(true);
					machine.getHoppers().setHopperOn(Hoppers.SUGAR);
					machine.getHoppers().setHopperOn(Hoppers.COFFEE);
					if (cup != null) {
						if (cup.getCoffeeGrams() >= drink.getCoffeeAmount()) {
							machine.getHoppers().setHopperOff(Hoppers.COFFEE);
						}
						if (cup.getSugarGrams() >= drink.getSugarAmount()) {
							machine.getHoppers().setHopperOff(Hoppers.SUGAR);
						}
					}
					state =1;
					machine.setBalance(machine.getBalance() - drink.getBlackSugarCoffee());

				} else {
					System.out.println("You have not inserted enough coins");
				}
			} else {
				System.out.println("Invalid Drink Option");
				for (int i = 0; i < inputCode.length; i++) {
					deleteKeyCode(inputCode[i]);
				}
			}
		} else if (inputCode[1] == 2) {
			if (inputCode[2] == 0 && inputCode[3] == 1) {
				if (machine.getBalance() >= drink.getWhiteCoffee()) {
					state = 2;
					machine.getWaterHeater().setHotWaterTap(true);
					machine.getHoppers().setHopperOn(Hoppers.COFFEE);
					machine.getHoppers().setHopperOn(Hoppers.MILK);
					if (cup != null) {
						if (cup.getCoffeeGrams() >= drink.getCoffeeAmount()) {
							machine.getHoppers().setHopperOff(Hoppers.COFFEE);
						}
						if (cup.getMilkGrams() >= drink.getMilkAmount()) {
							machine.getHoppers().setHopperOff(Hoppers.MILK);
						}
					}
					state = 1;
					machine.setBalance(machine.getBalance() - drink.getWhiteCoffee());
				} else {
					System.out.println("You have not inserted enough coins");
				}
			} else if (inputCode[2] == 0 && inputCode[3] == 2) {
				if (machine.getBalance() >= drink.getWhiteSugarCoffee()) {
					state = 2;
					machine.getWaterHeater().setHotWaterTap(true);
					machine.getHoppers().setHopperOn(Hoppers.COFFEE);
					machine.getHoppers().setHopperOn(Hoppers.MILK);
					machine.getHoppers().setHopperOn(Hoppers.SUGAR);
					if(cup != null ) {
						if (cup.getCoffeeGrams() >= drink.getCoffeeAmount()) {
							machine.getHoppers().setHopperOff(Hoppers.COFFEE);
						} 
						if (cup.getMilkGrams() >= drink.getMilkAmount()) {
							machine.getHoppers().setHopperOff(Hoppers.MILK);
						}
						if (cup.getSugarGrams() >= drink.getSugarAmount()) {
							machine.getHoppers().setHopperOff(Hoppers.SUGAR);
						}
					}
					state = 1;
					machine.setBalance(machine.getBalance() - drink.getWhiteCoffee());
				} else {
					System.out.println("You have not inserted enough coins");
				}
			} else {
				System.out.println("Invalid Drink Option");
				for (int i = 0; i < inputCode.length; i++) {
					deleteKeyCode(inputCode[i]);
				}
			}
		} else if (inputCode[1] == 3) {
			if (inputCode[2] == 0 && inputCode[3] == 0) {
				if (machine.getBalance() >= drink.getHotChoc()) {
					state = 3;
					machine.getWaterHeater().setHotWaterTap(true);
					machine.getHoppers().setHopperOn(Hoppers.CHOCOLATE);
					if (cup != null) {
						if (machine.getCup().getChocolateGrams() >= drink.getHotChocAmount()) {
							machine.getHoppers().setHopperOff(Hoppers.CHOCOLATE);
						}
					}
					state = 1;
				} else {
					System.out.println("You have not inserted enough coins");
				}
			} else {
				// deal with invalid input, delete inputCode
				System.out.println("Invalid Drink Option");
				for (int i = 0; i < inputCode.length; i++) {
					deleteKeyCode(inputCode[i]);
				}
			}

		}
		
	}
	

}
