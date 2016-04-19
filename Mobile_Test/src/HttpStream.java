import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
 
import javax.imageio.ImageIO;
 
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.video.Video;
 
public class HttpStream {
     
    private static int WIDTH = 160;
    private static int HEIGHT = 120;
    private static int NUM_PIXELS = WIDTH * HEIGHT;
    private static int FRAME_SIZE = NUM_PIXELS * 2;
 
    public static void main(String[] args) throws IOException {
        EV3 ev3 = (EV3) BrickFinder.getLocal();
        Video video = ev3.getVideo();
        video.open(WIDTH, HEIGHT);
        byte[] frame = video.createFrame(); 
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
        ServerSocket ss = new ServerSocket(8080);
        Socket sock = ss.accept();
        String boundary = "Thats it folks!";
        writeHeader(sock.getOutputStream(), boundary);
        while (Button.ESCAPE.isUp()) {
            video.grabFrame(frame);
            for(int i=0;i<FRAME_SIZE;i+=4) {
                int y1 = frame[i] & 0xFF;
                int y2 = frame[i+2] & 0xFF;
                int u = frame[i+1] & 0xFF;
                int v = frame[i+3] & 0xFF;
                int rgb1 = convertYUVtoARGB(y1,u,v);
                int rgb2 = convertYUVtoARGB(y2,u,v);
                img.setRGB((i % (WIDTH * 2)) / 2, i / (WIDTH * 2), rgb1);
                img.setRGB((i % (WIDTH * 2)) / 2 + 1, i / (WIDTH * 2), rgb2);
            }
            writeJpg(sock.getOutputStream(), img, boundary);
        }   
        video.close();
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
     
    private static void writeJpg(OutputStream stream, BufferedImage img, String boundary) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();
        stream.write(("Content-type: image/jpeg\r\n" +
                "Content-Length: " + imageBytes.length + "\r\n" +
                "\r\n").getBytes());
        stream.write(imageBytes);
        stream.write(("\r\n--" + boundary + "\r\n").getBytes());
    }
     
    private static int convertYUVtoARGB(int y, int u, int v) {
        int c = y - 16;
        int d = u - 128;
        int e = v - 128;
        int r = (298*c+409*e+128)/256;
        int g = (298*c-100*d-208*e+128)/256;
        int b = (298*c+516*d+128)/256;
        r = r>255? 255 : r<0 ? 0 : r;
        g = g>255? 255 : g<0 ? 0 : g;
        b = b>255? 255 : b<0 ? 0 : b;
        return 0xff000000 | (r<<16) | (g<<8) | b;
    }
}