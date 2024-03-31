package remoteserver;

import javax.imageio.*;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.net.Socket;
import javax.swing.ImageIcon;

import java.io.*;

public class ScreenSpyer extends Thread {

    Socket socket = null; 
    Robot robot = null; 
    Rectangle rectangle ; 
    int wid,sWid,hei,sHei;
    boolean continueLoop = true;
    DataOutputStream dos = null;
    
    public ScreenSpyer(Socket socket, Robot robot,Rectangle rect, int w, int h) {
        this.socket = socket;
        this.robot = robot;
        rectangle = rect;
        wid = w;
        hei = h;
        start();
    }
    public void processFramebufferUpdate() throws IOException {
        // Read the framebuffer update message from the server

        DataInputStream in = new DataInputStream(socket.getInputStream());
        
        int messageType = in.readInt();
          // Read the rectangle data
          int x = in.readInt();
          int y = in.readInt();
          int width = in.readInt();
          int height = in.readInt();
          boolean incremental  = in.readBoolean();
            if(incremental)
                {   Rectangle recta = new Rectangle(x,y,width,height);
                    rectangle = recta;    
                }
        
      }

    public void run(){
             
      try
      {     
      //  System.out.println("log1"); 
      dos = new DataOutputStream(socket.getOutputStream());

      dos.writeInt(rectangle.width);
      dos.writeInt(rectangle.height);
      }
      catch(Exception e)
      {e.printStackTrace();}

      while(continueLoop){
            
			try{
            
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            wid = dis.readInt(); //
            hei = dis.readInt(); // Read the Dimensions of client application
            System.out.println("width:"+wid+"height:"+hei);

            processFramebufferUpdate(); //receive framebuffer request

            BufferedImage image = robot.createScreenCapture(rectangle);            			
            byte[] imageInByte;
            
		    Image im1 = image.getScaledInstance(wid,hei, Image.SCALE_SMOOTH);
            BufferedImage bi1 = new BufferedImage(wid,hei,BufferedImage.TYPE_INT_RGB);
	        Graphics g = bi1.createGraphics();
            g.drawImage(im1,0,0,new Color(0,0,0), null);
            g.dispose(); 
		    
		                
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi1,"jpg", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();

            
            dos.writeInt(imageInByte.length);        // send framebuffer length
			System.out.println(imageInByte.length);  // console print of framebuffer length
            dos.write(imageInByte);                  // send the framebuffer   
            dos.flush();
			}
			catch(Exception e)
			{e.printStackTrace();}
                      
        }

    }

}
