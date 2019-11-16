import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.net.*;
import java.io.*;

public class Client 
{ 
    // initialize socket and input output streams 
    private Socket           socket  = null; 
    private InputStream  input   = null;
    private BufferedReader   br      = null;
    private DataOutputStream out     = null;
    private FileInputStream fis = null;

    // constructor to put ip address and port 
    public Client(String address, int port) 
    { 
        // establish a connection 
        try
        { 
            socket = new Socket(address, port); 
            System.out.println("Connected"); 
  
            // takes input from terminal
//            input  = new DataInputStream(System.in);

            input = new FileInputStream("Serialized.xml");
            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
//            File file = new File("Serialized.xml");
            //Server responds to HEAD request
            byte[] data =  new byte[8192];
            int ct = 0;
            while((ct = input.read(data)) >0){
                out.write(data, 0, ct);
            }
            out.flush();
        } 
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

        System.out.println("Closing connection");
        // close the connection 
        try
        { 
            input.close(); 
            out.close(); 
            socket.close(); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
  
    public static void main(String args[]) throws NullPointerException
    { 

        Menu menu = new Menu();
        menu.menu();

        // Create a Serialize object
        Serialize s = new Serialize();

        Document doc = null;
        // Serialize to XML
        for(int i=0; i<menu.objects.size(); i++){
            if(i==0){
                doc = s.setup(menu.objects.get(i));
            }else{
                doc = s.serialize(menu.objects.get(i));
            }
        }


        // Create XML file and XMLOutputStream
        XMLOutputter xmlout = new XMLOutputter();

        try {
            FileOutputStream fos = new FileOutputStream("Serialized.xml");

            xmlout.setFormat(Format.getPrettyFormat());
            xmlout.output(doc, System.out);
            xmlout.output(doc, fos);

        }

        catch(IOException e) {
            e.printStackTrace();
        }
        Client client = new Client("172.19.3.151", 5000);
    } 
} 