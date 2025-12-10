package Texture;
import com.sun.opengl.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;
import java.util.*;

public class Anim extends JFrame implements ActionListener {
    public static void main(String[] args) {
        
         AnimGLEventListener2 obj = new AnimGLEventListener2();
         Vector<Integer> vec = new Vector<Integer>();

        vec.add(obj.getscore());
        obj.sethighscore(Collections.max(vec));

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
    new Anim();
    }

    GLCanvas glcanvas;
    AnimGLEventListener2 listener;
    Animator animator ;
    public Anim() {
        listener = new AnimGLEventListener2();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        glcanvas.addMouseListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(glcanvas ,10);
        setTitle("Chicken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
        animator.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
