
package SensorTools;

import POJO.JSON.FingerImage;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;

public class FingerImageFinder {
    private static final String DIRECTORY="C:\\fingerImage";
    private static final String URL_HUELLA="https://6iunrk1f11.execute-api.us-west-1.amazonaws.com/dev/huella";
    private static final String METHOD="GET";
    private static final String FORMAT=".bmp";
    private static final String FORMAT_USE="bmp";
     
   
    
    String[] namesDirectory;
    ArrayList<FingerImage> imageBase=new ArrayList<FingerImage>();
    ArrayList<FingerImage> download=new ArrayList<FingerImage>();
    
     /*public static void main(String[] args) throws IOException, JSONException {
            new FingerImageFinder().downloadFingerImage();
    }*/    
    
    public void downloadFingerImage() throws JSONException{                       
        makeDirectory();
        findNamesInDirectory();
        findNamesInBase();
        download();
    }
     public  void makeDirectory(){
        File directorio = new File(DIRECTORY);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio creado");
            } else {
                System.out.println("Error al crear directorio");
            }
        }        
    }   
    public void findNamesInDirectory(){
        File f = new File(DIRECTORY);
        File[] ficheros = f.listFiles();
        namesDirectory=new String[ficheros.length];
        for (int x=0;x<ficheros.length;x++)
            namesDirectory[x]=ficheros[x].getName();                    
    }
    
    private void findNamesInBase() throws JSONException{
        getNameBase(getJSONBase());
        if(namesDirectory.length==0){
            System.out.println("Descargando todo");
            download=imageBase;
        }
        else{
            System.out.println("Descargando alguno");
            findDownload();        
        }
    } 
    private void download(){
        for(int x=0;x<download.size();x++)
        {
            try {
                URL url = new URL(download.get(x).getPublic_url());
                BufferedImage bImage2 = ImageIO.read(url);
                ImageIO.write(bImage2,FORMAT_USE, new File(DIRECTORY+"/"+download.get(x).getPath()+FORMAT) );
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        
    }
    private void findDownload(){
        boolean aux;
        for(int x=0;x<imageBase.size();x++){
            aux=true;
            for(int y=0;y<namesDirectory.length;y++){                ;
                if((imageBase.get(x).getPath()+".bmp").equals(namesDirectory[y])){                       
                    aux=false;
                    break;
                }                
            }
          if(aux){
              download.add(imageBase.get(x));                        
          }
        }
        System.out.println(download.size());
    }
    private void getNameBase(String namesJSON) throws JSONException{
        JSONObject nameObj;
        JSONArray obj=new JSONArray(namesJSON);        
        for(int x=0;x<obj.length();x++){
            nameObj=obj.getJSONObject(x);
            String path=nameObj.getString("path");
            String public_url=nameObj.getString("public_url");
            imageBase.add(new FingerImage(path,public_url));
        }
    }
    private String getJSONBase(){        
        String respuesta = "";
        try {
                respuesta = peticionHttpGet(URL_HUELLA);
                //System.out.println("La respuesta es:\n" + respuesta);
        } catch (Exception e) {                
                e.printStackTrace();
        }
        return respuesta;
    }
    
    public static String peticionHttpGet(String urlParaVisitar) throws Exception {		
            StringBuilder resultado = new StringBuilder();		
            URL url = new URL(urlParaVisitar);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod(METHOD);		
            BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String linea;		
            while ((linea = rd.readLine()) != null) {
                    resultado.append(linea);
            }		
            rd.close();	
            return resultado.toString();
	}

    public String[] getNamesDirectory() {
        return namesDirectory;
    }   
}
