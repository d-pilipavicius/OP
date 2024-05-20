package number_work;

import java.util.Locale;
public final class NumeralWork {
    private NumeralWork() {}
    public static double roundFloat(double number, int afterDecimal) {
        String temp = String.format(Locale.US, "%f", number);
        int tempPoint = 0;
        for(int i = 0; i < temp.length(); ++i) {
            if(temp.charAt(i) == '.') {
                tempPoint = i;
                break;
            }
        }
        if(tempPoint == 0 || temp.length()-tempPoint-1 <= afterDecimal)
            return number;
        else {
            temp = temp.substring(0, tempPoint+afterDecimal+1);
            return Double.parseDouble(temp);
        }
    }
    public static int getNumberLength(double number) {
        if(number == (int) number) {
            return Integer.toString((int) number).length();
        }
        String nr = String.format(Locale.US, "%f", number);
        int nrLength = nr.length();
        for(int i = 0; i < nr.length(); ++i) {
            if(nr.charAt(i) == '.') {
                nrLength--;
                break;
            }
        }
        return nrLength;
    }
    public static int getNumberLength(int number) {
        return Integer.toString(number).length();
    }
    public static int sumIntArray(int[] numbers) {
        int temp = 0;
        for(int number : numbers) {
            temp += number;
        }
        return temp;
    }
    public static double sumDoubleArray(double[] numbers) {
        double temp = 0;
        for(double number : numbers) {
            temp += number;
        }
        return temp;
    }
    public static double[] sumTwoArrays(double[] array1, double[] array2) {
        int arrSmallest = array2.length;
        if(array1.length < array2.length) {
            arrSmallest = array1.length;
        }
        double[] arr = new double[arrSmallest];
        for(int i = 0; i < arrSmallest; ++i) {
            arr[i] = array1[i]+array2[i];
        }
        return arr;
    }
    public static int[] doubleArrToInt(double[] arr) {
        int[] temp = new int[arr.length];
        for(int i = 0; i < arr.length; ++i){
            temp[i] = (int) arr[i];
        }
        return temp;
    }
}
