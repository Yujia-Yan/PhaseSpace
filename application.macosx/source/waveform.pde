
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
  
  synchronized void samples(float[] samp){
    left=samp;
  }

  synchronized void samples(float[] sampL, float[] sampR)
  {
    
   
    freq=freq*0.2+0.8*pitch.detect(sampL);
   
    //println(freq);
    left = sampL;
    right = sampR;
  }
  
  synchronized void draw()
  {
    
      float m=0;
    // we've got a stereo signal if right or left are not null
    if ( left != null && right != null )
    {
     for ( int i = 0; i < left.length; i++ )
     {
       if(abs(left[i])>m)m=abs(left[i]);
     }
     if(m<0.01)return;
     // noFill();
      stroke(0);
      float tmp=0;
      float tmp2=0;
      for ( int i = 0; i < left.length; i++ )
      {
        
      strokeWeight(10);
        tmp=left[i]-prev;
        tmp2=tmp-prev2;
       // stroke(30,60);
        stroke(30,30);
        //normalize tmp with frequency
        //
        //point( left[i]*600/m,200/(m) *(tmp)/PI*sampleRate/freq);
       // println(200*tmp2/m/freq/PI*sampleRate/freq/PI*sampleRate);
        point(left[i]*600/m,200*(tmp)/m/freq/PI*sampleRate);
        //point( left[i]*200/m,2000/m*(tmp));
                //stroke(15,15);
       // color(0,255,0);
     // strokeWeight(2);
      //  line(600*prev/m,200*prev2/m/freq*sampleRate/PI,200*prev3/m/freq/PI*sampleRate/freq/PI*sampleRate, left[i]*600/m,200*(tmp)/m/freq/PI*sampleRate,200*tmp2/m/freq/PI*sampleRate/freq/PI*sampleRate);
        
      //  line(600*prev/m,200*prev2/m/freq*sampleRate/PI,0, left[i]*600/m,200*(tmp)/m/freq/PI*sampleRate,0);
        prev2=tmp;
        prev3=tmp2;
        prev=left[i];
        
      }
      
    }

  }
  

}

