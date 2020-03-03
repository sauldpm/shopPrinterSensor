package Servlet;



import ImpresoraTools.ImpresoraStatus;
import ImpresoraTools.Imprimir;
import POJO.JSON.Ticket;
import POJO.JSON.Productos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Impresora extends HttpServlet {
    private Ticket ticket;
    private ImpresoraStatus impresoraStatus;

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        super.doOptions(req, resp);         
        //doGet(req, resp);
    }
    
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
       doGet(req, resp);
   }
   
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
       if(impresoraStatus!=null)
           impresoraStatus=null;       
     impresoraStatus=new ImpresoraStatus();
     impresoraStatus.status();    
     determinarStatus(req,resp);          
   }
   
    public void determinarStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();
        int statusCode=impresoraStatus.getStatusCode();
        int stateCode=impresoraStatus.getStateCode();        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");      
        resp.addHeader("Access-Control-Allow-Origin", "*");
        System.out.println(statusCode+" "+stateCode);
        switch(statusCode){
            case ImpresoraStatus.DISPONIBLE:
                switch(stateCode){
                    case ImpresoraStatus.OK:
                        ticket=new Ticket();
                        StringBuffer jb=getJSON(req);
                        createTicket(jb);
                        new Imprimir(ticket).initPrint();
                        out.print("{\"code\":"+ImpresoraStatus.OK+"}");
                        out.flush(); 
                        break;
                    case ImpresoraStatus.ABIERTO:
                        out.print("{\"code\":"+ImpresoraStatus.ABIERTO+"}");
                        break;
                    case ImpresoraStatus.ABIERTO_ANTES_PRENDER:
                        out.print("{\"code\":"+ImpresoraStatus.ABIERTO_ANTES_PRENDER+"}");
                        break; 
                    case ImpresoraStatus.ABIERTO_2:
                        out.print("{\"code\":"+ImpresoraStatus.ABIERTO_2+"}");
                        break;
                    case ImpresoraStatus.PROBLEMA_PAPEL:
                        out.print("{\"code\":"+ImpresoraStatus.PROBLEMA_PAPEL+"}");
                        break;
                    default: 
                        out.print("{\"code\":-1001}");
                        break;
                }
                break;
            case ImpresoraStatus.NO_DISPONIBLE:
                switch(stateCode){
                    case ImpresoraStatus.APAGADO:
                        out.print("{\"code\":"+ImpresoraStatus.APAGADO+"}");
                        break;
                    default:
                        out.print("{\"code\":-1001}");
                        break;
                }
                break; 
            case ImpresoraStatus.OTRO:
                switch(stateCode){
                    case ImpresoraStatus.PROBLEMA_PAPEL_COLOCADO:
                        out.print("{\"code\":"+ImpresoraStatus.PROBLEMA_PAPEL_COLOCADO+"}");
                        break;
                    default:
                        out.print("{\"code\":-1001}");
                        break;
                }
                break;
            default:
                out.print("{\"code\":-1001}");
                break;
        }        
    }
       
   private void createTicket(StringBuffer jb){
       try {                      
           JSONObject obj=new JSONObject(jb.toString());
           //Obtener id
           JSONObject idObj=obj.getJSONObject("_id");
           ticket.setId(idObj.getString("$oid")); 
           //Status
           ticket.setStatus(obj.getBoolean("status"));
           //Identifier
           ticket.setIdentifier(obj.getInt("identifier"));
           //Productos
           searchProducts(obj.getJSONArray("products"));
           //Discount
           ticket.setDiscount(obj.getInt("discount"));
           //tax
           ticket.setTax(obj.getInt("tax"));
           //Total
           ticket.setTotal(obj.getDouble("total"));
           //Moneda
           ticket.setMoneda(obj.getString("moneda"));
           //paymented
           ticket.setPaymented(obj.getBoolean("paymented"));
           //Created at
           ticket.setCreatedAt(Long.toString(obj.getJSONObject("created_at").getLong("$date")));
           //Update
           ticket.setUpdatedAt(Long.toString(obj.getJSONObject("updated_at").getLong("$date")));
       } catch (JSONException ex) {
           Logger.getLogger(Impresora.class.getName()).log(Level.SEVERE, null, ex);
       }       
   }
   private void searchProducts(JSONArray productos) throws JSONException{
       for(int x=0;x<productos.length();x++){
           Productos producto=new Productos();
           JSONObject productoJ=(JSONObject) productos.get(x);
           producto.setId(productoJ.getString("_id"));
           producto.setName(productoJ.getString("name"));
           producto.setSku(productoJ.getString("sku"));
           producto.setQty(productoJ.getInt("qty"));
           producto.setPrice(productoJ.getDouble("price"));
           producto.setProductoTotal(productoJ.getDouble("product_total"));
           ticket.addProduct(producto);
       }
   }
   private StringBuffer getJSON(HttpServletRequest req){
       StringBuffer jb = new StringBuffer();
       String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null){                
                jb.append(line);                
            }                
        } catch (Exception e) {System.out.println(e); }
        return jb;
   }
   private void imprimir(){
     System.out.println(ticket.getId());
     System.out.println(ticket.getStatus());
     System.out.println(ticket.getIdentifier());
     System.out.println(ticket.getDiscount());
     System.out.println(ticket.getTax());
     System.out.println(ticket.getTotal());
     System.out.println(ticket.getMoneda());
     System.out.println(ticket.isPaymented());
     System.out.println(ticket.getCreatedAt());
     System.out.println(ticket.getUpdatedAt());
     for(int x=0;x<ticket.getProductos().size();x++){
         System.out.println(ticket.getProductos().get(x).getId());
         System.out.println(ticket.getProductos().get(x).getName());
         System.out.println(ticket.getProductos().get(x).getSku());
         System.out.println(ticket.getProductos().get(x).getQty());
         System.out.println(ticket.getProductos().get(x).getPrice());
         System.out.println(ticket.getProductos().get(x).getProductoTotal());
     }
   }
}
