
// A Java program for a Server 
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class Server 
{ 
    //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private InputStream in       = null;
  
    // constructor with port 
    public Server(int port) 
    { 
        // starts server and waits for a connection 
        try
        { 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
  
            System.out.println("Waiting for a client ..."); 
  
            socket = server.accept(); 
            System.out.println("Client accepted"); 
  
            // takes input from the client socket 
            in = socket.getInputStream();

            // Create a Deserialize object
            Deserialize d = new Deserialize();

            
            // Deserialize

            FileOutputStream fos = new FileOutputStream("Serialized1.xml");
            //Server responds to HEAD request
            byte[] data =  new byte[8192];
            int ct = 0;
            while((ct = in.read(data)) >0){
                System.out.println(data);
                fos.write(data, 0, ct);
            }
            //fos.flush();
            fos.close();
            SAXBuilder sax = new SAXBuilder();
            try {
                File input = new File("Serialized1.xml");
                Document doc = sax.build(input);
                Inspector i  = new Inspector();
                HashMap<String, Object> map = d.setup(doc);
                map.forEach((k, v) -> {
                    i.inspect(v,false);
                });
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Closing connection"); 
  
            // close connection 
            socket.close();
            server.close();
            in.close(); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
  
    public static void main(String args[]) 
    {
        Server server = new Server(5000);
    } 
}