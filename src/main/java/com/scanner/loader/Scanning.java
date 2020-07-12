package com.scanner.loader;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class Scanning {
    private final String path = "D:/Scanner/5sem.jpg";
    private final String path1 = "D:/Scanner/adhar1.jpg";
    private final String dest = "D:/Scanner/5sem.pdf";

    public static void main(String[] args)
    {
        try {
            new Scanning().generatePDF();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void generatePDF() throws IOException {

        PdfWriter pdfWriter = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(pdfWriter);

        Document document = new Document(pdf);
        ImageData data = ImageDataFactory.create(path);
        ImageData data1 = ImageDataFactory.create(path1);
        Image image = new Image(data);
        Image image1 = new Image(data1);
        document.add(image);
        document.add(image1);
        document.close();
    }


}
