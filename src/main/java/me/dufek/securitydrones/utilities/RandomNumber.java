package me.dufek.securitydrones.utilities;

import java.util.Random;

/**
 *
 * @author Jan Dufek
 */
public class RandomNumber {

    public static double getDouble(double number1, double number2) {

        double min = Math.min(number1, number2);
        double max = Math.max(number1, number2);

        Random random = new Random();
        
        double randomDouble = random.nextDouble();
        double result = min + (randomDouble * (max - min));

        return result;
    }
    
    public static int getInteger(int number1, int number2) {

        int min = Math.min(number1, number2);
        int max = Math.max(number1, number2);

        Random random = new Random();

        int randomInteger = random.nextInt(max - min + 1);
        int result = min + randomInteger;

        return result;
    }
    
    public static double getNormal(double mean, double standardDeviation) {
        Random random = new Random();
        
        double randomNormal = mean + standardDeviation * random.nextGaussian();
        
        return randomNormal;
    }
}
