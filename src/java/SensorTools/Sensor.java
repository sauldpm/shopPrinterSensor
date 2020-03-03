package SensorTools;

import com.zkteco.biometric.FingerprintSensorErrorCode;
import com.zkteco.biometric.FingerprintSensorEx;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Base64;

public class Sensor {
    private static final String DIRECTORY="C:\\fingerImage\\";
    private long mhDevice = 0; //Identificador del sensor
    private long mhDB = 0;//Identificador DB
    
    private int fpWidth = 0;
    private int fpHeight = 0;
    
    private byte[] imgbuf = null;
    
    private int[] templateLen = new int[1];
    private int[] templateLen2 = new int[1];
    
    private byte[] template = new byte[2048];    
    private byte[] template2 = new byte[2048]; 
        
     public int buscar() throws IOException{
         int ret = FingerprintSensorErrorCode.ZKFP_ERR_OK;         
                //Inicializar                
                if (ret!= FingerprintSensorEx.Init()) //Inicializar recursos
                {
                        System.out.println("Init failed!\n");
                        return -1001;
                }
                ret = FingerprintSensorEx.GetDeviceCount();
                if (ret < 0)//Buscar el numero de dispositivos 
                {
                        System.out.println("No devices connected!\n");
                        FreeSensor();
                        return -1002;
                }
                if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0)))//coneccion al sensor
                {
                        System.out.println("Open device fail, ret = " + ret + "!\n");
                        FreeSensor();
                        return -1003;
                }
                if (0 == (mhDB = FingerprintSensorEx.DBInit()))//inicializar el algoritmo de la libreria
                {
                        System.out.println("Init DB fail, ret = " + ret + "!\n");
                        FreeSensor();
                        return -1;
                }
                
                int nFmt = 0;	
                FingerprintSensorEx.DBSetParameter(mhDB, 5010, nFmt);//indentificador  - estandar				                

                byte[] paramValue = new byte[4];
                int[] size = new int[1];
               
                size[0] = 4;
                FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);// 1 leer solamente width
                fpWidth = byteArrayToInt(paramValue);
                size[0] = 4;
                FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);// 2 leer solamente height
                fpHeight = byteArrayToInt(paramValue);

                imgbuf = new byte[fpWidth*fpHeight];
                //btnImg.resize(fpWidth, fpHeight);                 
                int ren=-1;
                int x=0;               
                while(ren!=0){
                    System.out.println("leyendo");
                     templateLen[0] = 2048;
                     ren=FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLen);                    
                     x++;
                      try {
	                    Thread.sleep(500);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
                      if(x==25){
                          FreeSensor();
                          return 2;
                      }
                } 
                FingerImageFinder imageFinder=new FingerImageFinder();
                imageFinder.findNamesInDirectory();
                String [] names=imageFinder.getNamesDirectory();
                                
                for(int i=0;i<names.length;i++){
                    System.out.println("Comparando");
                    templateLen2[0] = 2048;
                    template2 = new byte[2048];
                    FingerprintSensorEx.ExtractFromImage( mhDB, DIRECTORY+names[i], 500, template2, templateLen2);
                    int percent=FingerprintSensorEx.DBMatch(mhDB ,template2, template);
                    if(percent>=90){
                        FreeSensor();
                        return 0;
                    }   
                }                
                FreeSensor();
                return 3;
     }     
    public int registrar() throws IOException{
         int ret = FingerprintSensorErrorCode.ZKFP_ERR_OK;
                //Inicializar                
                if (ret!= FingerprintSensorEx.Init()) //Inicializar recursos
                {
                        System.out.println("Init failed!\n");
                        return -1001;
                }
                ret = FingerprintSensorEx.GetDeviceCount();
                if (ret < 0)//Buscar el numero de dispositivos 
                {
                        System.out.println("No devices connected!\n");
                        FreeSensor();
                        return -1002;
                }
                if (0 == (mhDevice = FingerprintSensorEx.OpenDevice(0)))//coneccion al sensor
                {
                        System.out.println("Open device fail, ret = " + ret + "!\n");
                        FreeSensor();
                        return -1003;
                }
                if (0 == (mhDB = FingerprintSensorEx.DBInit()))//inicializar el algoritmo de la libreria
                {
                        System.out.println("Init DB fail, ret = " + ret + "!\n");
                        FreeSensor();
                        return -1;
                }
                
                int nFmt = 0;	
                FingerprintSensorEx.DBSetParameter(mhDB, 5010, nFmt);//indentificador  - estandar				                

                byte[] paramValue = new byte[4];
                int[] size = new int[1];
               
                size[0] = 4;
                FingerprintSensorEx.GetParameters(mhDevice, 1, paramValue, size);// 1 leer solamente width
                fpWidth = byteArrayToInt(paramValue);
                size[0] = 4;
                FingerprintSensorEx.GetParameters(mhDevice, 2, paramValue, size);// 2 leer solamente height
                fpHeight = byteArrayToInt(paramValue);

                imgbuf = new byte[fpWidth*fpHeight];
                //btnImg.resize(fpWidth, fpHeight);                 
                int ren=-1;
                int x=0;                
                while(ren!=0){
                     templateLen[0] = 2048;
                     ren=FingerprintSensorEx.AcquireFingerprint(mhDevice, imgbuf, template, templateLen);                    
                     x++;
                      try {
	                    Thread.sleep(500);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
                      if(x==25){
                          FreeSensor();
                          return 2;
                      }
                } 
                
                FingerImageFinder imageFinder=new FingerImageFinder();
                imageFinder.findNamesInDirectory();
                String [] names=imageFinder.getNamesDirectory();
                                
                for(int i=0;i<names.length;i++){
                    System.out.println("Comparando");
                    templateLen2[0] = 2048;
                    template2 = new byte[2048];
                    FingerprintSensorEx.ExtractFromImage( mhDB, DIRECTORY+names[i], 500, template2, templateLen2);
                    int percent=FingerprintSensorEx.DBMatch(mhDB ,template2, template);
                    if(percent>=90){
                        FreeSensor();
                        return 10;//ya esta registrado
                    }   
                }   
                
                String nameFile=generarName();
                writeBitmap(imgbuf, fpWidth, fpHeight,DIRECTORY+nameFile+".bmp");                 
                sendServer(nameFile);                                
                FreeSensor();                   
                return 0;
    }
    
    private void sendServer(String nameFile){
        try {
            URL url = new URL("https://6iunrk1f11.execute-api.us-west-1.amazonaws.com/dev/huella");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString ="{\"data\":\""+toBase64(DIRECTORY+nameFile+".bmp")+"\",\"name\":\""+nameFile+"\"}";
            try(OutputStream os = con.getOutputStream()) {
                 byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length); 
                os.close();
            }
            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();
                System.out.println(response.toString());
            }
          con.disconnect();          
        } catch (MalformedURLException e) {
            FreeSensor();
            System.err.println(e);  
        } catch (ProtocolException e){
            FreeSensor();
            System.err.println(e);  
        }
        catch (IOException e){
            FreeSensor();
            System.err.println(e);  
        }        
    }
    
    public String toBase64(String nameFile) throws IOException{
        File f =  new File(nameFile);
        FileInputStream fileInputStreamReader = new FileInputStream(f);
            byte[] bytes = new byte[(int)f.length()];
            fileInputStreamReader.read(bytes);
            return Base64.getEncoder().encodeToString(bytes);
    }
    
     public static void writeBitmap(byte[] imageBuf, int nWidth, int nHeight,String path) throws IOException {
         System.out.println(path);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
            java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

            int w = (((nWidth+3)/4)*4);
            int bfType = 0x424d;
            int bfSize = 54 + 1024 + w * nHeight;
            int bfReserved1 = 0;
            int bfReserved2 = 0;
            int bfOffBits = 54 + 1024;

            dos.writeShort(bfType); 
            dos.write(changeByte(bfSize), 0, 4); 
            dos.write(changeByte(bfReserved1), 0, 2);
            dos.write(changeByte(bfReserved2), 0, 2);
            dos.write(changeByte(bfOffBits), 0, 4);

            int biSize = 40;
            int biWidth = nWidth;
            int biHeight = nHeight;
            int biPlanes = 1; 
            int biBitcount = 8;
            int biCompression = 0;
            int biSizeImage = w * nHeight;
            int biXPelsPerMeter = 0;
            int biYPelsPerMeter = 0;
            int biClrUsed = 0;
            int biClrImportant = 0;

            dos.write(changeByte(biSize), 0, 4);
            dos.write(changeByte(biWidth), 0, 4);
            dos.write(changeByte(biHeight), 0, 4);
            dos.write(changeByte(biPlanes), 0, 2);
            dos.write(changeByte(biBitcount), 0, 2);
            dos.write(changeByte(biCompression), 0, 4);
            dos.write(changeByte(biSizeImage), 0, 4);
            dos.write(changeByte(biXPelsPerMeter), 0, 4);
            dos.write(changeByte(biYPelsPerMeter), 0, 4);
            dos.write(changeByte(biClrUsed), 0, 4);
            dos.write(changeByte(biClrImportant), 0, 4);

            for (int i = 0; i < 256; i++) {
                    dos.writeByte(i);
                    dos.writeByte(i);
                    dos.writeByte(i);
                    dos.writeByte(0);
            }

            byte[] filter = null;
            if (w > nWidth)
            {
                    filter = new byte[w-nWidth];
            }

            for(int i=0;i<nHeight;i++)
            {
                    dos.write(imageBuf, (nHeight-1-i)*nWidth, nWidth);
                    if (w > nWidth)
                            dos.write(filter, 0, w-nWidth);
            }
            dos.flush();
            dos.close();
            fos.close();
	}

	public static byte[] changeByte(int data) {
		return intToByteArray(data);
	}
	
	public static byte[] intToByteArray (final int number) {
            byte[] abyte = new byte[4];  	    
	    abyte[0] = (byte) (0xff & number);  	    
	    abyte[1] = (byte) ((0xff00 & number) >> 8);  
	    abyte[2] = (byte) ((0xff0000 & number) >> 16);  
	    abyte[3] = (byte) ((0xff000000 & number) >> 24);  
	    return abyte; 
	}
         public static int byteArrayToInt(byte[] bytes) {
            int number = bytes[0] & 0xFF;  		      
            number |= ((bytes[1] << 8) & 0xFF00);  
            number |= ((bytes[2] << 16) & 0xFF0000);  
            number |= ((bytes[3] << 24) & 0xFF000000);  
            return number;  
	}
     private void FreeSensor()
	{			
            if (0 != mhDB)
            {
                    FingerprintSensorEx.DBFree(mhDB);                    
                    mhDB = 0;
            }
            if (0 != mhDevice)
            {
                    FingerprintSensorEx.CloseDevice(mhDevice);                    
                    mhDevice = 0;
            }
            FingerprintSensorEx.Terminate();
	}   
     
     private String generarName(){
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();        
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i <50; i++) {			
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            //System.out.format("%d\t:\t%c%n", rndCharAt, rndChar);
            sb.append(rndChar);       
        }
         return sb.toString();
     }
}
