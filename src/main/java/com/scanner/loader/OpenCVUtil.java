package com.scanner.loader;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OpenCVUtil {

    Logger logger = LoggerFactory.getLogger(OpenCVUtil.class);
    public OpenCVUtil()
    {
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        OpenCV.loadLocally();
    }

    public Mat loadImage(String path)
    {
        logger.info("Loading Image");
        Imgcodecs imgcodecs = new Imgcodecs();
        return imgcodecs.imread(path);
    }

    public void saveImage(Mat imageMatrix, String targetPath)
    {
        Imgcodecs imgcodecs = new Imgcodecs();
        imgcodecs.imwrite(targetPath, imageMatrix);
    }
}
