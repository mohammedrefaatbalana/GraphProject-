import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import static java.awt.SystemColor.menu;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.media.opengl.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static javafx.scene.text.Font.font;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class AnimGLEventListener2 implements GLEventListener, KeyListener, MouseListener , MouseMotionListener {

    TextRenderer t = new TextRenderer(Font.decode("SansSerif"));
    final String assetsFolderName = "Texture";
    int maxWidth = 100;
    int maxHeight = 100;
    int x = maxWidth / 2, y = 1/* maxHeight / 2*/;
    int index = 0;
    int indexx = 0;
    int yy = 100;
    public int xPosition = 1000;
    public int yPosition = 1000;
    boolean useKeyboard = false;
    boolean useMouse = false;
    boolean chooseControl = false;


    ArrayList<Point> eggs = new ArrayList<Point>();

    String textureNames[] = {"basket.png", "egg.png", "exit.png", "gameOverbkg.png", "gamebkg.png", "help.png", "helpbkg.png", "menubkg.png", "play.png", "paused.png", "Chicken1.png", "Chicken2.png", "Chicken3.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];
    int previousX = 0;
    int currentX = 0;
    int score = 0;
    Clip clip;
    File soundFile;
    AudioInputStream audioIn;

    public void init(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );

            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 50; i++) {
            if (i < 7) {
                currentX = (int) (Math.random() * 10 + 1) * 10;
                while ((Math.abs(currentX - previousX) > 20) || (currentX == previousX)) {
                    currentX = (int) (Math.random() * 10 + 1) * 10;
                }
                eggs.add(new Point(currentX, yy));
                yy += 50;
                previousX = currentX;
            } else if (i < 15) {
                currentX = (int) (Math.random() * 10 + 1) * 10;
                while ((Math.abs(currentX - previousX) > 30) || (currentX == previousX)) {
                    currentX = (int) (Math.random() * 10 + 1) * 10;
                }
                eggs.add(new Point(currentX, yy));
                yy += 35;
                previousX = currentX;
            } else if (i < 20) {
                currentX = (int) (Math.random() * 10 + 1) * 10;
                while ((Math.abs(currentX - previousX) > 40) || (currentX == previousX)) {
                    currentX = (int) (Math.random() * 10 + 1) * 10;
                }
                eggs.add(new Point(currentX, yy));
                yy += 30;
                previousX = currentX;
            } else if (i < 25) {
                currentX = (int) (Math.random() * 10 + 1) * 10;
                while ((Math.abs(currentX - previousX) > 50) || (currentX == previousX)) {
                    currentX = (int) (Math.random() * 10 + 1) * 10;
                }
                eggs.add(new Point(currentX, yy));
                yy += 25;
                previousX = currentX;
            } else if (i < 35) {
                currentX = (int) (Math.random() * 10 + 1) * 10;
                while ((Math.abs(currentX - previousX) > 60) || (currentX == previousX)) {
                    currentX = (int) (Math.random() * 10 + 1) * 10;
                }
                eggs.add(new Point(currentX, yy));
                yy += 18;
                previousX = currentX;
            } else if (i < 50) {
                currentX = (int) (Math.random() * 10 + 1) * 10;
                while ((Math.abs(currentX - previousX) > 60) || (currentX == previousX)) {
                    currentX = (int) (Math.random() * 10 + 1) * 10;
                }
                eggs.add(new Point(currentX, yy));
                yy += 10;
                previousX = currentX;
            }
        }
    }
    int eggshanded = 1;
    int level = 1;
    int oldscore = 0;
    int lifes = 3;
    boolean gameover = false;
    boolean pause = false;
    boolean h = false;
    boolean firstone = true;
    boolean startgame = false;
    boolean helppic = false;
    boolean startagain = false;
    int Chickenindex =10;
    int highscore = 0;
    boolean askName = false;
    String playerName = "";
    boolean nameEntered = false;

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

    }
    public void chooseControl(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        t.beginRendering(400, 400);
        t.setColor(Color.BLACK);
        t.draw("Choose Control Mode", 80, 300);
        t.draw("Press K for Keyboard", 100, 240);
        t.draw("Press M for Mouse", 100, 200);
        t.endRendering();

        // choose keyboard
        if (isKeyPressed(KeyEvent.VK_K)) {
            useKeyboard = true;
            useMouse = false;
            startgame = true;
            chooseControl = false;
        }

        // chosse mouse
        if (isKeyPressed(KeyEvent.VK_M)) {
            useMouse = true;
            useKeyboard = false;
            startgame = true;
            chooseControl = false;
        }
    }


    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.set(keyCode);
    }
    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

     @Override
    public void mouseMoved(MouseEvent e) {
        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();

        
        xPosition = (int)((e.getX() / width) * maxWidth);      
       // yPosition = (int)(((height - e.getY()) / height) * maxHeight);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
}
