package Texture;
import com.sun.opengl.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.swing.*;
import java.util.*;

public class Anim extends JFrame implements ActionListener {
    public static void main(String[] args) {


        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Anim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
