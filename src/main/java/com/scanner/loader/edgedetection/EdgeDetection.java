package com.scanner.loader.edgedetection;

import com.scanner.loader.OpenCVUtil;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EdgeDetection extends OpenCVUtil {

    Logger logger = LoggerFactory.getLogger(EdgeDetection.class);

    private EdgeDetection()
    {
        super();
    }

    public static EdgeDetection getInstance()
    {
        return new EdgeDetection();
    }

    public Mat detectEdge(Mat image)
    {
        Mat orignalImage = new Mat();
        image.copyTo(orignalImage);
        logger.info("trying to detect edges");
        Mat gray = new Mat(image.rows(), image.cols(), image.type());
        Mat edges = new Mat(image.rows(), image.cols(), image.type());
        Mat greyImage = new Mat(image.rows(), image.cols(), image.type(), new Scalar(0));
        Mat contour = new Mat();
        Mat enhance = new Mat();
        Random rng = new Random(12345);
        //Photo.detailEnhance(image, enhance, 10, 0.15f);
        //enhance = applyGamma(image);
        //converting to gray scale
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // filtering out noise
        Imgproc.blur(gray, edges, new Size(3,3));
        //Imgproc.adaptiveThreshold(gray, edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 115 , 10);
        // edge detection
        //TODO need to find out proper threshold value
        Imgproc.Canny(edges, greyImage, 10, 10);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(greyImage, contours, contour, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        MatOfPoint x = new MatOfPoint();
        MatOfPoint2f[] contourPoly = new MatOfPoint2f[contours.size()];
        Rect[] rect = new Rect[contours.size()];
        double maxArea = 0;
        Rect maxContour = null;

        for (int i =0; i < contours.size(); i++)
        {
            contourPoly[i] = new MatOfPoint2f();
            //double area = Imgproc.contourArea(contours.get(i));
            double peri = Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true);
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contourPoly[i], 0.1 * peri, true);
            rect[i] = Imgproc.boundingRect(new MatOfPoint(contourPoly[i].toArray()));
            if (rect[i].area() > maxArea )
            {
                maxArea = rect[i].area();
                //maxContour = Imgproc.boundingRect(new MatOfPoint(contourPoly[i].toArray()));
                maxContour = rect[i];
            }
        }

        //Mat drawing = Mat.zeros(greyImage.size(), CvType.CV_8UC3);
        if (maxContour != null)
        {
            Imgproc.rectangle(orignalImage, maxContour.tl(), maxContour.br(), new Scalar(rng.nextInt(256),rng.nextInt(256),rng.nextInt(256)), 5);
        }
        return orignalImage;
    }

    public Mat detectEdgeConvex(Mat image)
    {
        Random rng = new Random(12345);
        Mat originalImage = new Mat();
        image.copyTo(originalImage);

        logger.info("Applying Convex Hull");
        Mat grayScale = new Mat();
        Mat threshold = new Mat();
        Mat threshold1 = new Mat();
        Imgproc.cvtColor(image, grayScale, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(grayScale, grayScale, new Size(5, 5));
        //TODO check canny parameters
        Mat cannyImage = applyCannyEdge(grayScale);
        Mat hull = applyConvex(cannyImage, rng, originalImage);
//        Imgproc.threshold(grayScale, threshold, 80,255, Imgproc.THRESH_BINARY);
//        Imgproc.adaptiveThreshold(threshold, threshold1, 255,1, 1, 11, 2);
        logger.info("returning processed image");
        return hull;
    }

    private Mat applyCannyEdge(Mat image)
    {
        logger.info("applying canny edge detection");
        Mat cannyOutput = new Mat();
        Imgproc.Canny(image, cannyOutput, 20, 10, 3, false);
        return cannyOutput;
    }

    private Mat applyConvex(Mat image, Random rng, Mat orignalImage)
    {
        logger.info("applying convex hull");
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        List<MatOfPoint> hullList = new ArrayList<>();
        List<MatOfPoint> maxContour = new ArrayList<>();
        double maxArea = 0;
        for (MatOfPoint contour : contours) {
            if (isContourSquare(contour))
            {
                maxContour.add(contour);
            }
//            double area = Imgproc.contourArea(contour);
//            MatOfInt hull = new MatOfInt();
//            Imgproc.convexHull(contour, hull);
//            double peri = Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true);
//            MatOfPoint2f approxCurve = new MatOfPoint2f();
//            Imgproc.approxPolyDP(new MatOfPoint2f(contour.toArray()), approxCurve, 0.1 * peri, true);
//            if (area > maxArea )
//            {
//                logger.info("Max Area: " + area);
//                MatOfPoint approxContour = new MatOfPoint();
//                approxCurve.convertTo(approxContour,CvType.CV_32FC2);
////                if (approxContour.size().height == 4)
////                {
//                    maxArea = area;
//                    maxContour.add(contour);
//
//                    Point[] contourArray = contour.toArray();
//                    Point[] hullPoints = new Point[hull.rows()];
//                    List<Integer> hullContourIdxList = hull.toList();
//                    for (int i = 0; i < hullContourIdxList.size(); i++) {
//                        hullPoints[i] = contourArray[hullContourIdxList.get(i)];
//                    }
//                    hullList.add(new MatOfPoint(hullPoints));
//                //}
//            }

        }

        logger.info("drawing contours: "+maxContour.size());
        Rect[] rect = new Rect[maxContour.size()];
        for (int i =0; i <maxContour.size(); i++)
        {
            rect[i] = Imgproc.boundingRect(maxContour.get(i));
            Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
            Imgproc.rectangle(orignalImage, rect[i].tl(), rect[i].br(), color, 4);
            //Imgproc.drawContours(orignalImage, maxContour, i, color, 5);
        }


        //Mat drawing = Mat.zeros(image.size(), CvType.CV_8UC3);
//        for (int i = 0; i < maxContour.size(); i++) {
//            logger.info("i: "+i);
//            Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
//            //Imgproc.drawContours(orignalImage, maxContour, i, color, 10);
//            Imgproc.drawContours(orignalImage, hullList, i, color, 10 );
//        }

        return orignalImage;
    }

    private static boolean isContourSquare(MatOfPoint thisContour) {

        Rect ret = null;

        MatOfPoint2f thisContour2f = new MatOfPoint2f();
        MatOfPoint approxContour = new MatOfPoint();
        MatOfPoint2f approxContour2f = new MatOfPoint2f();

        thisContour.convertTo(thisContour2f, CvType.CV_32FC2);

        Imgproc.approxPolyDP(thisContour2f, approxContour2f, 2, true);

        approxContour2f.convertTo(approxContour, CvType.CV_32S);

        if (approxContour.total() == 4) {
            ret = Imgproc.boundingRect(approxContour);
        }

        return (ret != null);
    }

    public Mat applyGamma(Mat image)
    {
        Mat newImage = Mat.zeros(image.size(), image.type());
        double alpha = 2.0; /*< Simple contrast control */
        int beta = 20;       /*< Simple brightness control */
        byte[] imageData = new byte[(int) (image.total()*image.channels())];
        image.get(0, 0, imageData);
        byte[] newImageData = new byte[(int) (newImage.total()*newImage.channels())];
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                for (int c = 0; c < image.channels(); c++) {
                    double pixelValue = imageData[(y * image.cols() + x) * image.channels() + c];
                    pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                    newImageData[(y * image.cols() + x) * image.channels() + c]
                            = saturate(alpha * pixelValue + beta);
                }
            }
        }
        newImage.put(0, 0, newImageData);
        return newImage;
    }

    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }
}
