import ddf.minim.AudioListener;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {


    Minim minim;
    AudioPlayer song;
    BeatDetect beat;
    BeatListener bl;

    float kickSize, snareSize, hatSize;
    float imageScale;

    PImage kickImage, snareImage, hatImage;

    PImage images [];
    int colors [];
    float mods [];
    float dmods [];
    
    int INSTANCES;

    public void settings() {
        size(1024,1024, P3D);
        pixelDensity(2);
    }

    public void setup(){
        
        INSTANCES = 32;
    minim = new Minim(this);

    song = minim.loadFile("/Users/cam/IdeaProjects/untitled2/data/out.mp3", 1024);

    // a beat detection object that is FREQ_ENERGY mode that
    // expects buffers the length of song's buffer size
    // and samples captured at songs's sample rate

    beat = new BeatDetect(song.bufferSize(), song.sampleRate());

    // set the sensitivity to 300 milliseconds
    // After a beat has been detected, the algorithm will wait for 300 milliseconds
    // before allowing another beat to be reported. You can use this to dampen the
    // algorithm if it is giving too many false-positives. The default value is 10,
    // which is essentially no damping. If you try to set the sensitivity to a negative value,
    // an error will be reported and it will be set to 10 instead.
    // note that what sensitivity you choose will depend a lot on what kind of audio
    // you are analyzing. in this example, we use the same BeatDetect object for
    // detecting kick, snare, and hat, but that this sensitivity is not especially great
    // for detecting snare reliably (though it's also possible that the range of frequencies
    // used by the isSnare method are not appropriate for the song).
  beat.setSensitivity(300);
    kickSize = snareSize = hatSize = 16;
    // make a new beat listener, so that we won't miss any buffers for the analysis
    bl = new BeatListener(beat, song);
    textFont(createFont("Helvetica", 16));
    textAlign(CENTER);

    snareImage = loadImage("/Users/cam/IdeaProjects/untitled2/data/0.png");
    hatImage= loadImage("/Users/cam/IdeaProjects/untitled2/data/1.png");
    kickImage = loadImage("/Users/cam/IdeaProjects/untitled2/data/2.png");

    images = new PImage[8];
    colors = new int[5];
    mods = new float[INSTANCES];
    dmods = new float[INSTANCES];

    colors[0] = color(42, 59, 48);
    colors[1] = color(171, 255, 209);
    colors[2] = color(235, 255, 245);
    colors[3] = color(157, 254, 255);
    colors[4] = color(39, 59, 64);

  for (int i=0; i<5; i++) {
        images[i] = loadImage("/Users/cam/IdeaProjects/untitled2/data/u_" + i + ".png");
    }

    imageScale = 0.5f;

  for (int i=0; i<INSTANCES; i++) {
        mods[i] = dmods[i] = 0.0f;
    }
    background(0,0,0);

}

    public void draw()
    {
        // println(beat.detectSize());
        background(0,0,0);



        if(!song.isPlaying()){
            for (int i=0; i<INSTANCES; i++) {
                dmods[i] = 0.f;
            }
        }
        //blendMode(NORMAL);
        //fill(0,0,0,128);
        //rect(0,0,width,height);

        // draw a green rectangle for every detect band
        // that had an onset this frame
        float rectW = width / beat.detectSize();
        //for(int i = 0; i < beat.detectSize(); ++i)
        //{
        //  // test one frequency band for an onset
        //  if ( beat.isOnset(i) )
        //  {
        //    fill(0,200,0);
        //    rect( i*rectW, 0, rectW, height);
        //  //  image(kickImage,i*rectW-kickImage.width/2,(float)height/2-kickImage.height/2);

        //  }
        //}

        // draw an orange rectangle over the bands in
        // the range we are querying
        int lowBand = 5;
        int highBand = 15;
        // at least this many bands must have an onset
        // for isRange to return true
        int numberOfOnsetsThreshold = 4;


        if ( beat.isKick() ) kickSize = INSTANCES;
        if ( beat.isSnare() ) snareSize = INSTANCES;
        if ( beat.isHat() ) hatSize = INSTANCES;
        blendMode(NORMAL);


        translate(width/2, height/2);


        int b = beat.detectSize();

        for (int i = 0; i < b; ++i)
        {

            if ( beat.isOnset(i)) {
                mods[i] = 1.0f;
            } else {
                mods[i] += (dmods[i] - mods[i])/((float)(i+1));
            }
            tint(colors[i%5]*2);
            //tint(255);
            pushMatrix();
            float dir = i%2*2-1;
            rotateZ((frameCount/(i%50+100.0f))*dir);
            float s = sin(frameCount/(i%50+100.0f) + i) * mods[i];

            float imageW = images[i%5].width/2;
            float imageH = images[i%5].height/2;
            image(images[i%5], -imageW/2*s, -imageH/2*s, imageW*s, imageH*s);
            popMatrix();
        }
    }

    public void mousePressed(){
        song.rewind();
        song.play();

        for (int i=0; i<INSTANCES; i++) {
            dmods[i] = 0.5f;
        }
    }


}

