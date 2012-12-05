
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
  WaveformRenderer(PitchDetectorHPS pitch,int sampleRate,float partialFactor)
  {
    left = null; 
    right = null;
    this.pitch=pitch;
    this.sampleRate=sampleRate;
    partialCount=(1+partialCount)*partialCount/2;
  }
  
  synchronized void samples(float[] samp)
  {
    left = samp;
  }
  
  synchronized void samples(float[] sampL, float[] sampR)
  {
    freq=pitch.detect(sampL);
    println(freq);
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
     if(m<0.1)return;
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
        //normalize tmp with frequency
        point( left[i]*100/m,100/5/(m)*(tmp)/freq/PI*sampleRate);
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

