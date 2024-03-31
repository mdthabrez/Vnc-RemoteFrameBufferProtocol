package remoteserver;

import java.awt.*;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerInitiator {

    private String pass = "123456";
    private int wid=1000, hei=1000;


    public static void main(String args[]) {
 
       String port = "5900";
        new ServerInitiator().initialize(Integer.parseInt(port));
    }

    public void initialize(int port) {
        Robot robot = null;
        Rectangle rectangle = null;
        Socket client = null;
        ServerSocket sc = null;
        try {

            sc = new ServerSocket(port);
            drawGUI();
            client = sc.accept();
            System.out.println("New client Connected to the server on port :" + port);
            
            String protocolVersion="RFB 003.003\n";
            String recvprotocolVersion = "";
            DataInputStream dis = new DataInputStream(client.getInputStream());
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            BufferedReader d = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //INITIAL HANDSHAKES
            //PROTOCOL VERSION
            dos.writeUTF(protocolVersion);
            dos.flush();
            recvprotocolVersion = dis.readUTF(); 
            System.out.print("The protocol version received from RFB client :"+recvprotocolVersion);
            //PROTOCOL VERSION - END

            //SECURITY TYPES
            String securityTypes = "\nSecurity types\n1.None\n2.VNC Authentication\n";
            dos.writeUTF(securityTypes);
            dos.flush();
            int recvsecurityType = dis.readInt();
            String ok="OK",failed="failed";
            switch (recvsecurityType) {


                case 1:
                    System.out.println("\nSecurity type - NONE\n");
                    dos.writeUTF(ok);
                    break;
                case 2:
                    System.out.println("\nSecurity type - VNC Authentication\nVNC Password : 123456");
                    String recvpassword = "nope";
                    recvpassword = dis.readUTF();

                    System.out.println(recvpassword);
                    if(!recvpassword.equals(pass))
                    {
                        System.out.println("\nWRONG PASSWORD - TERMINATING...");
                        dos.writeUTF(failed);
                        dos.flush();
                        return;
                    }
                    System.out.println("\nPassword Accepted");
                    dos.writeUTF(ok);
                    dos.flush();
                    break;
                default :
                    System.out.println("\nINVALID CONNECTION - TERMINATING...");
                    dos.writeUTF(failed);
                    return;
            }
            //SECURITY TYPES - END


        //INITIAL HANDSHAKES - END
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gDev = gEnv.getDefaultScreenDevice();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            rectangle = new Rectangle(dim);
            robot = new Robot(gDev);

            new ScreenSpyer(client, robot, rectangle, wid, hei);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void drawGUI() {
        JFrame frame = new JFrame("Remote Admin");
        JButton button = new JButton("Terminate");

        frame.setBounds(100, 100, 150, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(button);
        button.addActionListener(new ActionListener() {

                                     public void actionPerformed(ActionEvent e) {
                                         System.exit(0);
                                     }
                                 }
        );
        frame.setVisible(true);
    }

   
}