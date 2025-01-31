package coms309.clothing;


public class Clothing {

    String colorID;
    String size;
    String price;
    String material;
    String occasion;
    String name;

    public Clothing(String c, String s, String p, String m, String o, String n){
        colorID = c;
        size = s;
        price = p;
        material = m;
        occasion = o;
        name = n;
    }

    public String getName(){
        return name;
    }

}
