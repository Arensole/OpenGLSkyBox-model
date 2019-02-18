import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.texture.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.ArrayList;
import java.util.TimerTask;
import java.math.*;
import com.sun.opengl.util.GLUT;
import javax.media.opengl.glu.*;
import java.util.Random;


/**
 * HeightMap.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class HeightMapSnS implements GLEventListener, KeyListener {
    int mapSize = 1024; 
       double xView = 0f, zView = 0f, aView = 45, yView = 0;
       double xViews = 0f, zViews = 0f, aViews = 45, yViews = 0;
byte pHeightMap[];
double x1,x2,z1,z2,y1,y2,y3,y4,y,ymax,ymin,yks;
int x,z;
int stepSize = 16; 
double deg=0;
double degv=0;
double degcam=0;
double degs=0;
double lxs = 0;
double lzs = -1;
double lx = 0;
double lz = -1;
double cx = 50;
double ly = 4;
int xx1,zz1 = 512;
double Scale = 2;
int n;
int[] check;



public Texture TextureSet = null;
public Texture Sky = null;
public Texture Tree = null;

    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        
        GLCanvas canvas = new GLCanvas();
canvas.setFocusable(true);

        canvas.addGLEventListener(new HeightMapSnS());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        canvas.setFocusable(true);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    
    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));
check = new int[20];
for (int i=0; i<20; i++)

    check[i]=(int)(Math.random()*4096);
 
        GL gl = drawable.getGL();
        drawable.addKeyListener(this);
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        
 
        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
      gl.glClearColor(.9f, .9f, 1, 0);

        gl.glShadeModel(GL.GL_SMOOTH); 
        gl.glEnable(GL.GL_FOG);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
gl.glEnable(GL.GL_NORMALIZE);
		gl.glFogfv(GL.GL_FOG_COLOR, new float[] {.9f,.9f,1,0}, 0);
                gl.glFogf(GL.GL_FOG_DENSITY, .025f);
        	gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL.GL_GREATER,0);
    }
public void keyPressed(KeyEvent e) {
 if (e.getKeyCode() == KeyEvent.VK_UP){
     if (degcam>0)
      {
      degcam -= 0.5;
 
      }
  }
  else if (e.getKeyCode() == KeyEvent.VK_DOWN)
  {
    if (degcam<+10)
      {
      degcam += 0.5;
 
      }
  }
  else if (e.getKeyCode() == KeyEvent.VK_LEFT)
  {
      if (deg<0.4)
      {
      deg += 0.02;
     
      lx = Math.sin(deg);
      lz= -Math.cos(deg);
     
      }
  }
  else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
      {
          if (deg>-0.4)
      {
      deg -= 0.02;
      lx = Math.sin(deg);
      lz= -Math.cos(deg);
         
      }
  }
  else if (e.getKeyCode() == 'S')
  {
     zViews -= lzs/2;
 xViews -= lxs/2;
     zView += lzs/2;
 xView += lxs/2;
    
  }
  else if (e.getKeyCode() == 'W')
      {
     zViews += lzs/2;
  xViews += lxs/2;
      zView -= lzs/2;
 xView -= lxs/2;
  }
  
   else if (e.getKeyCode() == 'D')
  {
   
      degs+=4;
    lxs = -Math.sin(degs/(180/Math.PI));
      lzs= -Math.cos(degs/(180/Math.PI));
      if (deg==-360) 
          deg = 0;
  }
  else if (e.getKeyCode() == 'A')
      {
    
           degs-=4;
           lxs = -Math.sin(degs/(180/Math.PI));
      lzs= -Math.cos(degs/(180/Math.PI));
      if (deg==360) 
          deg = 0;
  }
  else if (e.getKeyCode() == 'R')
  {
      if (yViews<6){
      yViews+=0.1;
      yView-=0.1;
      }
  }
  else if (e.getKeyCode() == 'F')
      {
          if (yViews>0){
    yViews-=0.1;
    yView+=0.1;
          }
  }
    
  
      
  }
  

  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {} 
  
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();

     
        GLU glu = new GLU();
TextureSet = TerrainLoader.load ("Trava.png");
Sky = TerrainLoader.load ("Sky.png");
Tree = TerrainLoader.load ("Tree.png");
pHeightMap = TerrainLoader.load("Terrain.data", mapSize);
        if (height <= 0) { // avoid a divide by zero error!
        
            height = 1;
        }
        final float h = (float) width / (float) height;
           
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
       ymax=-999;
       ymin=999;
       y=0;
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

    }
    
double getHeight(int x, int z) {
int mapX = x % mapSize;
int mapZ = z % mapSize;
return (pHeightMap[mapX + (mapZ * mapSize)] & 0xFF);
}


    public void display(GLAutoDrawable drawable) {
       long millis = System.currentTimeMillis();
	int cycle = 3600;
        degv = millis % cycle * 360d / cycle;
     
           GL gl = drawable.getGL();
    GLU glu = new GLU();
      GLUquadric quadric = glu.gluNewQuadric();
    
               gl.glMatrixMode(GL.GL_PROJECTION);
	gl.glLoadIdentity();
        glu.gluPerspective(aView, (double) -4 / 3, 1, 60);
        glu.gluLookAt(0, 0, 0, lx, 0, lz, 0, 1, 0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
 
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
     gl.glEnable(GL.GL_DEPTH_TEST);


   
        gl.glLoadIdentity();
           
gl.glPushMatrix();
			gl.glColor3d(1,1,1);
			glu.gluQuadricTexture(quadric,true);
                  
			gl.glBindTexture(GL.GL_TEXTURE_2D, Sky.getTextureObject());
			gl.glRotated(90,1,0,0);
			glu.gluSphere(quadric, 60, 20, 20);
		gl.glPopMatrix();
        
gl.glRotated(deg, 0, 1, 0);
gl.glRotated(degcam, 1, 0, 0);
 if (xView>cx) xView=xView-2*cx;
 if (xView<-cx) xView=xView+2*cx;
  if (zView>cx) zView=zView-2*cx;
 if (zView<-cx) zView=zView+2*cx;
 gl.glPushMatrix();
 gl.glTranslated(xView-100, yView+3, zView-110);
 
 gl.glPushMatrix();
 if (xViews>cx) xViews=xViews-2*cx;
 if (xViews<-cx) xViews=xViews+2*cx;
  if (zViews>cx) zViews=zViews-2*cx;
 if (zViews<-cx) zViews=zViews+2*cx;

  gl.glTranslated(xViews+100, yViews-3, zViews+100);
 gl.glRotated(8*degv,0,1,0);


OBJmodel Object3 = new OBJmodel("helicop_propel.obj","gimp1.png",gl);
Object3.drawModel(gl);


  gl.glPopMatrix();

gl.glPushMatrix();
 if (xViews>cx) xViews=xViews-2*cx;
 if (xViews<-cx) xViews=xViews+2*cx;
  if (zViews>cx) zViews=zViews-2*cx;
 if (zViews<-cx) zViews=zViews+2*cx;
  gl.glTranslated(xViews+100, yViews-3, zViews+100);
gl.glRotated(degs,0,1,0);
 OBJmodel Object1 = new OBJmodel("helicop_corpus.obj","index1.png",gl);
Object1.drawModel(gl);
OBJmodel Object2 = new OBJmodel("helicop_glasses.obj","keks.png",gl);
Object2.drawModel(gl);

gl.glPopMatrix();

 
 
 
  
gl.glPushMatrix();

     
 gl.glPushMatrix();

     
      for (int i=0; i<3 ;i+=1)
          
      for (int j=0; j<3 ;j+=1)    
      {
          n=0;
 for (x = i*mapSize; x < (i+1)*mapSize; x+=stepSize)
 for (z = j*mapSize; z < (j+1)*mapSize; z+=stepSize)
 {
n+=1;
 y1 = -getHeight(x,z)/16;
 y2 = -getHeight(x+stepSize,z)/16;
 y3 = -getHeight(x+stepSize,z+stepSize)/16;
 y4 = -getHeight(x,z+stepSize)/16;
 x1 = 100*x/mapSize-50;
 x2 = 100*(x+16)/mapSize-50;
 z1 = 100*z/mapSize-50;
 z2 = 100*(z+16)/mapSize-50;


for (int o=0; o<20; o++)
if (n==check[o])
{
gl.glPushMatrix();
    gl.glBindTexture(GL.GL_TEXTURE_2D, Tree.getTextureObject());
   gl.glBegin(GL.GL_QUADS);
       gl.glTexCoord2d(1,1); gl.glVertex3d(-Scale*Math.cos(deg)+x1,y2,-Scale*Math.sin(deg)+z1);
      gl.glTexCoord2d(1,0); gl.glVertex3d(-Scale*Math.cos(deg)+x1,Scale+y2,-Scale*Math.sin(deg)+z1);
      gl.glTexCoord2d(0,0); gl.glVertex3d(x1+Scale*Math.cos(deg),Scale+y2,-Scale*Math.sin(-deg)+z1);
      gl.glTexCoord2d(0,1); gl.glVertex3d(x1+Scale*Math.cos(deg),y2,-Scale*Math.sin(-deg)+z1);
  
   gl.glEnd();
gl.glPopMatrix();
}
}

for (x = i*mapSize; x < (i+1)*mapSize; x+=stepSize)
 for (z = j*mapSize; z < (j+1)*mapSize; z+=stepSize)
 {

 y1 = -getHeight(x,z)/16;
 y2 = -getHeight(x+stepSize,z)/16;
 y3 = -getHeight(x+stepSize,z+stepSize)/16;
 y4 = -getHeight(x,z+stepSize)/16;
 x1 = 100*x/mapSize-50;
 x2 = 100*(x+16)/mapSize-50;
 z1 = 100*z/mapSize-50;
 z2 = 100*(z+16)/mapSize-50;
if (y1>ymax)
    ymax=y1;
if (y1<ymin)
    ymin=y1;
yks=ymax-ymin;

gl.glPushMatrix();
 gl.glBindTexture(GL.GL_TEXTURE_2D, TextureSet.getTextureObject());
gl.glBegin(GL.GL_TRIANGLES);
 gl.glColor3d((y1-ymin)/yks, (y1-ymin)/yks, (y1-ymin)/yks);
 gl.glTexCoord2d(0, 0); gl.glVertex3d(x1, y1, z1);
 gl.glColor3d((y2-ymin)/yks, (y2-ymin)/yks, (y2-ymin)/yks);
 gl.glTexCoord2d(1, 0); gl.glVertex3d(x2, y2, z1);
 gl.glColor3d((y3-ymin)/yks, (y3-ymin)/yks, (y3-ymin)/yks);
 gl.glTexCoord2d(1, 1); gl.glVertex3d(x2, y3, z2);
 gl.glColor3d((y3-ymin)/yks, (y3-ymin)/yks, (y3-ymin)/yks);
 gl.glTexCoord2d(1, 1); gl.glVertex3d(x2, y3, z2);
 gl.glColor3d((y4-ymin)/yks, (y4-ymin)/yks, (y4-ymin)/yks);
 gl.glTexCoord2d(0, 1); gl.glVertex3d(x1, y4, z2);
 gl.glColor3d((y1-ymin)/yks, (y1-ymin)/yks, (y1-ymin)/yks);
 gl.glTexCoord2d(0, 0); gl.glVertex3d(x1, y1, z1);
 }
  gl.glEnd();


       

      }


      
gl.glPopMatrix();
 

 gl.glPopMatrix();

        gl.glFlush();
    }
   
    
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
