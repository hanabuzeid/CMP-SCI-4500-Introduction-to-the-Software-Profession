import java.util.ArrayList;
import java.util.Scanner;

//Class that runs the program of finding a zip number inside the PI digits 
public class Main {

	//Main starts the program
	public static void main(String[] args) {
		//First, we print an opening message
		System.out.println("--- Welcome to the ZIP number search on PI digits! ---");
		//We initialize a Scanner to read the inputs
		Scanner scan = new Scanner(System.in);
		//We first ask for a valid zip number. To do this, we use a loop to make sure the input is a positive 5-digit number
		String zip = "";
		boolean valid = false; //boolean that represents if input is valid zip or not
		while(!valid) {
			System.out.print("Enter the ZIP number: ");
			zip = scan.nextLine(); //Read next input from user
			//First, we check if we can convert it to an integer. We use try/catch in case the input isn't a valid integer
			try {
				int num = Integer.parseInt(zip); //Parse input to integer
				//Now, we check if number is posive and has 5 digits
				if(num > 0 && zip.length() == 5) {
					//If so, the zip code is valid
					valid = true;
				} else {
					throw new Exception(); //Throws exception to go to catch and print message
				}
			} catch (Exception e) {
				//We can print a message for invalid input
				System.out.println("Input must be a positive 5-digit number");
			}
		}
		//Once we have a valid zip code, we call for the method that finds the starting digit for the PI number where the zip exists
		findZipOnPi(zip);
		scan.close();
	}

	//Method that finds the starting digit of PI where the given zip number exists using the Spigot Algorithm
	//Helpful Source : https://www.cut-the-knot.org/Curriculum/Algorithms/SpigotForPi.shtml
	public static void findZipOnPi(String zip) {
		ArrayList<Integer> digits = new ArrayList<Integer>(); //stores all digits
		//Following is the Spigot Algorithm to find the digits of pi, one at a time
		int startDigit = -1; //meaning no digit is found yet for zip number
		int count = 0; //count variable that represents how many digits for the zip number have been found
		int n = 1000000; //num of max digits to find
		int[] A = new int[(10*n)/3+1];
		//Loop to fill up the array with 2's as needed for the algorithm
		for(int i = 0; i < A.length; i++) {
			A[i] = 2;
		}
		//Loop that finds next digit of PI number
		for(int i = 0; startDigit == -1 && i < n; i++) {
			//1. Multiply all A values by 10
			for(int j = 0; j < A.length; j++) {
				A[j] = A[j]*10;
			}
			//2. Loop from right to left to reduce all elements
			int carry = 0;
			for(int j = A.length; j >= 2; j--) { 
				//Find remainder and quotient as needed
				int r = (A[j-1]+carry) % (2*j-1);
				int q = (A[j-1]+carry) / (2*j-1);
				//Now, adjust carry and current value
				carry = q * (j-1);
				A[j-1] = r;
			}
			//3. Reduce leftmost entry of A to get the next digit and remainder
			int r = (A[0]+carry) % 10;
			int q = (A[0]+carry) / 10;
			//We store r as the new remainder at 0 index
			A[0] = r;
			//Here, q is the next digit for PI. 
			if(q <= 9) {
				digits.add(q); //add digit found to List
				//We check if the input zip at count has this digit
				if(Character.getNumericValue(zip.charAt(count)) == q) {
					count++; //one digit found, count++
				} else {
					count = 0;
				}
				// if count = 5, we set the starting digit as i-4
				if(count == 5) {
					startDigit = i-4;
				}
			} else {
				//Loop backwards increasing the previous until digit <= 9
				digits.add(10); //10 becomes 0
				int j = digits.size()-1;
				while(digits.get(j) == 10) {
					digits.set(j,0); //replace 10 by 0
					//Increase previous by 1
					digits.set(j-1,digits.get(j-1)+1);
					j--;
				}
				//Then, check again for zip starting at 4 digits before last changed
				j = j - 4;
				count = 0;
				while(j < digits.size() && startDigit == -1) {
					if(Character.getNumericValue(zip.charAt(count)) == digits.get(j)) {
						count++;      
					} else {
						count = 0;
					}
					// if count = 5, we set the starting digit as j-4
					if(count == 5) {
						startDigit = j-4;
					}
					j++;
				}
			}
		}
		//When the loop ends, we print the respective message
		if(startDigit != -1) {
			//This means the digit was found
			System.out.println("The ZIP number was found on PI digits starting at #"+(startDigit+1));
		} else {
			//This means it was not found on first 1,000,000 digits
			System.out.println("The ZIP number was not found on the first 1,000,000 digits");
		}
	}
}