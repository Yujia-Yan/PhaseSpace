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
  float detect(float[] frame){
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
      
      return index*fft.getBandWidth();
   
  }
}
