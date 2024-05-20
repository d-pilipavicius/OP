package Windows;

import DataStructures.*;
import IOHandler.StudentPDF;
import Table.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
public class AttendanceWindow {
    public static void loadAttendance() {
        DisplaySingleton window = DisplaySingleton.getInstance();
        DataHolder holder = DataHolder.getGlobalHolder();
        JPanel panel = window.getCanvas();
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.CENTER));
        leftSide.setPreferredSize(new Dimension(300, 500));
        JScrollPane rightSide = new JScrollPane();
        rightSide.setPreferredSize(new Dimension(450, 450));

        //RIGHT SIDE IMPLEMENTATION
        Table table = new Table();
        table.resetValues();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        Comparator<String> stringComparator = Comparator.naturalOrder();

        for(int i = 0; i < table.getColumnCount(); ++i)
            sorter.setComparator(i, stringComparator);

        rightSide.setViewportView(table);
        //RIGHT SIDE IMPLEMENTATION END

        //LEFT SIDE IMPLEMENTATION
        //Add, remove student box
        JPanel studentChoice = new JPanel();
        JLabel label0 = new JLabel("Choose student");
        JComboBox<String> studentBox = new JComboBox<>();
        DateField date1 = new DateField();
        JButton submit = new JButton("Add attendance");

        studentBox.setPreferredSize(new Dimension(100, 30));
        studentChoice.add(label0); studentChoice.add(studentBox);
        studentBox.addItem("-");
        String[] names = holder.getStudentNames();
        for(int i = 0; i < names.length; ++i) {
            studentBox.addItem(names[i]);
        }
        submit.addActionListener(e -> {
            if(studentBox.getSelectedIndex() > 0 && studentBox.getItemCount() > 1) {
                try {
                    int year = date1.getYear();
                    int month = date1.getMonth();
                    int day = date1.getDay();
                    holder.getStudentByName((String) studentBox.getSelectedItem()).includeDay(year, month, day);
                    table.resetValues();
                } catch(NumberFormatException ex) {
                    System.out.println("Date entered incorrectly.");
                } catch(ParseException ex) {
                    System.out.println("Entered date unavailable.");
                }
            }
            else {
                System.out.println("No student selected.");
            }
        });

        JButton removeAtt = new JButton("Remove attendance");
        removeAtt.addActionListener(e -> {
            if(studentBox.getSelectedIndex() == 0 || studentBox.getItemCount() <= 1)
                return;
            Student student = holder.getStudentByName((String) studentBox.getSelectedItem());
            String[] daysString = new String[student.getDayCount()];
            for(int i = 0; i < student.getDayCount(); ++i) {
                daysString[i] = student.getDay(i);
            }

            //Opens removal window
            MultipleChoice removeDays = new MultipleChoice(daysString, "Day removal", "Choose the days you wish to remove for "+studentBox.getSelectedItem()) {
                @Override
                public void submittedRun() {
                    List<String> list = getSelectedData();
                    while(list.size() > 0) {
                        String temp = list.remove(list.size() - 1);
                        int yearT = Integer.parseInt(temp.substring(0, 4));
                        int monthT = Integer.parseInt(temp.substring(5, 7));
                        int dayT = Integer.parseInt(temp.substring(8, 10));
                        int dayID = student.locateDay(yearT, monthT, dayT);
                        if(dayID >= 0)
                            student.removeDay(dayID);
                    }
                    table.resetValues();
                }
            };
        });
        //Add, remove student box END

        //Day interval box
        JPanel fromToPanel = new JPanel();
        JLabel infoLabel1 = new JLabel("Set day interval from - to ");
        JButton sortByInterval = new JButton("Set interval");
        DateField from = new DateField();
        DateField to = new DateField();

        infoLabel1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        sortByInterval.setAlignmentX(JButton.CENTER_ALIGNMENT);
        fromToPanel.setLayout(new BoxLayout(fromToPanel, BoxLayout.Y_AXIS));
        fromToPanel.add(infoLabel1);
        fromToPanel.add(from); fromToPanel.add(to);
        fromToPanel.add(sortByInterval);

        sortByInterval.addActionListener(e -> {
            if(from.isEmpty() && to.isEmpty()) {
                table.resetValues();
            }
            try {
                int y1 = from.getYear();
                int y2 = to.getYear();
                int m1 = from.getMonth();
                int m2 = to.getMonth();
                int d1 = from.getDay();
                int d2 = to.getDay();
                if(LocalDate.of(y1, m1, d1).isAfter(LocalDate.of(y2, m2, d2)))
                    throw new Exception();
                Student[] students = holder.getStudents();
                DataHolder tempHolder = new DataHolder();

                for(int i = 0; i < students.length; ++i) {
                    Student student = new Student(students[i].getName(), students[i].getGroup());
                    for(int j = 0; j < students[i].getDayCount(); ++j) {
                        String date = students[i].getDay(j);
                        int year = Integer.parseInt(date.substring(0, 4));
                        int month = Integer.parseInt(date.substring(5, 7));
                        int day = Integer.parseInt(date.substring(8, 10));
                        if(isDateInRange(LocalDate.of(year, month, day), LocalDate.of(y1, m1, d1), LocalDate.of(y2, m2, d2)))
                            student.includeDay(date);
                    }
                    if(student.getDayCount() > 0)
                        tempHolder.addStudent(student);
                }

                table.getValuesFromHolder(tempHolder);
            } catch(Exception ex) {
                if(!(from.isEmpty() && to.isEmpty()))
                    System.out.println("Bad date arguments entered.");
            }
        });
        //Day interval box END

        //PDF save box
        JPanel pdfPanel = new JPanel();
        JTextField pdfName = new JTextField();
        pdfName.setPreferredSize(new Dimension(100, 30));
        pdfPanel.setPreferredSize(new Dimension(200, 40));
        JLabel pdf = new JLabel(".pdf");
        pdfPanel.add(pdfName);
        pdfPanel.add(pdf);
        JButton saveToPdf = new JButton("Save table to PDF");
        saveToPdf.addActionListener(e -> {
            try {
                StudentPDF pdfWriter = new StudentPDF(pdfName.getText().trim()+".pdf");
                String[][] data = new String[table.getRowCount()][];
                for(int i = 0; i < data.length; ++i) {
                    data[i] = table.getRowData(i);
                }
                pdfWriter.addPage(data);
                pdfWriter.closePDF();
            } catch(Exception ex) {
                System.out.println("Could not save table to "+pdfName.getText().trim()+".pdf");
            }
        });
        //PDF save box END

        //Left panel arrangement
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JLabel label1 = new JLabel("Add attendance for student");
        JLabel label2 = new JLabel("Remove attendance for student");

        label1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        submit.setAlignmentX(JButton.CENTER_ALIGNMENT);
        removeAtt.setAlignmentX(JButton.CENTER_ALIGNMENT);
        saveToPdf.setAlignmentX(JButton.CENTER_ALIGNMENT);

        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

        panel1.setBorder(new LineBorder(Color.BLACK, 1));
        fromToPanel.setBorder(new LineBorder(Color.BLACK, 1));
        panel2.setBorder(new LineBorder(Color.BLACK, 1));

        panel1.add(studentChoice); panel1.add(label1); panel1.add(date1);
        panel1.add(submit); panel1.add(label2); panel1.add(removeAtt);
        panel2.add(pdfPanel);
        panel2.add(saveToPdf);

        leftSide.add(panel1);
        leftSide.add(fromToPanel);
        leftSide.add(panel2);
        //Left panel arrangement END
        //LEFT SIDE IMPLEMENTATION END


        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        panel4.add(leftSide); panel4.add(rightSide);
        panel.add(TitleWindowSetup.backButton());
        panel.add(panel4);
        window.setSize(800, 500);
    }

    public static boolean isDateInRange(LocalDate date, LocalDate from, LocalDate to) {
        return (date.isAfter(from) && date.isBefore(to) || date.isEqual(from) || date.isEqual(to));
    }

    private static class DateField extends JPanel {
        private JTextField y;
        private JTextField m;
        private JTextField d;
        public DateField() {
            y = new JTextField();
            m = new JTextField();
            d = new JTextField();
            y.setPreferredSize(new Dimension(50, 30));
            m.setPreferredSize(new Dimension(30, 30));
            d.setPreferredSize(new Dimension(30, 30));
            add(new JLabel("Year: "));
            add(y);
            add(new JLabel("Month: "));
            add(m);
            add(new JLabel("Day: "));
            add(d);
        }
        public boolean isEmpty() {
            return y.getText().trim().isEmpty() &&
                    m.getText().trim().isEmpty() &&
                    d.getText().trim().isEmpty();
        }
        public int getYear() throws NumberFormatException {
            return Integer.parseInt(y.getText().trim());
        }
        public int getMonth() throws NumberFormatException {
            return Integer.parseInt(m.getText().trim());
        }
        public int getDay() throws NumberFormatException {
            return Integer.parseInt(d.getText().trim());
        }
    }
}
