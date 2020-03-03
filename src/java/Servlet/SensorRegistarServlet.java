package Servlet;

import SensorTools.FingerImageFinder;
import SensorTools.Sensor;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SensorRegistarServlet extends HttpServlet {

     protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
       doGet(req, resp);
   }
     
     protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
      FingerImageFinder finger=new FingerImageFinder();
      finger.makeDirectory();
      PrintWriter out = resp.getWriter();
      Sensor sensor=new Sensor();
      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");     
      resp.addHeader("Access-Control-Allow-Origin", "*");
      String json="{\"code\":"+sensor.registrar()+"}";       
      out.print(json);
      out.flush();
   }
}
