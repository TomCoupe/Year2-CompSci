//Thomas Coupe, 201241037
package ac.liv.csc.comp201;
import ac.liv.csc.comp201.model.*;
import ac.liv.csc.comp201.simulate.*;
import ac.liv.csc.comp201.*;

public class Drinks {
	private IMachine machine;
	private int blackCoffeeCost = 120;
	private int blackCoffeeSugarCost = 130;
	private int whiteCoffeeCost = 120;
	private int whiteCoffeeSugarCost = 130;
	private int hotChocCost = 110;
	private int cupCost;
	
	//private double temperature;
	private double coffeeGrams;
	private double totalCost;
	private double sugar;
	private double milkPowder;
	private double chocPowder;
	private String drinkName;
	//cup sizes in litres
	private double cupSize;
	
	//public Drinks(int[] code) {
		
		
	//}
	//public Drinks(int[] code) {
		//(newcup / smallcup)*ingredient
		/*int index = 0;
		// checking cup size selected by the user
		if (code.length == 4) {
			if (code[0] == 5) {
				setCup("medium");

			} else if (code[0] == 6) {
				setCup("large");
			} else {
				// if the first values of the 4 length code are not 5 or 6,
				// set all values to null as its an invalid input
				for (int i = 0; i < code.length; i++) {
					code[i] = -1;
				}
			}

			if (getDrinkName() == "medium")
				machine.vendCup(Cup.MEDIUM_CUP);
			else if (getDrinkName() == "large")
				machine.vendCup(Cup.LARGE_CUP);
			else 
				machine.vendCup(Cup.SMALL_CUP);
			index++;
		} else {
			System.out.println("No cup size specified, small cup selected");
			setCup("small");
		}
		if (code[0 + index] == 1) {
			if (code[1 + index] == 0 && code[2 + index] == 1) {
				if (machine.getBalance() >= getBlackCoffee()) {
					MachineController.maintainHeater(false);
					machine.getCup().addWater(getCupSize(), 95.9);
					machine.getHoppers().setHopperOn(Hoppers.COFFEE);
					if (machine.getCup().getTemperatureInC() >= 95.9) {
						System.out.println("reached this point");
						maintainHeater(true);
					}
					if (machine.getCup().getCoffeeGrams() >= 2.0) {
						machine.getHoppers().setHopperOff(Hoppers.COFFEE);
					}
					machine.setBalance((machine.getBalance() - getBlackCoffee()));
				} else {
					System.out.println("You have not inserted enough coins");
				}

			} else if (code[1 + index] == 0 && code[2 + index] == 2) {
				if (machine.getBalance() >= getBlackSugarCoffee()) {

					machine.getCup().addWater(getCupSize(), 95.9);
					machine.getHoppers().setHopperOn(Hoppers.SUGAR);
					machine.getHoppers().setHopperOn(Hoppers.COFFEE);
					if (machine.getCup().getTemperatureInC() >= 95.9) {

					}
					if (machine.getCup().getCoffeeGrams() >= 2.0) {
						machine.getHoppers().setHopperOff(Hoppers.COFFEE);
					} else if (machine.getCup().getSugarGrams() >= 5.0) {
						machine.getHoppers().setHopperOff(Hoppers.SUGAR);
					}
					machine.setBalance(machine.getBalance() - getBlackSugarCoffee());

				} else {
					System.out.println("You have not inserted enough coins");
				}
			} else {
				System.out.println("Invalid Drink Option");
				for (int i = 0; i < code.length; i++) {
					deleteKeyCode(code[i]);
				}
			}
		} else if (code[0 + index] == 2) {
			if (code[1 + index] == 0 && code[2 + index] == 1) {
				if (machine.getBalance() >= getWhiteCoffee()) {
					maintainHeater(false);
					machine.getCup().addWater(getCupSize(), 95.9);
					machine.getHoppers().setHopperOn(Hoppers.COFFEE);
					machine.getHoppers().setHopperOn(Hoppers.MILK);
					if (machine.getCup().getTemperatureInC() >= 95.9) {
						maintainHeater(true);
					}
					if (machine.getCup().getCoffeeGrams() >= 2.0) {
						machine.getHoppers().setHopperOff(Hoppers.COFFEE);
					} else if (machine.getCup().getMilkGrams() >= 3.0) {
						machine.getHoppers().setHopperOff(Hoppers.MILK);
					}
					machine.setBalance(machine.getBalance() - getWhiteCoffee());
				} else {
					System.out.println("You have not inserted enough coins");
				}
			} else if (code[1 + index] == 0 && code[2 + index] == 2) {
				if (machine.getBalance() >= getWhiteSugarCoffee()) {
					maintainHeater(false);
					machine.getCup().addWater(getCupSize(), 95.9);
					machine.getHoppers().setHopperOn(Hoppers.COFFEE);
					machine.getHoppers().setHopperOn(Hoppers.MILK);
					machine.getHoppers().setHopperOn(Hoppers.SUGAR);
					if (machine.getCup().getTemperatureInC() >= 95.9) {
						maintainHeater(true);
					}
					if (machine.getCup().getCoffeeGrams() >= 2.0) {
						machine.getHoppers().setHopperOff(Hoppers.COFFEE);
					} else if (machine.getCup().getMilkGrams() >= 3.0) {
						machine.getHoppers().setHopperOff(Hoppers.MILK);
					} else if (machine.getCup().getSugarGrams() >= 5.0) {
						machine.getHoppers().setHopperOff(Hoppers.SUGAR);
					}
					machine.setBalance(machine.getBalance() - getWhiteCoffee());
				} else {
					System.out.println("You have not inserted enough coins");
				}
			} else {
				System.out.println("Invalid Drink Option");
				for (int i = 0; i < code.length; i++) {
					deleteKeyCode(code[i]);
				}
			}
		} else if (code[0 + index] == 3) {
			if (code[1 + index] == 0 && code[2 + index] == 0) {
				if (machine.getBalance() >= getHotChoc()) {
					maintainHeater(false);
					machine.getCup().addWater(getCupSize(), 90.0);
					machine.getHoppers().setHopperOn(Hoppers.CHOCOLATE);
					if (machine.getCup().getTemperatureInC() >= 90.0) {
						maintainHeater(true);
					}
					if (machine.getCup().getChocolateGrams() >= 28.0) {
						machine.getHoppers().setHopperOff(Hoppers.CHOCOLATE);
					}
				} else {
					System.out.println("You have not inserted enough coins");
				}
			} else {
				// deal with invalid input, delete code
				System.out.println("Invalid Drink Option");
				for (int i = 0; i < code.length; i++) {
					deleteKeyCode(code[i]);
				}
			}

		}
		
	}*/
		
	//}
 	
	public int setCup(String n) {
		drinkName = n;
		if (n == "medium") {
			cupSize = 0.45;
			cupCost = 20;
			return cupCost;
		}
		else if (n == "large") {
			cupSize = 0.56;
			cupCost = 25;
			return cupCost;
		}
		else {
		// if not medium or large it must be small so cupCost defaults to 0
			cupSize = 0.34;
			cupCost = 0;
			return cupCost;
		}	
	}
	/*public void makeBlackCoffee() {
		if(machine.getWaterHeater().getTemperatureDegreesC() == 95.9) {
			machine.getWaterHeater().setHotWaterTap(true);
			if (machine.getCup().getWaterLevelLitres() == getCupSize()) {
				machine.getWaterHeater().setHotWaterTap(false);
			}
			machine.getHoppers().setHopperOn(Hoppers.COFFEE);
			if(machine.getCup().getCoffeeGrams() >= getCoffeeAmount()) {
				machine.getHoppers().setHopperOff(Hoppers.COFFEE);
			}
		}
	}*/
	
	
	public double getCupCost() {
		return cupCost;
	}
	
	public double getCupSize() {
		return cupSize;
	}
	
	public String getDrinkName() {
		return drinkName;
	}
	
	public int getBlackCoffee() {
		return blackCoffeeCost + cupCost;
	}
	public int getBlackSugarCoffee() {
		return blackCoffeeSugarCost + cupCost;
	}
	public int getWhiteCoffee() {
		return whiteCoffeeCost + cupCost;
	}
	public int getWhiteSugarCoffee() {
		return whiteCoffeeSugarCost + cupCost;
	}
	public int getHotChoc() {
		return hotChocCost + cupCost;
	}
	public double getCoffeeAmount() {
		//(new cup size / small cup size) * ingredient
		if (cupSize == 0.45) {
			coffeeGrams = (0.45/0.34) * 2;
		}
		else if(cupSize == 0.56) {
			coffeeGrams = (0.56/0.34) * 2;
		}
		else {
			coffeeGrams = 2;
		}
		return coffeeGrams;
	}
	
	public double getSugarAmount() {
		if (cupSize == 0.45) {
			sugar = (0.45/0.34) * 5;
		}
		else if (cupSize == 0.56) {
			sugar = (0.56/0.34) * 5;
		}
		else {
			sugar = 5;
		}
		return sugar;
	}
	
	public double getMilkAmount() {
		if(cupSize == 0.45) {
			milkPowder = (0.45/0.34) * 3;
		}
		else if(cupSize == 0.56) {
			milkPowder = (0.56/0.34) * 3;
		}
		else {
			milkPowder = 3;
		}
		return milkPowder;
	}
	
	public double getHotChocAmount() {
		if(cupSize == 0.45) {
			chocPowder = (0.45/0.34) * 28;
		}
		else if(cupSize == 0.56) {
			chocPowder = (0.56/0.34) * 28;
		}
		else {
			chocPowder = 28;
		}
		return chocPowder;
	}
	
	public double getDrinkTempNormal() {
		return 95.9;
	}
	public double getHotChocTemp() {
		return 90.0;
	}
	

}
