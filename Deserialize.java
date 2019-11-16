import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;

import org.jdom2.*;

public class Deserialize {

	public HashMap<String, Object> setup(Document src) {
		Element elem = src.getRootElement();
		List<Element> objList = elem.getChildren();

		HashMap<String, Object> visited = new HashMap<>();
		deserialize(visited, objList);
		fillFields(visited, objList);
		return visited;
	}

	public void deserialize(HashMap<String, Object> visited, List<Element> objList) {
		for (int i = 0; i < objList.size(); i++) {
			Element elem = objList.get(i);
			try {
				Class c = Class.forName(elem.getAttributeValue("class"));
				Object obj = null;
				if (!c.isArray()) {
					Constructor cons = c.getDeclaredConstructor(null);
					cons.setAccessible(true);
					obj = cons.newInstance(null);
				} else {
					System.out.println("MARK" + elem.getAttributeValue("class"));
					System.out.println("MARKER" + elem.getAttributeValue("length"));
					obj = Array.newInstance(c.getComponentType(), Integer.parseInt(elem.getAttributeValue("length")));
					System.out.println("CLEARED");
				}
				visited.put(elem.getAttributeValue("id"), obj);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public void fillFields(HashMap<String, Object> visited, List<Element> objList) {
		for (int i = 0; i < objList.size(); i++) {
			Element elem = objList.get(i);
			Object obj = visited.get(elem.getAttributeValue("id"));
			List<Element> fieldElems = elem.getChildren();
			if (!obj.getClass().isArray()) {
				for (int j = 0; j < fieldElems.size(); j++) {
					Element fieldElem = fieldElems.get(j);
					String cName = fieldElem.getAttributeValue("declaringclass");
					try {
						Class fieldDC = Class.forName(cName);
						String fName = fieldElem.getAttributeValue("name");
						try {
							Field f = fieldDC.getDeclaredField(fName);
							f.setAccessible(true);
							Element vElem = fieldElem.getChildren().get(0);
							if(!(Modifier.isStatic(f.getModifiers()))){
								f.set(obj, obtainVal(vElem, f.getType(), visited));
							}
						} catch (NoSuchFieldException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			} else {
				Class comptype = obj.getClass().getComponentType();
				for (int j = 0; j < fieldElems.size(); j++) {
					Array.set(obj, j, obtainVal(fieldElems.get(j), comptype, visited));
				}
			}
		}
	}

	private Object obtainVal(Element vElem, Class<?> type, HashMap<String, Object> visited) {
		String vType = vElem.getName();
		if (vType.equals("null")) {
			return null;
		} else if (vType.equals("reference")) {
			return visited.get(vElem.getText());
		} else {
			if (type.equals(boolean.class)) {
				if (vElem.getText().equals("true")) {
					return Boolean.TRUE;
				} else {
					return Boolean.FALSE;
				}
			} else if (type.equals(byte.class)) {
				return Byte.valueOf(vElem.getText());
			} else if (type.equals(short.class)) {
				return Short.valueOf(vElem.getText());
			} else if (type.equals(int.class)) {
				return Integer.valueOf(vElem.getText());
			} else if (type.equals(long.class)) {
				return Long.valueOf(vElem.getText());
			} else if (type.equals(float.class)) {
				return Float.valueOf(vElem.getText());
			} else if (type.equals(double.class)) {
				return Double.valueOf(vElem.getText());
			} else if (type.equals(char.class)) {
				return Character.valueOf(vElem.getText().charAt(0));
			} else {
				return vElem.getText();
			}
		}
	}
}