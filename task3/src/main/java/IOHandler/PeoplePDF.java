package IOHandler;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;

public abstract class PeoplePDF {
    private Document document;
    private PdfWriter pdf;
    public PeoplePDF(String fileName) throws IOException, DocumentException {
        document = new Document(PageSize.A4);
        pdf = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
    }
    public void addElementToPdf(Element element) throws DocumentException{
        document.add(element);
    }
    public void closePDF() {
        document.close();
    }
    public abstract void addPage(Object data);
}
