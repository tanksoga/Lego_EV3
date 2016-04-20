import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
 
import lejos.hardware.Button;
 
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
 
public class HttpStreamOpenCV {
    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat();
        VideoCapture vid = new VideoCapture(0);
        vid.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 160);
        vid.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 120);
        vid.open(0);
        System.out.println("Camera open");
           
        ServerSocket ss = new ServerSocket(8080);
        Socket sock = ss.accept();
        System.out.println("Socket connected");
        String boundary = "Thats it folks!";
        writeHeader(sock.getOutputStream(), boundary);
        System.out.println("Written header");
         
        long stime = System.currentTimeMillis();
        int cnt = 0;
        while (Button.ESCAPE.isUp()) {
            vid.read(mat);
            if (!mat.empty()) {
                writeJpg(sock.getOutputStream(), mat, boundary);
                System.out.println("Written jpg");
                if (cnt++ >= 100)
                {
                    long stop = System.currentTimeMillis();
                    System.out.println("Frame rate: " + (cnt*1000/(stop - stime)));
                    cnt = 0;
                    stime = stop;
                }
            } else  System.out.println("No picture");
        }
        sock.close();
        ss.close();    
    }
      
    private static void writeHeader(OutputStream stream, String boundary) throws IOException {
        stream.write(("HTTP/1.0 200 OK\r\n" +
                "Connection: close\r\n" +
                "Max-Age: 0\r\n" +
                "Expires: 0\r\n" +
                "Cache-Control: no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0\r\n" +
                "Pragma: no-cache\r\n" + 
                "Content-Type: multipart/x-mixed-replace; " +
                "boundary=" + boundary + "\r\n" +
                "\r\n" +
                "--" + boundary + "\r\n").getBytes());
    }
      
    private static void writeJpg(OutputStream stream, Mat img, String boundary) throws IOException {
        MatOfByte buf = new MatOfByte();
        Highgui.imencode(".jpg", img, buf);
        byte[] imageBytes = buf.toArray();
        stream.write(("Content-type: image/jpeg\r\n" +
                "Content-Length: " + imageBytes.length + "\r\n" +
                "\r\n").getBytes());
        stream.write(imageBytes);
        stream.write(("\r\n--" + boundary + "\r\n").getBytes());
    }
 }