package com.ride_steady.ride_steady.live_stream;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.opencv.imgproc.Imgproc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Live_Stream {

    VideoCapture videoCapture = new VideoCapture("https://10.0.0.230:8080/");
    Mat mat = new MatOfByte();
    boolean open = videoCapture.read(mat);

    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("https://10.0.0.230:8080/");

    public static void main( String[] args )
    {
        Image image = null;
        BufferedImage Image =null;

        // loads image from file system.
        try {

            File sourceimage = new File("C:\\Users\\mhine\\Downloads\\Alligator-to-pothole.jpg");
            image = ImageIO.read(sourceimage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        *   This method screenshots and displays the results in a JFrame.
        *   The idea is to continuously screenshot the streamed video from the internet, while passing
        *   to the computer vision module for pattern recognition and verification.
        * */
        try {
            Thread.sleep(120);
            Robot bot = new Robot();

            // saves screenshot to desired path
            String path = "C:\\Users\\mhine\\Downloads\\Shot.jpg";

            // Used to get ScreenSize and capture image
            Rectangle capture =
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            //BufferedImage Image = r.createScreenCapture(capture);
            Image = bot.createScreenCapture(capture);
            ImageIO.write(Image, "jpg", new File(path));
            System.out.println("Screenshot saved");
        }
        catch (AWTException | IOException | InterruptedException ex) {
            System.out.println(ex);
        }

        //sets image from file system to JFrame.
        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        JLabel label = new JLabel(new ImageIcon(image));
        frame.add(label);
        frame.setVisible(true);

        //sets screenshot to JFrame.
        JFrame frame1 = new JFrame();
        frame1.setSize(300, 300);
        JLabel  label1 = new JLabel(new ImageIcon(Image));
        frame1.add(label1);
        frame1.setVisible(true);

        /*
        *
        *   converts image to grey scale to store in file system.
        *
        * */
        //Loading the OpenCV core library
        //System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        //Reading the image
        Mat src = Imgcodecs.imread("C:\\Users\\mhine\\Downloads\\Alligator-to-pothole.jpg");
        //Creating the empty destination matrix
        Mat dst = new Mat();
        //Converting the image to grey scale
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);
        //Instantiating the Imagecodecs class
        Imgcodecs imageCodecs = new Imgcodecs();
        //Writing the image
        imageCodecs.imwrite("C:\\Users\\mhine\\Downloads\\colortogreyscale.jpg", dst);
        System.out.println("Image Saved");

        /*
        *   contours
        *
        * */
        Mat source = Imgcodecs.imread("C:\\Users\\mhine\\Downloads\\Pothole1.jpg");
        Mat gray = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_BGR2GRAY);
        Mat binary = new Mat(source.rows(), source.cols(), source.type(), new Scalar(0));
        Imgproc.threshold(gray, binary, 100, 255, Imgproc.THRESH_BINARY_INV);
        //Finding Contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchey = new Mat();
        Imgproc.findContours(binary, contours, hierarchey, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_SIMPLE);
        //Drawing the Contours
        Scalar color = new Scalar(0, 0, 255);
        Imgproc.drawContours(source, contours, -1, color, 2, Imgproc.LINE_8,
                hierarchey, 2, new Point() ) ;
        HighGui.imshow("Drawing Contours", source);
        HighGui.waitKey();

        /*
        *  Guassian blur
        *
        * */
        /* Mat blursrc = Imgcodecs.imread("C:\\Users\\mhine\\Downloads\\Pothole1.jpg");
        Mat blurdst = new Mat(blursrc.rows(), blursrc.cols(), blursrc.type());
        Imgproc.GaussianBlur(blursrc,blurdst, new Size(15,15), 0);
        imageCodecs.imwrite("C:\\Users\\mhine\\Downloads\\blur.jpg",blurdst);
        HighGui.imshow("Blur", blurdst);
        HighGui.waitKey();*/

        /*
        *   thresholding
        * */
        /*  Mat image2 = Imgcodecs.imread("C:\\Users\\mhine\\Downloads\\blur.jpg", Imgcodecs.IMREAD_GRAYSCALE);

        Mat dest = new Mat(image2.rows(), image2.cols(), image2.type());
        // maxValue: 125 blockSize: 11 C:12
        Imgproc.adaptiveThreshold(image2, dest, 125,
                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 11, 12);
        imageCodecs.imwrite("C:\\Users\\mhine\\Downloads\\thresholding.jpg",dest);
        HighGui.imshow("TruncateThresholding", dest);
        HighGui.waitKey();*/

        /*
        *   uncanny
        * */
        /* Mat srcpic = Imgcodecs.imread("C:\\Users\\mhine\\Downloads\\Alligator-to-pothole.jpg");
        Mat gry = new Mat();

        Imgproc.cvtColor(srcpic,gry, Imgproc.COLOR_BGR2GRAY);
        Mat edges = new Mat();

        // initial thresholds: 60 ; 60*3
        Imgproc.Canny(gry, edges, 60, 60*3);
        Imgcodecs.imwrite("C:\\Users\\mhines\\Downloads\\canny_out.jpg",edges);
        HighGui.imshow("Uncanny",edges);
        HighGui.waitKey();
        System.out.println("uncanny loaded");

        /*
        *   distance transform - depth. This method seeks to locate depth by highlighting its edge.
        * */
        /* Mat dpthsrc = Imgcodecs.imread("C:\\Users\\mhine\\Downloads\\Alligator-to-pothole.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        Mat dpthdst = new Mat();
        Mat dpthbinary = new Mat();
        // changes: Thres_binary_inv ; thresh: 100 maxVal: 255 maskSize:3
        Imgproc.threshold(dpthsrc,dpthbinary,50,255, Imgproc.THRESH_BINARY);

        Imgproc.distanceTransform( dpthbinary, dpthdst, Imgproc.DIST_C, 3);
        Core.convertScaleAbs(dpthdst, dpthdst);

        HighGui.imshow("transform",dpthdst);
        HighGui.waitKey(); */
    }
}
