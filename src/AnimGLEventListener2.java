
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
import javax.swing.*;

public class AnimGLEventListener2 implements GLEventListener, KeyListener, MouseListener , MouseMotionListener {

    TextRenderer t = new TextRenderer(
            new Font("SansSerif", Font.BOLD, 12),
            true,   // anti-aliased
            true    // use fractional metrics
    );
    final String assetsFolderName = "";
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

    String textureNames[] = {"basket1.png", "egg.png", "EXIT.png", "GAME OVER bg.png", "gamebg.png", "HELP.png", "HELPbg.png", "HOM1.png", "START-removebg-preview.png", "PAUSE.png", "ch4.png", "ch2.png", "ch3.png"};
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
                String path = assetsFolderName.isEmpty()
                        ? textureNames[i]
                        : assetsFolderName + File.separator + textureNames[i];
                texture[i] = TextureReader.readTexture(path, true);

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
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gld.addMouseMotionListener(this);

        //  menu screen
        if (firstone) {
            menu(gl);

            // Detect Play
            if ((isKeyPressed(KeyEvent.VK_ENTER) || ((xPosition <= -90 && xPosition >= -210) && (yPosition <= -70 && yPosition >= -90)))) {
                firstone = false;
                askName = true; // user name
            }

            // Detect Help
            if (isKeyPressed(KeyEvent.VK_H) || ((xPosition <= 60 && xPosition >= -60) && (yPosition <= -70 && yPosition >= -90))) {
                helppic = true;
                firstone = false;
            }

            // Detect Exit
            if (isKeyPressed(KeyEvent.VK_ESCAPE) || ((xPosition <= 210 && xPosition >= 90) && (yPosition <= -70 && yPosition >= -90))) {
                firstone = false;
                System.exit(0);
            }
        }

        // user for once
        if (askName) {
            askName = false;
            SwingUtilities.invokeLater(() -> {
                String name = JOptionPane.showInputDialog(null, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
                if (name != null && !name.isEmpty()) {
                    playerName = name;
                    System.out.println("Player: " + playerName);

                    // nro7 l a5tyarat
                    chooseControl = true;
                    nameEntered = true;
                } else {
                    // lw dost cancle yrg3 l menu bbs msh sh8al
                    firstone = true;
                }
            });
        }

        // nro7 control
        if (chooseControl) {
            chooseControl(gl);
        }

        // start game
        if (!firstone && startgame) {
            gameplay(gl);
            if (isKeyPressed(KeyEvent.VK_ESCAPE)) {
                pause = !pause;
            }
        }

        // rest of menu
        if (pause && !gameover) pause(gl);
        else if (gameover) gameover(gl);
        if (!firstone && helppic) {
            help(gl);
            if (isKeyPressed(KeyEvent.VK_BACK_SPACE)) {
                helppic = false;
                firstone = true;
            }
        }


        if (lifes < 1) {
            gameover = true;
        }

        if (gameover && isKeyPressed(KeyEvent.VK_ENTER)) {
            reset();
            startagain = true;
        }

        if (startagain) {
            init(gld);
            startagain = false;
        }
    }

    public void DrawSprite(GL gl, int x, int y, int indexx, float scale) {
        GLUT glut = new GLUT();
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[indexx]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    Color c1 = new Color(0, 0, 0);

    //background
    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);
        // Turn Blending On
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    boolean soundone = false;

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

    public void gameplay(GL gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        GLUT glut = new GLUT();
        Font font = new Font("ARIAL", Font.BOLD, 50);
        DrawBackground(gl);

        try {
            if (useKeyboard) {
                handleKeyPress(gl); // keyboard move
            } else if (useMouse) {
                x = xPosition; // mouse move
                DrawSprite(gl, x, y, index, 0.8f); // رسم السلة
            }
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
        }

        t.beginRendering(500,500);
        t.setColor(c1.BLACK);
        t.draw("SCORE : " + score,2, 10);
        t.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        t.endRendering();

        t.beginRendering(400, 400);
        t.setColor(c1.BLACK);
        t.draw("LEVEL : " + level,5, 380);
        t.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        t.endRendering();

        t.beginRendering(200,200);
        t.setColor(c1.RED);
        t.draw(""+lifes,190,3);
        t.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        t.endRendering();

        DrawSprite(gl,0,76,  Chickenindex, 2);
        DrawSprite(gl,12,76,  Chickenindex, 2);
        DrawSprite(gl,24,76, Chickenindex, 2);
        DrawSprite(gl,36,76, Chickenindex, 2);
        DrawSprite(gl,48,76, Chickenindex, 2);
        DrawSprite(gl,60,76, Chickenindex, 2);
        DrawSprite(gl,72,76, Chickenindex, 2);
        DrawSprite(gl,84,76, Chickenindex, 2);
        DrawSprite(gl,92,76, Chickenindex, 2);


        Chickenindex++;
        if(Chickenindex == 13){
            Chickenindex = 10 ;
        }
        for (int i = 0; i < eggs.size(); i++) {
             if(eggs.get(i).y == 72){
                    soundFile = new File("sound3.wav");
                    try {
                    audioIn = AudioSystem.getAudioInputStream (soundFile);// getAudioInputStream(soundFile);
                    } catch (UnsupportedAudioFileException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        clip = AudioSystem.getClip();
                    } catch (LineUnavailableException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        clip.open(audioIn);
                    } catch (LineUnavailableException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    clip.start();
                }
            if(eggs.get(i).y <69){
                DrawSprite(gl, eggs.get(i).x, eggs.get(i).y, 1, 0.3f);

            }

            if(!pause && !gameover){
                eggs.get(i).y -= 2;  // البيض ينزل أسرع

            }
        }

        for (int j = 0; j < eggs.size(); j++) {

            if ((eggs.get(j).y - 5) == y && ((eggs.get(j).x > x-5)&&(eggs.get(j).x <x+5))) {
                oldscore = score;
                eggshanded++;
                if (eggshanded < 8) {
                    score += 10;
                    level = 1;
                }
                else if (eggshanded < 15) {
                    score += 20;
                    level = 2;
                } else if (eggshanded < 20) {
                    score += 30;
                    level = 3;
                } else if (eggshanded < 25) {
                    score += 40;
                    level = 4;
                } else if (eggshanded < 30) {
                    score += 50;
                    level = 5;
                } else if (eggshanded < 35) {
                    score += 30;
                    level = 6;
                } else if (eggshanded < 40) {
                    score += 40;
                    level = 7;
                } else if (eggshanded < 45) {
                    score += 50;
                    level = 8;
                } else if (eggshanded < 50) {
                    score += 60;
                    level = 9;
                }
                eggs.remove(j);
                t.beginRendering(200, 200);
                t.setColor(c1.BLACK);
                t.draw("+" + (score - oldscore), 10, 10);
                t.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                t.endRendering();
            } else if ((eggs.get(j).y - 5) == y && (!(eggs.get(j).x > x-5)||(!(eggs.get(j).x <x+5))))  {
                lifes--;
                if (lifes == 0) {
                    gameover = true;
                }
                if(lifes >0 && !gameover)
                {
                    soundFile = new File("sound2.wav");
                    try {
                    audioIn = AudioSystem.getAudioInputStream (soundFile);// getAudioInputStream(soundFile);
                    } catch (UnsupportedAudioFileException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        clip = AudioSystem.getClip();
                    } catch (LineUnavailableException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        clip.open(audioIn);
                    } catch (LineUnavailableException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    clip.start();
                }
            }
        }
    }

    int scoreeee = 0;
    int soundgameover = 0 ;
    //game over page
    public void gameover(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[3]);
        // Turn Blending On
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
        t.beginRendering(200, 200);
        t.setColor(c1.WHITE);
        scoreeee = score;
        t.draw("Score: " + scoreeee, 78, 130);
        t.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        t.endRendering();

        t.beginRendering(200,200);
        t.setColor(c1.WHITE);
        t.draw("High: " + highscore, 78 , 115);
        t.setColor(0.0f, 0.0f, 0.0f, 0.0f);
        t.endRendering();

        if(highscore < score){
            highscore = score;
        }
        if(soundgameover == 1){
            soundFile = new File("gameover.wav");
            try {
                audioIn = AudioSystem.getAudioInputStream (soundFile);// getAudioInputStream(soundFile);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException ex) {
                Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                clip.open(audioIn);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AnimGLEventListener2.class.getName()).log(Level.SEVERE, null, ex);
            }
            clip.start();
        }
        soundgameover++;
    }

     public void reset() {
        firstone = false;
        eggshanded = 0;
        level = 1;
        oldscore = 0;
        lifes = 3;
        gameover = false;
        pause = false;
        previousX = 0;
        currentX = 0;
        score = 0;
    }

    //pause game
    public void pause(GL gl) {

        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[9]); // صورة pause
        gl.glPushMatrix();

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 0); gl.glVertex3f(-1, -1, -1);
        gl.glTexCoord2f(1, 0); gl.glVertex3f( 1, -1, -1);
        gl.glTexCoord2f(1, 1); gl.glVertex3f( 1,  1, -1);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(-1,  1, -1);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);

    }

    boolean music = false;
    //home page
    public void menu(GL gl) {
        music = true;
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[7]);
        // Turn Blending On
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
        // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - .5, y / (maxHeight / 2.0) - 0.5, 0);
        gl.glScaled(0.2, 0.2, 0.2);

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();

        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);
        // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 1.0, y / (maxHeight / 2.0) + -0.5, 0);
        gl.glScaled(0.2, 0.2, 0.2);

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);

        gl.glEnd();
        gl.glPopMatrix();
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[8]);
        // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 1.5, y / (maxHeight / 2.0) + (-0.5), 0);
        gl.glScaled(0.2, 0.2, 0.7);

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void help(GL gl) {
        h = true;
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[6]);
        // Turn Blending On
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    public int getscore(){
        return highscore ;
    }
    public void sethighscore(int highscoree){
        highscore = highscoree;
    }
    public void handleKeyPress(GL gl) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (x > 0) {
                x -= 2;
                DrawSprite(gl, x, y, 0,0.8f);
                if(!gameover){
                    soundFile = new File("sound2.wav");
                    audioIn = AudioSystem.getAudioInputStream (soundFile);// getAudioInputStream(soundFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.start();
                }
            }
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (x < maxWidth - 10) {
                x += 2;
                DrawSprite(gl, x, y, 0, 0.8f);
                if(!gameover){
                    soundFile = new File("sound2.wav");
                    audioIn = AudioSystem.getAudioInputStream (soundFile);// getAudioInputStream(soundFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.start();
                }
            }
        }
        DrawSprite(gl, x, y, index,0.8f);

    }
    public BitSet keyBits = new BitSet(256);

    //handle press key function
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        // predd r restart
        if (gameover) {
            if (key == KeyEvent.VK_R) {
                reset();
                startagain = true;
            }
            //press e exit
            if (key == KeyEvent.VK_E) {
                System.exit(0);
            }
//            if (key == KeyEvent.VK_M) {
//                reset();               // إعادة كل متغيرات اللعبة الأساسية
//                firstone = true;       // نرجع للمينيو
//                gameover = false;
//                startgame = false;
//                chooseControl = false;
//                useKeyboard = false;
//                useMouse = false;
//                askName= false;
//            }
        }
        if (helppic){
            if (key == KeyEvent.VK_BACK_SPACE) {

                pause = false;
                startgame = false;
                chooseControl = false;
                gameover = false;
                useKeyboard = false;
                useMouse = false;
                firstone = true;
            }
        }

        if (key == KeyEvent.VK_ESCAPE) {
            pause = !pause;
            return;
        }

        // in pause
        if (pause) {

            // e exit
            if (key == KeyEvent.VK_E) {
                System.exit(0);
            }

            if (key == KeyEvent.VK_M) {
                pause = false;
                startgame = false;
                chooseControl = false;
                gameover = false;
                useKeyboard = false;
                useMouse = false;
                firstone = true;

            }
            return; // ⛔ يمنع أي input تاني
        }

        // ✅ باقي مفاتيح اللعبة تسجّل عادي
        keyBits.set(key);
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


    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override    public void mouseClicked(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();

        xPosition = (int) ((x / width) * 600) - 300;
        yPosition = 175 - ((int) ((y / height) * 350));
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
