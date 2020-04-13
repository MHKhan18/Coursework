/*************************************************************************
 *  Compilation:  javac StdOut.java
 *  Execution:    java cos126.StdOut
 *
 *  Writes data of various types to standard output.
 *
 *************************************************************************/
package cos126;

import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;

public final class StdOut {

    // force Unicode UTF-8 encoding; otherwise it's system dependent
    private static String charsetName = "UTF-8";

    // send output here
    private static PrintWriter out;

    // this is called before invoking any methods
    static {
        try {
            out = new PrintWriter(new OutputStreamWriter(System.out, charsetName), true);
        }
        catch (UnsupportedEncodingException e) { System.out.println(e); }
    }

    // singleton pattern - can't instantiate
    private StdOut() { }

    // close the output stream (not required)
    public static void close() { out.close(); }

    // println methods
    public static void println()          { out.println();  }
    public static void println(Object x)  { out.println(x); }
    public static void println(boolean x) { out.println(x); }
    public static void println(char x)    { out.println(x); }
    public static void println(double x)  { out.println(x); }
    public static void println(float x)   { out.println(x); }
    public static void println(int x)     { out.println(x); }
    public static void println(long x)    { out.println(x); }

    // print methods
    public static void print()            {               out.flush(); }
    public static void print(Object x)    { out.print(x); out.flush(); }
    public static void print(boolean x)   { out.print(x); out.flush(); }
    public static void print(char x)      { out.print(x); out.flush(); }
    public static void print(double x)    { out.print(x); out.flush(); }
    public static void print(float x)     { out.print(x); out.flush(); }
    public static void print(int x)       { out.print(x); out.flush(); }
    public static void print(long x)      { out.print(x); out.flush(); }

    // formated printing
    public static void printf(String format, Object... args) {
        out.printf(format, args);
    }


    // This method is just here to test the class
    public static void main(String[] args) {

        // write to stdout
        StdOut.println("Test");
        StdOut.println(17);
        StdOut.println(true);
        StdOut.printf("%.6f\n", 1.0/7.0);
    }

}
