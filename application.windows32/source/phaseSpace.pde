/**
  * This sketch demonstrates how to use the <code>addListener</code> method of a <code>Recordable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also add listeners to <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. The class defined in waveform.pde implements 
  * the <code>AudioListener</code> interface and can therefore be added as a listener to <code>groove</code>.
  */

import ddf.minim.*;
import ddf.minim.analysis.*;
Minim minim;
AudioPlayer groove;
AudioInput in;
WaveformRenderer waveform;

void setup()
{
  size(800 , 600, OPENGL);
PFont font;
// The font must be located in the sketch's 
// "data" directory to load successfully
font = loadFont("BrowalliaUPC-Bold-48.vlw"); 
textFont(font); 


  minim = new Minim(this);
    in = minim.getLineIn(Minim.STEREO, 1024,44100);
PitchDetectorHPS pitch=new PitchDetectorHPS(1024,44100,8);
 // groove = minim.loadFile("groove.mp3", 512);
  waveform = new WaveformRenderer(pitch,44100,1);
    in.addListener(waveform);

  //groove.addListener(waveform);
}
float scal=0.4;
 void mousePressed(){
   {
    if( (mouseButton == LEFT))
    scal+=1.2f;
    else scal/=1.2f;
  }
 }
void draw()
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

void stop()
{
  // always close Minim audio classes when you are done with them
  groove.close();
  // always stop Minim before exiting.
  minim.stop();
  super.stop();
}
void mouseDragged()
{

}


