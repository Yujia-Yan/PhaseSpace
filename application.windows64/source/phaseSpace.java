import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 

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

public class PhaseSpace extends PApplet {

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
    in = minim.getLineIn(Minim.STEREO, 1024,44100);
PitchDetectorHPS pitch=new PitchDetectorHPS(1024,44100,5);
 // groove = minim.loadFile("groove.mp3", 512);
  waveform = new WaveformRenderer(pitch,8000);
    in.addListener(waveform);

  //groove.addListener(waveform);
}
float scal=0.5f;
 public void keyPressed(){
   {
    if( (key == '+' || key=='='))
    scal+=1.2f;
    if( (key == '-' || key=='_'))
    scal/=1.2f;
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


class PitchDetectorHPS{
  
  FFT fft;
  int sampleRate;
  int fftLength;
  int harmonicSize;
  float[][] step;
  PitchDetectorHPS(int fftLength,int sampleRate,int harmonicSize){
    fft=new FFT(fftLength,sampleRate);
    this.sampleRate=sampleRate;
    this.fftLength=fftLength;
    this.harmonicSize=harmonicSize;
    step=new float[harmonicSize][];
    for(int i=0;i<harmonicSize;i++){
      step[i]=new float[fftLength];
    }
   // fft.window(fft.HAMMING);
  }
  public float detect(float[] frame){
    fft.forward(frame);
    //downsample
      for(int i=0;i<fftLength;i++){
        for(int j=1;j<=harmonicSize;j++){
           if(i%j==0)( step[j-1])[i/j]=fft.getBand(i);
        }
      }
    //HSP
      int index=0;
      float max;
      max=0;
      float tmp;;
      for(int i=0;i<fftLength;i++){
        tmp=1;
        for(int j=0;j<harmonicSize;j++){
          tmp*=step[j][i];
        }
        //println(tmp);
        if(tmp>max){
          max=tmp;
          index=i;
        }
      }
       if(index==0)return 1.0f;
      return index*fft.getBandWidth();
   
  }
}

class WaveformRenderer implements AudioListener
{
  float prev=0;
   float prev2=0;
  float prev3=0;
 float freq=0;
  private float[] left;
  private float[] right;
  float partialCount;
  int sampleRate;
  PitchDetectorHPS pitch;
  WaveformRenderer(PitchDetectorHPS pitch,int sampleRate)
  {
    left = null;
    right = null;
    this.pitch=pitch;
    this.sampleRate=sampleRate;
  }
  
  public synchronized void samples(float[] samp){
    left=samp;
  }

  public synchronized void samples(float[] sampL, float[] sampR)
  {
    
   
    freq=freq*0.8f+0.2f*pitch.detect(sampL);
   
    //println(freq);
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
     if(m<0.01f)return;
     // noFill();
      stroke(0);
      float tmp=0;
      float tmp2=0;
      for ( int i = 0; i < left.length; i++ )
      {
        
      strokeWeight(6);
        tmp=left[i]-prev;
        tmp2=tmp-prev2;
       // stroke(30,60);
        stroke(15,15);
        //normalize tmp with frequency
        //
        point( left[i]*600/m,100/(m) *(tmp)/PI*sampleRate/freq);
        //point( left[i]*200/m,2000/m*(tmp));
                //stroke(15,15);
       // color(0,255,0);
     // strokeWeight(2);
        //line(600*prev/m,100*prev2/m/freq*sampleRate/PI,200*prev3/m/freq/PI*sampleRate/freq/PI*sampleRate, left[i]*600/m,100*(tmp)/m/freq/PI*sampleRate,200*tmp2/m/freq/PI*sampleRate/freq/PI*sampleRate);
        
        //line(600*prev/m,100*prev2/m/freq*sampleRate/PI,0, left[i]*600/m,100*(tmp)/m/freq/PI*sampleRate,0);
        prev2=tmp;
        prev3=tmp2;
        prev=left[i];
        
      }
      
    }

  }
  

}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PhaseSpace" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
