import processing.core.*; 

import processing.opengl.*; 
import ddf.minim.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class phaseSpace extends PApplet {

 /**
  * This sketch demonstrates how to use the <code>addListener</code> method of a <code>Recordable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also add listeners to <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. The class defined in waveform.pde implements 
  * the <code>AudioListener</code> interface and can therefore be added as a listener to <code>groove</code>.
  */



Minim minim;
AudioPlayer groove;
AudioInput in;
WaveformRenderer waveform;

public void setup()
{
  size(800 , 600, OPENGL);
PFont font;
// The font must be located in the sketch's 
// "data" directory to load successfully
font = loadFont("BrowalliaUPC-Bold-48.vlw"); 
textFont(font); 


  minim = new Minim(this);
    in = minim.getLineIn(Minim.STEREO, 512);

 // groove = minim.loadFile("groove.mp3", 512);
  waveform = new WaveformRenderer();
    in.addListener(waveform);

  //groove.addListener(waveform);
}
float scal=0.4f;
 public void mousePressed(){
   {
    if( (mouseButton == LEFT))
    scal+=1.2f;
    else scal/=1.2f;
  }
 }
public void draw()
{

   pushMatrix();
   fill(255,80); // use black with alpha 10
 // rectMode(CORNER);
  rect(0,0,width,height);
  fill(0);
    text("scale"+scal,0,0); 
  popMatrix();

beginCamera();
//rotateX(1/100f);
//rotateY(1/100f);

endCamera();
  //background(255);
  pushMatrix();
  translate(width/2,height/2);
  scale(scal);
  // see waveform.pde for an explanation of how this works
  //if(waveform!=null)
  waveform.draw();
  popMatrix();
}

public void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  // always stop Minim before exiting.
  minim.stop();
  super.stop();
}
public void mouseDragged()
{

}


// This class is a very simple implementation of AudioListener. By implementing this interface, 
// you can add instances of this class to any class in Minim that implements Recordable and receive
// buffers of samples in a callback fashion. In other words, every time that a Recordable object has 
// a new buffer of samples, it will send a copy to all of its AudioListeners. You can add an instance of 
// an AudioListener to a Recordable by using the addListener method of the Recordable. If you want to 
// remove a listener that you previously added, you call the removeListener method of Recordable, passing 
// the listener you want to remove.
//
// Although possible, it is not advised that you add the same listener to more than one Recordable. 
// Your listener will be called any time any of the Recordables you've added it have new samples. This 
// means that the stream of samples the listener sees will likely be interleaved buffers of samples from 
// all of the Recordables it is listening to, which is probably not what you want.
//
// You'll notice that the three methods of this class are synchronized. This is because the samples methods 
// will be called from a different thread than the one instances of this class will be created in. That thread 
// might try to send samples to an instance of this class while the instance is in the middle of drawing the 
// waveform, which would result in a waveform made up of samples from two different buffers. Synchronizing 
// all the methods means that while the main thread of execution is inside draw, the thread that calls 
// samples will block until draw is complete. Likewise, a call to draw will block if the sample thread is inside 
// one of the samples methods. Hope that's not too confusing!
  float prev=0;
   float prev2=0;
  float prev3=0;
 
class WaveformRenderer implements AudioListener
{

  private float[] left;
  private float[] right;
  
  WaveformRenderer()
  {
    left = null; 
    right = null;
  }
  
  public synchronized void samples(float[] samp)
  {
    left = samp;
  }
  
  public synchronized void samples(float[] sampL, float[] sampR)
  {
    left = sampL;
    right = sampR;
  }
  
  public synchronized void draw()
  {
    
      float m=0;
    // we've got a stereo signal if right or left are not null
    if ( left != null && right != null )
    {
     for ( int i = 0; i < left.length; i++ )
     {
       if(abs(left[i])>m)m=abs(left[i]);
     }
     if(m<0.1f)return;
     // noFill();
      stroke(0);
      float tmp=0;
      float tmp2=0;
      for ( int i = 0; i < left.length; i++ )
      {
        
      strokeWeight(8);
        tmp=left[i]-prev;
        tmp2=tmp-prev2;
        stroke(30,100);
        point( left[i]*200/m,4000/m*(tmp));
        //point( left[i]*200/m,2000/m*(tmp));
                stroke(30,15);

      strokeWeight(2);
        //line(200*prev/m,4000*prev2/m,2000*prev3/m, left[i]*200/m,2000*(tmp)/m,200*(tmp2)/m);
        prev2=tmp;
        prev3=tmp2;
        prev=left[i];
        
      }
      
    }

  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "phaseSpace" });
  }
}
