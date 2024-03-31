package remoteclient;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import java.net.*;

public class ClientInitiator {

    public static void main(String args[]) throws Exception {

        final JLabel label = new JLabel("", SwingConstants.CENTER);
        final JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(label, BorderLayout.CENTER);
        final Dimension preferredSize = new Dimension(1000, 1000);
        frame.setPreferredSize(preferredSize);
        
        frame.setVisible(true);
        frame.pack();
      
        final ImageUpdateWorker task = new ImageUpdateWorker(label);
        task.execute();
    }

    public static class ImageUpdateWorker extends SwingWorker<Void, Icon> {
        
        private JLabel label;
        ImageUpdateWorker(JLabel label) {
            this.label = label;
            
        }
        
        public void requestFramebufferUpdate(Socket socket ,int FRAMEBUFFER_UPDATE_REQUEST,int x,int y,int width,int height,boolean incremental) throws IOException {
            // Send a framebuffer update request to the server
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(FRAMEBUFFER_UPDATE_REQUEST);// message type
            out.writeInt(x); // x position
            out.writeInt(y); // y position
            out.writeInt(width); // width
            out.writeInt(height); // height
            out.writeBoolean(incremental); // incremental update
          }
          
        @Override
        public Void doInBackground() throws UnknownHostException, IOException {
            Socket socket=new Socket("localhost",5900);
            
            String protocolVersion="RFB 003.003\n";
            String recvprotocolVersion = "";


        try{

        int wid,hei;
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        BufferedReader d = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        //INITIAL HANDSHAKES
        //PROTOCOL VERSION
        recvprotocolVersion = dis.readUTF(); 
        dos.writeUTF(protocolVersion);
        dos.flush();
        System.out.print("The protocol version received from RFB server :"+recvprotocolVersion);
        //PROTOCOL VERSION - END

         //SECURITY TYPES
         String securityTypes = dis.readUTF();
         System.out.print(securityTypes);
         Scanner sc = new Scanner(System.in);

         int recvsecurityType = sc.nextInt();
         dos.writeInt(recvsecurityType);
         dos.flush();
         String status = new String();
         switch (recvsecurityType) {
             case 1:
                 System.out.println("\nSecurity type - NONE\n");
                 status = dis.readUTF();
                 System.out.println(status);
                 break;
             case 2:
                 System.out.print("\nEnter the password : ");
                 String pass = sc.next();
                 try {
                    dos.flush();
                    dos.writeUTF(pass);

                    status = dis.readUTF();

                    if(!status.equals("OK"))
                    {
                        System.out.println(status+"\nTERMINATING...");
                        sc.close();
                        return null;
                    }
                    System.out.println(status);
                 } catch (Exception e) {
                    e.printStackTrace();
                 }
                
         }
         sc.close();
         //SECURITY TYPES - END

        //INITIAL HANDSHAKES
      //  System.out.println("width:"+wid+"height:"+hei);
       wid = dis.readInt();
        hei = dis.readInt();
            boolean isTrue = true;
            while (isTrue) {
 
              Rectangle r = label.getBounds(); //send application size of client window
              dos.writeInt(r.width);
              dos.writeInt(r.height);
             
                requestFramebufferUpdate(socket, 3, 0, 0, wid/2, hei, isTrue);
                int len = dis.readInt();
                System.out.println("len:"+len);

                byte[] imageInByte = new byte[len];
                imageInByte = dis.readNBytes(len); // read framebuffer from server
                System.out.println(imageInByte); //console output


                ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);             //console check for image update by storing image in local memory
                BufferedImage bImage2 = ImageIO.read(bis);                                    //console check for image update by storing image in local memory      
                ImageIO.write(bImage2, "jpg", new File("output.jpg") );  //console check for image update by storing image in local memory
                
                ImageIcon icon = new ImageIcon(imageInByte);
                publish(icon);
               
            }
            socket.close();
            return null;
        }
        catch(Exception e){
            e.printStackTrace();
            socket.close();
            return null;
        }
        }
 
        @Override
        protected void process(List<Icon> icons) {
            label.setIcon(icons.get(icons.size() - 1));
        }
    }
}