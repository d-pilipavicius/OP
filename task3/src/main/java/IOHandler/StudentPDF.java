package IOHandler;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.*;

public class StudentPDF extends PeoplePDF {
    private String pdfName;
    public StudentPDF(String pdfName) throws IOException, DocumentException {
        super(pdfName);
        this.pdfName = pdfName;
    }
    @Override
    public void addPage(Object data) {
        String[][] forTable = (String[][]) data;
        PdfPTable table = new PdfPTable(3);
        table.addCell("Name");
        table.addCell("Group");
        table.addCell("Date");
        for(int i = 0; i < forTable.length; ++i) {
            if(forTable[i].length > 3)
                System.out.println("WARNING! Row "+(i+1)+" has more than 3 elements inside. Every exceeding element will not be added to the table.");
            for(int j = 0; j < 3; ++j) {
                table.addCell(forTable[i][j]);
            }
        }
        try {
            Paragraph paragraph = new Paragraph("STUDENT ATTENDANCE", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingAfter(10);
            addElementToPdf(paragraph);
            addElementToPdf(table);
        } catch(DocumentException exception) {
            System.out.println("ERROR WHILE INSERTING THE TABLE INTO\""+pdfName+"\".");
        }
    }
}
