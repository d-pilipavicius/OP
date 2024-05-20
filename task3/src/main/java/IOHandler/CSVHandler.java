package IOHandler;

import DataStructures.DataHolder;
import DataStructures.Student;
import com.itextpdf.text.Paragraph;

import java.io.*;
import java.time.Year;
import java.util.*;

public class CSVHandler {
    private BufferedWriter writer;
    private BufferedReader reader;
    private String fileName;
    public CSVHandler(String fileName, boolean readFile) {
        this.fileName = fileName;
        if(readFile) {
            try {
                reader = new BufferedReader(new FileReader(fileName));
            } catch(IOException exIO) {
                exIO.printStackTrace();
                System.exit(1);
            }
        }
        else {
            try {
                writer = new BufferedWriter(new FileWriter(fileName));
            } catch(IOException exIO) {
                exIO.printStackTrace();
                System.exit(1);
            }
        }
    }
    public String[][] readWholeFile() {
        if(reader == null) {
            return null;
        }
        String[][] output;
        List<String> tempList = new ArrayList<>();
        try {
            String temp = reader.readLine();
            while(temp != null) {
                tempList.add(temp);
                temp = reader.readLine();
            }
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        output = new String[tempList.size()][];
        for(int i = 0; i < tempList.size(); ++i) {
            String temp = tempList.get(i);
            int cellCount = 1;
            for(int j = 0; j < temp.length(); ++j) {
                if(temp.charAt(j) == ';') {
                    ++cellCount;
                }
            }
            output[i] = new String[cellCount];
            int textSaved = 0;
            int afterSemicolon = 0;
            for(int j = 0; j < temp.length(); ++j) {
                if(temp.charAt(j) == ';') {
                    output[i][textSaved] = temp.substring(afterSemicolon, j);
                    afterSemicolon = j + 1;
                    ++textSaved;
                }
            }
            if(afterSemicolon == temp.length()) {
                output[i][textSaved] = "";
            }
            else {
                output[i][textSaved] = temp.substring(afterSemicolon);
            }
        }
        return output;
    }
    public void writeFile(String[][] data) {
        if(writer == null)
            return;
        try {
            for(int i = 0; i < data.length; ++i) {
                if(data[i].length == 0) {
                    writer.write("\n");
                }
                for(int j = 0; j < data[i].length; j++) {
                    writer.write(data[i][j]);
                    if(j == data[i].length-1) {
                        writer.write("\n");
                    }
                    else {
                        writer.write(";");
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            try {
                close();
            } catch(Exception exc) {
            }
            System.exit(1);
        }
    }
    public void writeOutData() {
        if(writer == null)
            return;
        DataHolder holder = DataHolder.getGlobalHolder();
        Student[] students = holder.getStudents();
        String[][] dataToLoad = new String[students.length+1][];
        dataToLoad[0] = holder.getGroupNames();
        for(int i = 0; i < students.length; ++i) {
            dataToLoad[i+1] = new String[students[i].getDayCount()+2];
            dataToLoad[i+1][0] = students[i].getName();
            dataToLoad[i+1][1] = students[i].getGroup();
            for(int j = 0; j < students[i].getDayCount(); ++j) {
                dataToLoad[i+1][j+2] = students[i].getDay(j);
            }
        }
        writeFile(dataToLoad);
        try {
            close();
        } catch(IOException ex) {
            System.out.println("Unable to create file "+fileName+".");
        }
    }
    public void loadData() {
        if(reader == null)
            return;
        DataHolder.resetGlobal();
        DataHolder holder = DataHolder.getGlobalHolder();
        String[][] data = readWholeFile();
        for(int i = 0; i < data[0].length; ++i) {
            holder.addGroup(data[0][i]);
        }
        for(int i = 1; i < data.length; ++i) {
            Student student = new Student(data[i][0], data[i][1]);
            for(int j = 2; j < data[i].length; ++j) {
                try {
                    student.includeDay(data[i][j]);
                } catch(Exception ex) {
                    System.out.println("Unable to import data from "+fileName+".");
                    DataHolder.resetGlobal();
                    try {
                        close();
                    } catch(Exception exc) {
                        System.out.println("Unable to close the opened file.");
                    }
                    return;
                }
            }
            holder.addStudent(student);
        }
        try {
            close();
        } catch(IOException ex) {
            System.out.println("Unable to load data from file "+fileName+".");
            DataHolder.resetGlobal();
        }
    }
    public void close() throws IOException{
        if(writer == null) {
            reader.close();
        } else {
            writer.flush();
            writer.close();
        }
    }

}
