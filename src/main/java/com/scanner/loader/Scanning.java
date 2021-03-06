package com.scanner.loader;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.scanner.loader.edgedetection.EdgeDetection;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import static com.scanner.loader.filter.AutoTone.AUTO_TONE;
import static com.scanner.loader.filter.BlackWhite.B_W;
import static com.scanner.loader.filter.GrayScale.GRAY_SCALE;

public class Scanning {
    private final String path = "D:/Scanner/6sem.jpg";
    private final String temp = "D:/Scanner/temp.jpg";
    private final String path1 = "D:/Scanner/Pancard.jpg";
    private final String path2 = "D:/Scanner/adhar1.jpg";
    private final String dest = "D:/Scanner/5sem.pdf";

    Logger logger = LoggerFactory.getLogger(Scanning.class);

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

        logger.info("Starting scanner");
        EdgeDetection edgeDetection = EdgeDetection.getInstance();
        Mat imageMatrix = edgeDetection.loadImage(path2);
        Mat computedImage = edgeDetection.detectEdge(imageMatrix);

        Mat filteredImage = applyFiler(computedImage, "autotone");
        edgeDetection.saveImage(filteredImage, temp);
        PdfWriter pdfWriter = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(pdfWriter);

        Document document = new Document(pdf);
        ImageData data = ImageDataFactory.create(temp);
        //ImageData data1 = ImageDataFactory.create(path1);
        Image image = new Image(data);
        //Image image1 = new Image(data1);
        document.add(image);
        //document.add(image1);
        document.close();
    }

    //TODO: Either implement slider for b&w
    private Mat applyFiler(Mat image, String filterName) {
        logger.info("applying filter: "+ filterName);
        switch (filterName)
        {
            case "gray":
                return GRAY_SCALE.apply(image);
            case "b&w":
                return B_W.apply(GRAY_SCALE.apply(image));
            case "autotone":
                return AUTO_TONE.apply(image);
            default:
                return image;
        }
    }


}
