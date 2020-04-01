package SensorTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class Windows {
    private ProcessBuilder pb;
    private Process p;
    private static  final String PATH=System.getProperty("user.home")+"\\Documents";
    public Windows(){
        try {
            pb=new ProcessBuilder();
            pb.command(Arrays.asList("python","ventana.py")).directory(new File(PATH));
            p=pb.start();
        } catch (IOException e) {
            System.out.println(e);
        }        
    }
    public void escribir(int x){
        try {
            OutputStream os = p.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(x+"\n");
            bw.flush(); 
        } catch (IOException e) {
            System.out.println(e);
        }        
    }
    public void destruir(){
        p.destroy();
    }
}
