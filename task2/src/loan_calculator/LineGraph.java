package loan_calculator;

import number_work.*;
import java.awt.*;

public class LineGraph {
    private int x, y;
    private int width, height;
    private int[][] data;
    private Color[] graphColors;
    private final int TEXT_LENGTH = 10;
    private String xAxyName;
    private String yAxyName = "Cost";
    public LineGraph(int x, int y, int width, int height, int[][] data) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.data = data;
        graphColors = new Color[data.length];
        setColors();
    }
    protected void setColors() {
        for(int i = 0; i < graphColors.length; ++i) {
            int randColor = (int) Math.floor(Math.random()*2.99);
            int clr1 = (int) (Math.random()*255);
            int clr2 = (int) (Math.random()*255);
            switch(randColor) {
                case 0 -> graphColors[i] = new Color(255, clr1, clr2);
                case 1 -> graphColors[i] = new Color(clr1, 255, clr2);
                case 2 -> graphColors[i] = new Color(clr1, clr2, 255);
            }
        }
    }
    private static int biggestNumber(int[] stats) {
        int biggest = stats[0];
        for(int i = 1; i < stats.length; ++i) {
            if(stats[i] > biggest)
                biggest = stats[i];
        }
        return biggest;
    }
    private int maxBounds() {
        int[] temp = new int[data.length];
        int biggest;
        for(int i = 0; i < data.length; ++i){
            temp[i] = biggestNumber(data[i]);
        }
        biggest = biggestNumber(temp);
        biggest *= 1.2;
        int nrLenght = NumeralWork.getNumberLength(biggest);
        nrLenght = nrLenght-((int)(Math.floor(Math.sqrt(nrLenght))));
        nrLenght = (int) Math.pow(10, nrLenght);
        biggest = (biggest / nrLenght) * nrLenght;
        return biggest;
    }
    public void displayGraph(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int maxNumber = maxBounds();
        int numberLength = NumeralWork.getNumberLength(maxNumber);
        int leftOffset = (numberLength+2)*(TEXT_LENGTH/2);
        final int generalOffset = 30;
        g2.setFont(new Font("Consolas", Font.PLAIN, TEXT_LENGTH));

        //Main background
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.WHITE);
        g2.fillRect(1+x, 1+y, width, height);
        g2.setColor(Color.BLACK);
        g2.drawRect(1+x, 1+y, width, height);

        //Axis
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        xAxyName = ((data[0].length < 12) ? "Month" : "Year");
        for(int i = 1; i <= data[0].length; ++i) {
            if(i%12 == 0){
                int mult = ((i/12 < 10) ? 3 : 2);
                int xOffset = x+leftOffset+(width-leftOffset-generalOffset)*i/data[0].length;
                g2.drawLine(xOffset, y+height-generalOffset, xOffset, y+height-generalOffset-TEXT_LENGTH);
                g2.drawString(i/12+"", xOffset-TEXT_LENGTH/mult, y+height-generalOffset+TEXT_LENGTH);
            }
            if(data[0].length < 12) {
                int mult = ((i < 10) ? 3 : 2);
                int xOffset = x+leftOffset+(width-leftOffset-generalOffset)*i/data[0].length;
                g2.drawLine(xOffset, y+height-generalOffset, xOffset, y+height-generalOffset-TEXT_LENGTH);
                g2.drawString(i+"", xOffset-TEXT_LENGTH/mult, y+height-generalOffset+TEXT_LENGTH);
            }
        }
        g2.drawString(xAxyName, x+width-generalOffset+2, y+height-generalOffset);
        g2.drawString(yAxyName, x+generalOffset, y+generalOffset/2);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x+leftOffset, y+generalOffset, x+leftOffset ,y+height-generalOffset);
        g2.drawLine(x+leftOffset, y+height-generalOffset, x+width-generalOffset ,y+height-generalOffset);
        g2.setStroke(new BasicStroke(1));
        String tempS = "0";
        for(int j = 0; j < numberLength-1; ++j) {
            tempS = " "+tempS;
        }
        g2.drawString(tempS, x+TEXT_LENGTH/3, y+height-generalOffset+TEXT_LENGTH/3);
        for(int i = 1; i < 6; ++i){
            int yOffset = (height-generalOffset*2)*i/5;
            tempS = maxNumber*i/5+"";
            for(int j = 0; j < numberLength-tempS.length(); ++j){
                tempS = " "+tempS;
            }
            g2.drawLine(x+leftOffset, y+height-generalOffset-yOffset, x+width-generalOffset, y+height-generalOffset-yOffset);
            g2.drawString(tempS, x+TEXT_LENGTH/3, y+height-generalOffset-yOffset+TEXT_LENGTH/3);
        }

        g2.setStroke(new BasicStroke(2));
        for(int i = 0; i < data.length; ++i) {
            g2.setColor(graphColors[i]);
            for(int j = 0; j < data[i].length-1; ++j) {
                long x1 = x+leftOffset+((long)(width-leftOffset-generalOffset))*j/(data[i].length-1);
                long x2 = x+leftOffset+((long)(width-leftOffset-generalOffset))*(j+1)/(data[i].length-1);
                long y1 = y+height-generalOffset-((long)(height-generalOffset))*data[i][j]/maxNumber;
                long y2 = y+height-generalOffset-((long)(height-generalOffset))*data[i][j+1]/maxNumber;
                g2.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
            }
        }
    }
}
