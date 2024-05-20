package loan_windows;

import javax.swing.*;
import java.awt.*;

import loan_calculator.DataStorage;
import swing_changes.*;

public class TitlePage extends JPanel {
    private TitlePage(JFrame frame) {
        super();
        setBackground(new Color(2, 99, 152));
        setLayout(null);

        ImprovedLabel title = new ImprovedLabel(200, 10,"MORTGAGE CALCULATOR", "Product Sans", Font.BOLD, 30);

        ImprovedLabel loan = new ImprovedLabel(50, 100, "Loan amount:", "Product Sans", Font.BOLD, 20);
        ImprovedTextField amountField = new ImprovedTextField(185, 103, 100, 20);

        ImprovedLabel perc = new ImprovedLabel(50, 150, "Interest rate:", "Product Sans", Font.BOLD,20);
        ImprovedTextField percentageField = new ImprovedTextField(175, 153, 40, 20);

        ImprovedLabel term = new ImprovedLabel(50, 200, "Term:", "Product Sans", Font.BOLD, 20);
        ImprovedTextField yearField = new ImprovedTextField(115, 203, 40, 20, "year");
        ImprovedTextField monthField = new ImprovedTextField(165, 203, 40, 20, "month");

        ImprovedLabel method = new ImprovedLabel(50, 250, "Calculation method:", "Product Sans", Font.BOLD, 20);
        ImprovedCheckBox lineCheck = new ImprovedCheckBox(245, 253, 20, 20);
        ImprovedLabel lCheckText = new ImprovedLabel(270, 258, "Linear", 10);
        ImprovedCheckBox annuityCheck = new ImprovedCheckBox(305, 253, 20, 20);
        ImprovedLabel aCheckText = new ImprovedLabel(330, 258, "Annuity", 10);

        ImprovedLabel delay = new ImprovedLabel(50, 300, "Mortgage suspension:","Product Sans", Font.BOLD, 20);
        ImprovedCheckBox suspCheck = new ImprovedCheckBox(265, 303, 20, 20);

        ImprovedLabel delayAfter = new ImprovedLabel(50, 350, "Suspend after:", "Product Sans", Font.BOLD, 20);
        ImprovedTextField suspYearField = new ImprovedTextField(200, 353, 40, 20, "year");
        ImprovedTextField suspMonthField = new ImprovedTextField(250, 353, 40, 20, "month");

        ImprovedLabel delayTerm = new ImprovedLabel(50, 400, "Suspend for:", "Product Sans", Font.BOLD, 20);
        ImprovedTextField delYearField = new ImprovedTextField(180, 403, 40, 20, "year");
        ImprovedTextField delMonthField = new ImprovedTextField(230, 403, 40, 20, "month");

        ImprovedLabel delayPerc = new ImprovedLabel(50, 450, "Interest rate after suspension:", "Product Sans", Font.BOLD, 20);
        ImprovedTextField delPercentageField = new ImprovedTextField(340, 453, 40, 20);

        JButton calcB = new JButton("Calculate");
        calcB.setSize(100, 40);
        calcB.setLocation(350, 350);
        calcB.addActionListener(e -> {
            try {
                double starting = Double.parseDouble(amountField.getText().trim());
                int years = ((yearField.isEmpty()) ? 0 : Integer.parseInt(yearField.getText().trim()));
                int months = ((monthField.isEmpty()) ? 0 : Integer.parseInt(monthField.getText().trim()));
                if(years + months == 0 || months > 11)
                    throw new NumberFormatException();
                double interest = Double.parseDouble(percentageField.getText().trim());
                if(!lineCheck.isSelected() && !annuityCheck.isSelected())
                    throw new NumberFormatException();
                boolean line = lineCheck.isSelected();
                DataStorage dataStorage;
                if(suspCheck.isSelected()) {
                    int sYear = ((suspYearField.isEmpty()) ? 0 : Integer.parseInt(suspYearField.getText().trim()));
                    int sMonth = ((suspMonthField.isEmpty()) ? 0 : Integer.parseInt(suspMonthField.getText().trim()));
                    if(suspYearField.isEmpty() && suspMonthField.isEmpty() || sMonth > 11)
                        throw new NumberFormatException();
                    int pYear = ((delYearField.isEmpty()) ? 0 : Integer.parseInt(delYearField.getText().trim()));
                    int pMonth = ((delMonthField.isEmpty()) ? 0 : Integer.parseInt(delMonthField.getText().trim()));
                    if(delYearField.isEmpty() && delMonthField.isEmpty() || pMonth > 11)
                        throw new NumberFormatException();
                    double sInterest = Double.parseDouble(delPercentageField.getText().trim());
                    dataStorage = new DataStorage(starting, years*12+months, interest, sYear*12+sMonth, pYear*12+pMonth, sInterest, line);
                }
                else {
                    dataStorage = new DataStorage(starting, years*12+months, interest, line);
                }
                DataPage.openData(dataStorage);
                double[] monthNrs = new double[dataStorage.getNrOfMonths()];
                for(int i = 0; i < dataStorage.getNrOfMonths(); ++i) {
                    monthNrs[i] = i+1;
                }
                repaint();
            }
            catch(NumberFormatException ex) {
                System.out.println("SOME FIELDS WERE NOT FILLED/THE DATA WAS ENTERED INCORRECTLY.");
            }
        });

        lineCheck.addActionListener(e -> {
            repaint();
            annuityCheck.setSelected(false);
        });

        annuityCheck.addActionListener(e -> {
            repaint();
            lineCheck.setSelected(false);
        });

        suspCheck.addActionListener(e -> {
            if(!suspCheck.isSelected()) {
                repaint();
                frame.setSize(800, 450);
                calcB.setLocation(350, 350);
                remove(delayTerm);
                remove(delMonthField);
                remove(delYearField);

                remove(delayAfter);
                remove(suspYearField);
                remove(suspMonthField);

                remove(delayPerc);
                remove(delPercentageField);
            }
            else {
                repaint();
                frame.setSize(800, 600);
                calcB.setLocation(350, 500);
                add(delayTerm);
                add(delYearField);
                add(delMonthField);

                add(delayAfter);
                add(suspYearField);
                add(suspMonthField);

                add(delayPerc);
                add(delPercentageField);
            }
        });

        add(title);
        add(calcB);

        add(loan);
        add(amountField);

        add(perc);
        add(percentageField);

        add(term);
        add(yearField);
        add(monthField);

        add(method);
        add(lineCheck);
        add(lCheckText);
        add(annuityCheck);
        add(aCheckText);

        add(delay);
        add(suspCheck);
    }

    public static void main(String args[]){
        JFrame f = new JFrame("Calculator");
        f.setSize(800, 450);
        f.add(new TitlePage(f));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }
}
