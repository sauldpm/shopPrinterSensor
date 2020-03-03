
package POJO.JSON;

import java.util.ArrayList;

public class Ticket {
    private String id;
    private boolean status;
    private int identifier;
    private ArrayList<Productos> productos;
    private int discount;
    private int tax;
    private double total;
    private String moneda;
    private boolean paymented;
    private String createdAt;
    private String updatedAt;

    public String getId() {
        return id;
    }

    public boolean getStatus() {
        return status;
    }

    public int getIdentifier() {
        return identifier;
    }

    public ArrayList<Productos> getProductos() {
        return productos;
    }

    public int getDiscount() {
        return discount;
    }

    public int getTax() {
        return tax;
    }

    public double getTotal() {
        return total;
    }

    public String getMoneda() {
        return moneda;
    }

    public boolean isPaymented() {
        return paymented;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public void setProductos(ArrayList<Productos> productos) {
        this.productos = productos;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public void setPaymented(boolean paymented) {
        this.paymented = paymented;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void addProduct(Productos p){
        if(productos==null)
            productos=new ArrayList<Productos>();
        productos.add(p);
    }
}
