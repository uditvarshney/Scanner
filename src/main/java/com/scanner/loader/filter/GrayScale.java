package com.scanner.loader.filter;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class GrayScale {
    public final static GrayScale GRAY_SCALE = new GrayScale();

    private GrayScale() {}

    public Mat apply(Mat image)
    {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        return grayImage;
    }
}
