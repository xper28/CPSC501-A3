import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.lang.reflect.*;

public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
        if (c != null) {
            String tab = indent(depth);
            //Check if array
            if (c.isArray()) {
                inspectArr(obj, recursive, depth);
            } else {
                //Do class inspection
                //Print class name to std output
                System.out.println("\n" + tab + "--------------------------");
                System.out.print(tab + "Class name: ");
                System.out.println(c.getName() + " Hashcode: " + obj.hashCode());
                System.out.println(tab + "--------------------------");

                //Print out field information
                Field[] fields = c.getDeclaredFields();
                System.out.println(tab + "Fields: ");
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    //Name
                    System.out.println(tab + " -Name: " + fields[i].getName());
                    //Type
                    System.out.println(tab + " -Type: " + fields[i].getType());
                    //Modifier
                    System.out.println(tab + " -Modifier: " + Modifier.toString(fields[i].getModifiers()));
                    //Value
                    Object field;
                    try {
                        //get field
                        field = fields[i].get(obj);
                        if (field == null) {
                            System.out.println(tab + " -Value: null");
                        } else {
                            //Check if array
                            boolean arr = field.getClass().isArray();
                            //Check if primitive
                            boolean prim = field.getClass().isPrimitive();
                            if (prim) {
                                System.out.println(tab + " -Value: " + field);
                            } else {
                                if (arr) {
                                    inspectArr(field, recursive, depth);
                                } else {
                                    System.out.println(tab + " -Value: " + field.hashCode());
                                    //Recursive inspection
                                    if (recursive) {
                                            inspectClass(fields[i].getType(), field, recursive, depth + 1);
                                    }else {
                                    }
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                depth = depth + 1;
            }
        }
    }

    public String indent(int depth) {
        String tab = "";
        for (int i = 0; i < depth; i++) {
            tab += "\t";
        }
        return tab;
    }

    public <T> void checkNone(T[] array, String tab) {
        if (array.length == 0) {
            System.out.println(tab + "none");
        }
    }

    public void inspectArr(Object obj, boolean recursive, int depth) {
        //Get class info
        Class c = obj.getClass();
        System.out.println("Class name: " + c.getName());
        System.out.println("Type: " + c.getComponentType() + "\n");
        int length = Array.getLength(obj);
        System.out.println("Length: " + length);
        String tab = indent(depth);
        StringBuilder build = new StringBuilder();
        build.append("-Array: [");
        for (int i = 0; i < length; i++) {
            Object item = Array.get(obj, i);
            if(item == null){
                build.append("null, ");
            }else if(obj.getClass().getComponentType().isPrimitive()){
                build.append(item);
                build.append(", ");
            }else if(item.getClass().isArray()){
                inspectArr(item,recursive,depth+1);
            }else{
                build.append(item);
                if(recursive){
                    build.append(item);
                    build.append(", ");
                    inspectClass(item.getClass(),item,recursive,depth+1);
                    build.append(item);
                    build.append(", ");
                }
                build.append(item);
                build.append(", ");
            }
        }
        build.append("]");
        System.out.println(build.toString());
    }
}
