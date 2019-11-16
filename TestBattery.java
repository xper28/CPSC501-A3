import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestBattery {

    @org.junit.Test
    public void testSetup() {
        Coordinate c = new Coordinate(1,1);
        Serialize s = new Serialize();
        Document doc = s.setup(c);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String id = Integer.toString(c.hashCode());
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
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<serialized>\n" +
                "  <object class=\"Coordinate\" id=\""+id +"\">\n" +
                "    <field name=\"row\" declaringclass=\"Coordinate\">\n" +
                "      <value>1</value>\n" +
                "    </field>\n" +
                "    <field name=\"column\" declaringclass=\"Coordinate\">\n" +
                "      <value>1</value>\n" +
                "    </field>\n" +
                "  </object>\n" +
                "</serialized>",outContent.toString().trim().replace("\r",""));

    }

    @org.junit.Test
    public void testSetupArray() {
        Numbers n = new Numbers();
        for(int i=0; i<n.nums.length; i++){
            n.nums[i] = i;
        }
        Serialize s = new Serialize();
        Document doc = s.setup(n);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String id = Integer.toString(n.hashCode());
        String id2 = Integer.toString(n.nums.hashCode());
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
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<serialized>\n" +
                "  <object class=\"[I\" id=\""+id2 +"\" length=\"3\">\n" +
                "    <value>0</value>\n" +
                "    <value>1</value>\n" +
                "    <value>2</value>\n" +
                "  </object>\n" +
                "  <object class=\"Numbers\" id=\""+ id +"\">\n" +
                "    <field name=\"nums\" declaringclass=\"Numbers\">\n" +
                "      <reference>"+id2+"</reference>\n" +
                "    </field>\n" +
                "  </object>\n" +
                "</serialized>",outContent.toString().trim().replace("\r",""));
    }

    @org.junit.Test
    public void testObjectTransfer() {
        Coordinate c = new Coordinate(1,1);
        Serialize s = new Serialize();
        Deserialize d = new Deserialize();
        Document doc = s.setup(c);
        HashMap<String, Object> map = d.setup(doc);
        map.forEach((k, v) -> {
                assertEquals(true, v.getClass().equals(c.getClass()));
        });
    }

    @org.junit.Test
    public void testObjectTransfer2() {
        Coordinate c = new Coordinate(1,12);
        Serialize s = new Serialize();
        Deserialize d = new Deserialize();
        Document doc = s.setup(c);
        HashMap<String, Object> map = d.setup(doc);
        Iterator iterator = map.keySet().iterator();
        String key = (String) iterator.next();
        Coordinate cDuplicate = (Coordinate) map.get(key);
        assertEquals(12, cDuplicate.column);
    }

}
