/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;
//import UpperEssential.UpperEssentialLookAndFeel;
import Vista.Inicio;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author José Carlos Grijalva Gonzalez
 */
public class SistemaVentas_V1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        Inicio ventana=new Inicio();
        //UIManager.setLookAndFeel(new UpperEssentialLookAndFeel());
        ventana.setVisible(true);
    }
    
}
