import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.collect.ImmutableList;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.video.Video;

public class TakePicture
{
	private int WIDTH = 160;
    private int HEIGHT = 120;
    private int NUM_PIXELS = WIDTH * HEIGHT;
    private int FRAME_SIZE = NUM_PIXELS * 2;
     
     private final String APPLICATION_NAME = "Google-VisionLabelSample/1.0";

     private final int MAX_LABELS = 5;

     private Vision vision = null;
     
	public TakePicture() 
	{
		try
		{
			EV3 ev3 = (EV3) BrickFinder.getLocal();
			Video video = ev3.getVideo();
			video.open(WIDTH, HEIGHT);
			byte[] frame = video.createFrame();
			BufferedImage img = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
			
			while (!Button.ESCAPE.isDown())
			{
				if (Button.UP.isDown())
				{
					video.grabFrame(frame);
					
					for(int i=0;i<FRAME_SIZE;i+=4) 
					{
		                int y1 = frame[i] & 0xFF;
		                int y2 = frame[i+2] & 0xFF;
		                int u = frame[i+1] & 0xFF;
		                int v = frame[i+3] & 0xFF;
		                int rgb1 = convertYUVtoARGB(y1,u,v);
		                int rgb2 = convertYUVtoARGB(y2,u,v);
		                img.setRGB((i % (WIDTH * 2)) / 2, i / (WIDTH * 2), rgb1);
		                img.setRGB((i % (WIDTH * 2)) / 2 + 1, i / (WIDTH * 2), rgb2);
		            }

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
				    ImageIO.write(img, "jpg", baos);
				    byte[] imageBytes = baos.toByteArray();
				        
					
					saveImageToFile(imageBytes);
					
					vision = getVisionService();
					
					printLabels(labelImage(imageBytes));
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static int convertYUVtoARGB(int y, int u, int v) 
	{
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
	
	private List<EntityAnnotation> labelImage(byte[] data) throws IOException
    {
         // [START construct_request]
		
         AnnotateImageRequest request = new AnnotateImageRequest().setImage(new Image().encodeContent(data))
                   .setFeatures(ImmutableList.of(new Feature().setType("LABEL_DETECTION").setMaxResults(MAX_LABELS)));
         Vision.Images.Annotate annotate = vision.images()
                   .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
         // Due to a bug: requests to Vision API containing large images fail
         // when GZipped.
         annotate.setDisableGZipContent(true);
         // [END construct_request]

         System.out.println("Sending image file to server, image = " + data.toString());
         
         // [START parse_response]
         BatchAnnotateImagesResponse batchResponse = annotate.execute();
         assert batchResponse.getResponses().size() == 1;
         
         System.out.println("image sent");
         
         AnnotateImageResponse response = batchResponse.getResponses().get(0);
         
         System.out.println("Get response : " + response.toPrettyString());
         
         if (response.getLabelAnnotations() == null)
         {
              throw new IOException(response.getError() != null ? response.getError().getMessage()
                        : "Unknown error getting image annotations");
         }
         return response.getLabelAnnotations();
         // [END parse_response]
    }
	
	private void printLabels(List<EntityAnnotation> labels)
    {
		
         for (EntityAnnotation label : labels)
         {
        	 System.out.println("Category : " + label.getDescription() + "score:" + label.getScore());
         }
         if (labels.isEmpty())
         {
        	 System.out.println("\tNo labels found.");
         }
    }
	
	private Vision getVisionService() throws IOException, GeneralSecurityException
    {
         GoogleCredential credential = GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
         JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
         
         System.out.println("Connect vision service...");
         
         return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                   .setApplicationName(APPLICATION_NAME).build();
    }
	
	private void saveImageToFile(byte[] img) 
	{
		   try 
		   {
			   System.out.println("Save image");
			   
			   FileOutputStream fos = new FileOutputStream("/tmp/001.jpg",false);
			   fos.write(img);
			   fos.close();
		   } catch(Exception ioe) 
		   {
	           ioe.printStackTrace();   
		   }
	}

	public static void main(String[] args)
	{
		new TakePicture();
	}

}
