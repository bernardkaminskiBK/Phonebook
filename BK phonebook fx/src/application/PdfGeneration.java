package application;


import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.ObservableList;

public class PdfGeneration {

	public void pdfGeneration(String fileName, ObservableList<Person> data) {
		Document document = new Document();

		try {
			// Firemne logo
			PdfWriter.getInstance(document, new FileOutputStream(fileName + ".pdf"));
			document.open();
			Image image1 = Image.getInstance(getClass().getResource("/pics/phonebook.png"));
			image1.scaleToFit(200, 86);
			image1.setAbsolutePosition(37f, 750f);
			document.add(image1);

			// println
			document.add(new Paragraph("\n\n\n\n\n\n"));

			// Tabulka
			float[] columnWidths = { 1, 3, 3, 4 };
			PdfPTable table = new PdfPTable(columnWidths);
			table.setWidthPercentage(100);
			PdfPCell cell = new PdfPCell(new Phrase("Zoznam kontaktov"));
			cell.setBackgroundColor(GrayColor.GRAYWHITE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(4);
			table.addCell(cell);

			table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell("Pocet");
			table.addCell("Meno");
			table.addCell("Priezvisko");
			table.addCell("E-mail adresa");
			table.setHeaderRows(1);

			table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			for (int i = 1; i <= data.size(); i++) {
				Person actualPerson = data.get(i - 1);
				table.addCell("" + i);
				table.addCell(actualPerson.getFirstName());
				table.addCell(actualPerson.getLastName());
				table.addCell(actualPerson.getEmail());
			}
			document.add(table);

			// Podpis
			Chunk signature = new Chunk("\n\n Generované pomocou aplikácie Phonebook.");
			Paragraph base = new Paragraph(signature);
			document.add(base);
		} catch (Exception e) {
			e.printStackTrace();
		}
		document.close();
	}
}
