package coms309.clothing;

public class Footwear extends Clothing{
    boolean laced;
    public Footwear(String c, String s, String p, String m, boolean l, String o, String n){
        super(c,s,p,m,o,n);
        laced = l;
    }
}
