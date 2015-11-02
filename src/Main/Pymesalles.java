package Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Control.ProductoPeticion;
import Modelo.Entity.Producto;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Control.ClienteControlador;
/**
 *
 * @author Admin
 */
public class Pymesalles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ProductoPeticion pe = new ProductoPeticion();
            List<Producto> prod1;
            //pe.crearProducto("102005", "Cartucho c54 color");
            
            
            prod1= pe.obtenerProductos();
            
            //ProductoJpaController dao=new ProductoJpaController();
            //prod2 = dao.findProductoEntities();
            
            for(Producto p1: prod1){
                System.out.println("Producto -> " + p1.getNombre());
                System.out.println("Còdigo -> " + p1.getCodBarra());
            }
            /*for(Producto p2: prod2){
            System.out.println("Codiigo de Barra ->" + p2.getCodBarra());
            }*/
        } catch (Exception ex) {
            Logger.getLogger(Pymesalles.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("TRATANDO DE CREAR CLIENTE");
  
        
    }
    
}
