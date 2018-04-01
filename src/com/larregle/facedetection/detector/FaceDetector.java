package com.larregle.facedetection.detector;

import javafx.scene.shape.Rectangle;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FaceDetector {
    private static final FaceDetector instance;
    // TODO Change to relative path in resource directory
    private static final String CLASSIFIER_PATH = "C:\\Users\\fedel\\git2\\face-detection-learning\\resources\\haarcascade_frontalface_alt.xml";

    private final CascadeClassifier cascadeClassifier;

    static {
        instance = new FaceDetector();
    }

    private FaceDetector() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        cascadeClassifier = new CascadeClassifier(CLASSIFIER_PATH);
    }

    public static FaceDetector getInstance() { return instance; }

    public List<Rectangle> detectFaces(File file) throws MalformedURLException {
        List<Rectangle> detections = new ArrayList<>();
        Mat imageMatrix = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
        MatOfRect faceDetected = new MatOfRect();
        cascadeClassifier.detectMultiScale(imageMatrix, faceDetected);
        // TODO Fix Rentangle scale
        for (Rect face : faceDetected.toArray()) {
            detections.add(new Rectangle(face.x, face.y, face.width, face.height));
        }
        return detections;
    }

    private BufferedImage matToImage(Mat imageMatrix) {
        int type = imageMatrix.channels() > 1 ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
        int bufferSize = imageMatrix.channels() * imageMatrix.cols() * imageMatrix.rows();
        byte[] bytes = new byte[bufferSize];
        BufferedImage bufferedImage = new BufferedImage(imageMatrix.cols(), imageMatrix.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(bytes, 0, targetPixels, 0, bytes.length);
        return bufferedImage;
    }
}
