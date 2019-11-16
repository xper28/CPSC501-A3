import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Serialize {

	private Element  root;
	private Element  arr_ele;
	private Element  field_element;
	private Element  value_element;
	private Element ref_element;
	private Class<?> clas;
	private Object   value;
	// Create XML document object and set the root
	private Document document =null;
	private HashSet<Integer> visited = new HashSet<Integer>();


	public Document setup(Object obj){
		// Set root as serialized
		root = new Element("serialized");
		document = new Document(root);
		serialize(obj);
		//output();
		return document;
	}

	public Document serialize(Object obj){
		// set object element tag
		Element ele = new Element("object");

		// Get the class of the object
		clas = obj.getClass();

		// Set required attributes
		ele.setAttribute("class", clas.getCanonicalName());
		ele.setAttribute("id", String.valueOf(obj.hashCode()));
		//Add object to visited list
		visited.add(obj.hashCode());


		// Get the fields of the object
		Field[] fields = obj.getClass().getDeclaredFields();

		for(Field f : fields) {
			f.setAccessible(true);
			Object field = null;
			try {
				field = f.get(obj);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			//Check for arrays
			if(field.getClass().isArray()){
				// set field element tag
				field_element = new Element("field");

				// get fields of the object
				field_element.setAttribute("name", f.getName());
				String[] temp = String.valueOf(f.getDeclaringClass()).split(" ");
				field_element.setAttribute("declaringclass",temp[1]);
				ref_element = new Element("reference");
				ref_element.setText(Integer.toString(field.hashCode()));
				field_element.addContent(ref_element);
				ele.addContent(field_element);
				if(field.getClass().getComponentType().isPrimitive()){
					serializeArr(field, true);
				}else{
					serializeArr(field, false);
				}
			//Check if primitive field
			}
			else if(f.getType().isPrimitive()){
				// set field element tag
				field_element = new Element("field");

				// get fields of the object
				field_element.setAttribute("name", f.getName());
				String[] temp = String.valueOf(f.getDeclaringClass()).split(" ");
				field_element.setAttribute("declaringclass", temp[1]);

				// set value
				try {
					value = f.get(obj);
				} catch (Exception e) {
					e.printStackTrace();
				}

				value_element = new Element("value");
				value_element.setText(String.valueOf(value));

				field_element.addContent(value_element);
				ele.addContent(field_element);
			//Check if field is an object reference
			}else {
				// create field element tag
				field_element = new Element("field");
				// set field attributes
				field_element.setAttribute("name", f.getName());
				String[] temp = String.valueOf(f.getDeclaringClass()).split(" ");
				field_element.setAttribute("declaringclass", temp[1]);
				// Create reference tag
				ref_element = new Element("reference");
				int id = field.hashCode();
				ref_element.setText(Integer.toString(id));
				field_element.addContent(ref_element);
				ele.addContent(field_element);
				//Check for circular reference
				if(!visited.contains(id)){
					visited.add(id);
					serialize(field);
				}
			}
		}
		root.addContent(ele);
		return document;
	}

	public void output(){
		// Create XML file and XMLOutputStream
		XMLOutputter xmlout = new XMLOutputter();

		try {
			FileOutputStream fos = new FileOutputStream("Serialized.xml");

			xmlout.setFormat(Format.getPrettyFormat());
			xmlout.output(document, System.out);
			xmlout.output(document, fos);
		}

		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void serializeArr(Object obj, boolean prim){
		int length = Array.getLength(obj);
		//Check if array of primitives
		if(prim) {
			//Create field tags
			arr_ele = new Element("object");
			arr_ele.setAttribute("class", obj.getClass().getName());
			arr_ele.setAttribute("id", Integer.toString(obj.hashCode()));
			arr_ele.setAttribute("length", Integer.toString(length));
			for (int i = 0; i < Array.getLength(obj); i++) {
				value = Array.get(obj, i);

				value_element = new Element("value");
				value_element.setText(String.valueOf(value));

				arr_ele.addContent(value_element);
			}
			root.addContent(arr_ele);
		}else{
			//Create field tags
			arr_ele = new Element("object");
			arr_ele.setAttribute("class", obj.getClass().getName());
			arr_ele.setAttribute("id", Integer.toString(obj.hashCode()));
			arr_ele.setAttribute("length", Integer.toString(length));
			for (int i = 0; i < Array.getLength(obj); i++) {
				try{
				serialize(Array.get(obj,i));
				int ref = Array.get(obj, i).hashCode();
				ref_element = new Element("reference");
				ref_element.setText(String.valueOf(ref));
				arr_ele.addContent(ref_element);
				}catch (NullPointerException e){

				}
			}
			root.addContent(arr_ele);
		}
	}
}
