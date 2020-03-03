/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImpresoraTools;

import POJO.JSON.Productos;
import POJO.JSON.Ticket;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import com.github.anastaciocintra.output.PrinterOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.print.PrintService;

/**
 *
 * @author pc1
 */
public class Imprimir {
   private Ticket ticket;
   private ArrayList<String> productosS;
   
    
    public Imprimir(Ticket ticket){
        this.ticket=ticket;
    }    
    public void initPrint(){               
        extraerProductos();
        printTicket();
    }
    public void printTicket() {        
        try {              
        PrintService foundService =PrinterOutputStream.getDefaultPrintService();                 
        BarCode barcode=new BarCode();
        barcode.setBarCodeSize(4,125);
        barcode.setJustification(EscPosConst.Justification.Center);
        
        EscPos escpos;                                      
        escpos = new EscPos(new PrinterOutputStream(foundService));
        
        Style productos=new Style().setJustification(EscPosConst.Justification.Center);       
        Style bold = new Style(escpos.getStyle()).setBold(true);               
        
        escpos.initializePrinter();
        StringBuilder z=new  StringBuilder();
        for(int x=0;x<productosS.size();x++)
            for(int y=0;y<ticket.getProductos().get(x).getQty();y++)
                escpos.writeLF(productos,productosS.get(x));
        escpos.feed(2);
        escpos.write(bold,addEspacios("Total:",Double.toString(ticket.getTotal())));
        escpos.feed(3);
        escpos.write(barcode,Integer.toString(ticket.getIdentifier()));        
        escpos.feed(5);
        escpos.cut(EscPos.CutMode.FULL);
        escpos.close(); 
        } catch (IOException e) {
            System.out.println(e);
        }              
    }
    public String addEspacios(String a,String b){
        String pal=a;
        while((pal.length()+b.length())!=40){
                pal+=" ";
            }
        return pal+b;
    }
    public void extraerProductos(){
        productosS=new ArrayList<String>();
        ArrayList productos=ticket.getProductos();
        for(int x=0;x<productos.size();x++){
            Productos p=(Productos) productos.get(x); 
            String a=p.getName();
            String b="$"+Double.toString(p.getPrice());            
            while((a.length()+b.length())!=40){
                a+=" ";
            }
            a+=b;
            productosS.add(a);
        }                
    }
}
