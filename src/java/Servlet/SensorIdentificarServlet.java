package Servlet;

import SensorTools.FingerImageFinder;
import SensorTools.Sensor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;

public class SensorIdentificarServlet extends HttpServlet {

     protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
       doGet(req, resp);
   }
     protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
              FingerImageFinder finger=new FingerImageFinder();
              
         try {
             finger.downloadFingerImage();
         } catch (JSONException ex) {
             Logger.getLogger(SensorIdentificarServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
         
      PrintWriter out = resp.getWriter();
      Sensor sensor=new Sensor();
      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");
      resp.addHeader("Access-Control-Allow-Origin", "*");
      String json="{\"code\":"+sensor.buscar()+"}";       
      out.print(json);
      out.flush();        
   }

}
