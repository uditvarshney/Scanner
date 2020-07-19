package com.scanner.loader.filter;

import org.opencv.core.Mat;

import static org.opencv.core.CvType.CV_8UC1;

public class AutoTone {

    public static final AutoTone AUTO_TONE = new AutoTone();

    private AutoTone() {}

    //TODO: need to add more auto tone
    public Mat apply(Mat image)
    {
        Mat filteredImage = new Mat();
        image.convertTo(filteredImage, CV_8UC1, 1, 40);
        return filteredImage;
    }
}
