package com.scanner.loader.edgedetection;

import com.scanner.loader.OpenCVUtil;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
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
        //converting to gray scale
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        // filtering out noise
        Imgproc.blur(gray, edges, new Size(3,3));
        // edge detection
        //TODO need to find out proper threshold value
        Imgproc.Canny(edges, greyImage, 10, 10);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(greyImage, contours, contour, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        MatOfPoint x = new MatOfPoint();
        MatOfPoint2f[] contourPoly = new MatOfPoint2f[contours.size()];
        Rect[] rect = new Rect[contours.size()];
        Random rng = new Random(12345);
        double maxArea = 0;
        Rect maxContour = null;
        MatOfInt hull = new MatOfInt();
        int index = -1;

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
                index = i;
                //maxContour = Imgproc.boundingRect(new MatOfPoint(contourPoly[i].toArray()));
                maxContour = rect[i];
            }
//            if(maxArea < rect[i].area())
//            {
//                maxArea = rect[i].area();
//                maxContour = rect[i];
//                index = i;
//            }
//            double peri = Imgproc.arcLength(contours.get(i), true);
//            MatOfPoint approx = new MatOfPoint();
//            Imgproc.approxPolyDP(contours.get(i), approx, 0.02* peri, true);
//
//            if (approx.size() = 4)
//            {
//                x = approx;
//                break;
//            }
        }

        //Mat drawing = Mat.zeros(greyImage.size(), CvType.CV_8UC3);
        if (maxContour != null)
        {
            Imgproc.rectangle(orignalImage, maxContour.tl(), maxContour.br(), new Scalar(0,255,0), 5);
        }
//        List<MatOfPoint> contoursPolyList = new ArrayList<>(contourPoly.length);
//        for (MatOfPoint2f poly : contourPoly) {
//            contoursPolyList.add(new MatOfPoint(poly.toArray()));
//        }
//
//        for (int i = 0; i < contours.size(); i++) {
//            Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
//            //Imgproc.drawContours(drawing, contoursPolyList, i, color);
//            Imgproc.rectangle(drawing, rect[i].tl(), rect[i].br(), color, 2);
//            //Imgproc.circle(drawing, centers[i], (int) radius[i][0], color, 2);
//        }
        //Imgproc.drawContours(greyImage, );
        return orignalImage;
    }
}
