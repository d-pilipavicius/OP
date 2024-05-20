package IOHandler;

import DataStructures.DataHolder;
import DataStructures.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
public class ExcelHandler {
    private boolean readFile;
    private String fileName;
    private Workbook workbook;
    public ExcelHandler(String fileName, boolean readFile) {
        this.fileName = fileName;
        this.readFile = readFile;
        if(readFile) {
            try {
                FileInputStream input = new FileInputStream(fileName);
                workbook = new XSSFWorkbook(input);
            } catch(Exception ex) {
                System.out.println("Unable to open file by the name "+fileName+".");
            }
        }
        else {
            try {
                workbook = new XSSFWorkbook();
            } catch(Exception ex) {
                System.out.println("Unable to create a file by the name "+fileName+".");
            }
        }
    }
    public void inputData() {
        if(!readFile)
            return;
        DataHolder.resetGlobal();
        DataHolder holder = DataHolder.getGlobalHolder();
        Sheet sheet = workbook.getSheetAt(0);
        Row tempRow = sheet.getRow(0);
        for(Cell cell : tempRow) {
            holder.addGroup(cell.getStringCellValue());
        }
        for(int i = 1; i <= sheet.getLastRowNum(); ++i) {
            tempRow = sheet.getRow(i);
            Student student = new Student(tempRow.getCell(0).getStringCellValue(), tempRow.getCell(1).getStringCellValue());
            for(int j = 2; j < tempRow.getLastCellNum(); ++j) {
                Cell cell = tempRow.getCell(j);
                try {
                    String cellValue = cell.getStringCellValue().trim();
                    student.includeDay(cellValue);
                } catch(Exception ex) {
                    DataHolder.resetGlobal();
                    System.out.println("Failed at "+i+"-"+j+"  Number "+tempRow.getLastCellNum());
                    System.out.println("The file has bad data presented inside.");
                    return;
                }
            }
            holder.addStudent(student);
        }
        try {
            workbook.close();
        } catch(Exception ex) {
            System.out.println("Error while reading "+fileName+".");
            DataHolder.resetGlobal();
        }
    }
    public void writeOutData() {
        if(readFile)
            return;
        DataHolder holder = DataHolder.getGlobalHolder();
        String[] groups = holder.getGroupNames();
        Student[] students = holder.getStudents();
        String[][] studentData = new String[students.length][];
        for(int i = 0; i < students.length; ++i) {
            studentData[i] = new String[students[i].getDayCount()+2];
            studentData[i][0] = students[i].getName();
            studentData[i][1] = students[i].getGroup();
            for(int j = 0; j < students[i].getDayCount(); ++j) {
                studentData[i][j+2] = students[i].getDay(j);
            }
        }
        Sheet sheet = workbook.createSheet("Students' data");
        Row groupRow = sheet.createRow(0);
        for(int i = 0; i < groups.length; ++i) {
            Cell cell = groupRow.createCell(i);
            cell.setCellValue(groups[i]);
        }

        for(int i = 0; i < studentData.length; ++i) {
            Row dataRow = sheet.createRow(i+1);
            for(int j = 0; j < studentData[i].length; ++j) {
                Cell cell = dataRow.createCell(j);
                cell.setCellValue(studentData[i][j]);
            }
        }

        try {
            FileOutputStream writer = new FileOutputStream(fileName);
            workbook.write(writer);
            workbook.close();
            writer.close();
        } catch(Exception ex) {
            System.out.println("Error while generating "+fileName+".");
        }
    }
}
