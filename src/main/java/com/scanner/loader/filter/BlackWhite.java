package com.scanner.loader.filter;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class BlackWhite {

    public static final BlackWhite B_W = new BlackWhite();

    private BlackWhite() {}

    public Mat apply(Mat image)
    {
        Mat black_white = new Mat();
        Imgproc.adaptiveThreshold(image, black_white, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 75 , 22);
        return black_white;
    }
}
