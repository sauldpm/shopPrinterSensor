
package ImpresoraTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ImpresoraStatus {
    public static final int DISPONIBLE=3;
    public static final int NO_DISPONIBLE=2;
    public static final int OTRO=4;
    //---------------disponible---------------------
    public static final int OK=0;
    public static final int ABIERTO=4194432;
    public static final int ABIERTO_ANTES_PRENDER=4224;    
    public static final int ABIERTO_2=4198400;
    public static final int PROBLEMA_PAPEL=144;    
    //---------------final disponible---------------------
    public static final int PROBLEMA_PAPEL_COLOCADO=1024;
    //---------------no disponible---------------------
    public static final int APAGADO=4096;
    //---------------final no disponible---------------------
    public static final int ERROR=-1;
    private static final String IMPRESORA_NAME="EPSON TM-T20II Receipt";
    private int statusCode = 0;
    private int stateCode = 0;
    
    //Verifica el estado de la impresora
    public void status(){
        String printerName = IMPRESORA_NAME;
        ProcessBuilder builder = new ProcessBuilder("powershell.exe", "get-wmiobject -class win32_printer | Select-Object Name, PrinterState, PrinterStatus | where {$_.Name -eq '"+printerName+"'}");
        String fullStatus = null;
        Process reg;
        builder.redirectErrorStream(true);
        try {
            reg = builder.start();
            fullStatus = getStringFromInputStream(reg.getInputStream());
            reg.destroy();
        } catch (IOException e1) {        
            e1.printStackTrace();
        }
        int indexPrinterStatusCodeStart = fullStatus.length() - 1;   
        
        while(fullStatus.charAt(indexPrinterStatusCodeStart) != ' '){ 
            indexPrinterStatusCodeStart--;
        }       
        try{        
            statusCode=Integer.parseInt(fullStatus.substring(indexPrinterStatusCodeStart, fullStatus.length()).trim());        
            stateCode=Integer.parseInt(fullStatus.substring(fullStatus.indexOf(printerName) + printerName.length(), indexPrinterStatusCodeStart).trim());
        }catch(Exception e){
            statusCode=ERROR;
            stateCode=ERROR;
            System.err.println("Failed to parse printer status/state codes!" + e.getMessage());            
        }       
    }
    
    //Convierte imputStream a String
    public String getStringFromInputStream(InputStream inputStream){
        String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getStateCode() {
        return stateCode;
    }       
}
