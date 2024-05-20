package loan_calculator;

import number_work.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
public class DataStorage {
    private static final int MONTHS_IN_YEAR = 12;
    private double startingPrice, interest;
    private int nrOfMonths;
    private boolean linearMethod = true;
    private double[] principals, interests, remainings;
    private double[] totalPayed;
    private int delayAfter, delayPeriod;
    private double delayInterest;
    public DataStorage(double startingPrice, int nrOfMonths, double interest, boolean linearMethod) {
        this.startingPrice = startingPrice;
        this.nrOfMonths = nrOfMonths;
        this.interest = interest;
        this.linearMethod = linearMethod;
        principals = new double[nrOfMonths];
        interests = new double[nrOfMonths];
        remainings = new double[nrOfMonths];
        if(linearMethod)
            calculateLinear(false);
        else
            calculateAnnuity(false);
        totalPayed = new double[nrOfMonths];
        totalPayed[0] = principals[0]+interests[0];
        for(int i = 1; i < nrOfMonths; ++i){
            totalPayed[i] = principals[i]+interests[i]+totalPayed[i-1];
        }
    }
    public DataStorage(double startingPrice, int nrOfMonths, double interest, int delayAfter, int delayPeriod, double delayInterest, boolean linearMethod) {
        this.startingPrice = startingPrice;
        this.nrOfMonths = nrOfMonths;
        this.interest = interest;
        this.linearMethod = linearMethod;
        this.delayAfter = delayAfter;
        this.delayPeriod = delayPeriod;
        this.delayInterest = delayInterest;
        principals = new double[nrOfMonths];
        interests = new double[nrOfMonths];
        remainings = new double[nrOfMonths];
        if(linearMethod)
            calculateLinear(true);
        else
            calculateAnnuity(true);
        totalPayed = new double[nrOfMonths];
        totalPayed[0] = principals[0]+interests[0];
        for(int i = 1; i < nrOfMonths; ++i) {
            totalPayed[i] = principals[i] + interests[i] + totalPayed[i - 1];
        }
    }
    private void calculateLinear(boolean withDelay) {
        double monthlyInterest = interest/MONTHS_IN_YEAR/100;
        double payment = startingPrice*monthlyInterest/(1-Math.pow(1/(1+monthlyInterest), nrOfMonths));
        double remaining = startingPrice;
        int delay = 0;

        if(withDelay) {
            delay = nrOfMonths-delayAfter;
        }

        for(int i = 0; i < nrOfMonths-delay; ++i) {
            interests[i] = remaining*monthlyInterest;
            principals[i] = payment-interests[i];
            remaining -= principals[i];
            if (remaining < 0)
                remaining = 0;
            remainings[i] = remaining;
        }

        if(withDelay) {
            for(int i = nrOfMonths-delay; i < nrOfMonths-delay+delayPeriod; ++i) {
                principals[i] = 0;
                interests[i] = 0;
                remainings[i] = remaining;
            }
            monthlyInterest = delayInterest/MONTHS_IN_YEAR/100;
            payment = remaining*monthlyInterest/(1-Math.pow(1/(1+monthlyInterest), delay-delayPeriod));
            for(int i = nrOfMonths-delay+delayPeriod; i < nrOfMonths; ++i) {
                interests[i] = remaining*monthlyInterest;
                principals[i] = payment-interests[i];
                remaining -= principals[i];
                if (remaining < 0)
                    remaining = 0;
                remainings[i] = remaining;
            }
        }
    }
    private void calculateAnnuity(boolean withDelay) {
        double monthlyInterest = interest/MONTHS_IN_YEAR/100;
        double principle = startingPrice/nrOfMonths;
        double remaining = startingPrice;
        int delay = 0;

        if(withDelay) {
            delay = nrOfMonths-delayAfter;
        }

        for(int i = 0; i < nrOfMonths-delay; ++i) {
            interests[i] = remaining*monthlyInterest;
            principals[i] = principle;
            remaining -= principals[i];
            if (remaining < 0)
                remaining = 0;
            remainings[i] = remaining;
        }

        if(withDelay) {
            for(int i = nrOfMonths-delay; i < nrOfMonths-delay+delayPeriod; ++i) {
                principals[i] = 0;
                interests[i] = 0;
                remainings[i] = remaining;
            }
            monthlyInterest = delayInterest/MONTHS_IN_YEAR/100;
            principle = remaining/(delay-delayPeriod);
            for(int i = nrOfMonths-delay+delayPeriod; i < nrOfMonths; ++i) {
                interests[i] = remaining*monthlyInterest;
                principals[i] = principle;
                remaining -= principals[i];
                if (remaining < 0)
                    remaining = 0;
                remainings[i] = remaining;
            }
        }
    }
    public int getNrOfMonths() {
        return nrOfMonths;
    }
    public double getStartingPrice() {
        return startingPrice;
    }
    public boolean isLinearMethod() {
        return linearMethod;
    }
    public double[] getPrincipals() {
        return principals.clone();
    }
    public double[] getInterests() {
        return interests.clone();
    }
    public double[] getRemainings() {
        return remainings.clone();
    }
    public double[] getTotalPayed() {
        return totalPayed.clone();
    }
    public void getReport(String fileName) {
        try {
            File f = new File(fileName);
            FileWriter fw = new FileWriter(f);
            fw.write("Month | Principal            | Interest             | Total for Month      | Remaining            | Total payed\n");
            for(int i = 0; i < nrOfMonths; ++i){
                int temp = NumeralWork.getNumberLength(i+1);
                String stemp;
                fw.write((i+1)+"");
                for(int j = 0; j < 5-temp; ++j){
                    fw.write(" ");
                }
                fw.write(" | ");
                stemp = String.format(Locale.US, "%.2f", principals[i]);
                temp = stemp.length();
                fw.write(stemp);
                for(int j = 0; j < 20-temp; ++j){
                    fw.write(" ");
                }
                fw.write(" | ");
                stemp = String.format(Locale.US, "%.2f", interests[i]);
                temp = stemp.length();
                fw.write(stemp);
                for(int j = 0; j < 20-temp; ++j){
                    fw.write(" ");
                }
                fw.write(" | ");
                stemp = String.format(Locale.US, "%.2f", principals[i]+interests[i]);
                temp = stemp.length();
                fw.write(stemp);
                for(int j = 0; j < 20-temp; ++j){
                    fw.write(" ");
                }
                fw.write(" | ");
                stemp = String.format(Locale.US, "%.2f", remainings[i]);
		temp = stemp.length();
                fw.write(stemp);
		for(int j = 0; j < 20-temp; ++j){
                    fw.write(" ");
                }
		fw.write(" | ");
		stemp = String.format(Locale.US, "%.2f", totalPayed[i])+"\n";
		temp = stemp.length();
		fw.write(stemp);
            }
            fw.close();
        }
        catch(Exception e) {
            System.out.println("ERROR WHILE OPENING FILE\""+fileName+"\".");
            System.exit(-1);
        }
    }
}
