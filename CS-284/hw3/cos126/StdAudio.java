/*************************************************************************
 *  Compilation:  javac StdAudio.java
 *  Execution:    java StdAudio
 *  
 *  Simple library for reading, writing, and manipulationg .wav files.
 *
 *  Limitations
 *  -----------
 *    - Does not seem to work properly when reading .wav files from .jar files.
 *    - Assumes the audio is monaural, with sampling rate of 44,100.
 *
 *************************************************************************/
package cos126;

import java.io.*;
import javax.sound.sampled.*;
import java.net.*;
import java.applet.*;

public final class StdAudio {

    private static final int BYTES_PER_SAMPLE = 2;                // 16-bit audio
    private static final int BITS_PER_SAMPLE = 16;                // 16-bit audio
    private static final double MAX_16_BIT = Short.MAX_VALUE;     // 32,767
    private static final int SAMPLE_RATE = 44100;                 // CD-quality audio
    private static final int SAMPLE_BUFFER_SIZE = 4096;


    private static SourceDataLine line;   // to play the sound
    private static byte[] buffer;         // our internal buffer
    private static int i = 0;             // number of samples currently in internal buffer


    // static initializer
    static { init(); }

    // open up an audio stram
    private static void init() {
        try {
            // 44,100 samples per second, 16-bit audio, mono, signed PCM, little Endian
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);
            
            // the internal buffer is a fraction of the actual buffer size, this choice is arbitrary
            // it gets diveded because we can't expect the buffered data to line up exactly with when
            // the sound card decides to push out its samples.
            buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE/3];
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // no sound gets made before this call
        line.start();
    }

   
    // not-instantiable
    private StdAudio() { }


    // close the audio stream
    public static final void close() {
        line.drain();
        line.stop();
    }
    
    // this writes the samples to our internal buffer and then eventually
    // to the sound card when it's full.
    public static final void play(double in) {

        // clip if outside [-1, +1]
        if (in < -1.0) in = -1.0;
        if (in > +1.0) in = +1.0;

        // convert to bytes
        short s = (short) (MAX_16_BIT * in);
        buffer[i++] = (byte) s;
        buffer[i++] = (byte) (s >> 8);   // little Endian

        // send to sound card if buffer is full        
        if (i >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            i = 0;
        }
    }

    // play an array of samples
    public static void play(double[] input) {
        for (int i = 0; i < input.length; i++) {
            play(input[i]);
        }
    }


    // return data as a double array, with values scaled to be between -1 and 1
    public static double[] read(String filename) {
        byte[] data = readByte(filename);
        int N = data.length;
        double[] d = new double[N/2];
        for (int i = 0; i < N/2; i++) {
            d[i] = ((short) (((data[2*i+1] & 0xFF) << 8) + (data[2*i] & 0xFF))) / ((double) MAX_16_BIT);
        }
        return d;
    }




    // play a wav or midi sound in the background
    public static void play(String filename) {
        URL url = null;
        try {
            File fil = new File(filename);
            if (fil.canRead()) url = fil.toURI().toURL();
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        // URL url = StdAudio.class.getResource(filename);
        if (url == null) throw new RuntimeException("audio " + filename + " not found");
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }


    // return data as a byte array
    private static byte[] readByte(String filename) {
        byte[] data = null;
        AudioInputStream ais = null;
        try {
            URL url = StdAudio.class.getResource(filename);
            ais = AudioSystem.getAudioInputStream(url);
            data = new byte[ais.available()];
            ais.read(data);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not read " + filename);
        }

        return data;
    }






    // save double array as .wav or .au file
    public static void save(double[] input, String filename) {

        // assumes 44,100 samples per second
        // use 16-bit audio, mono, signed PCM, little Endian
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        byte[] data = new byte[2 * input.length];
        for (int i = 0; i < input.length; i++) {
            int temp = (short) (input[i] * MAX_16_BIT);
            data[2*i + 0] = (byte) temp;
            data[2*i + 1] = (byte) (temp >> 8);
        }

        // now save the file
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais, format, input.length);
            if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
            }
            else if (filename.endsWith(".au") || filename.endsWith(".AU")) {
                AudioSystem.write(ais, AudioFileFormat.Type.AU, new File(filename));
            }
            else {
                throw new RuntimeException("File format not supported: " + filename);
            }
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }




    // create a note (sine wave) of the given frequency (Hz), for the given
    // duration (seconds) scaled to the given volume (amplitude)
    public static double[] note(double hz, double duration, double amplitude) {
        int N = (int) Math.round(SAMPLE_RATE * duration);
        double[] tone = new double[N+1];
        for (int i = 0; i <= N; i++)
            tone[i] = amplitude * Math.sin(2 * Math.PI * i * hz / SAMPLE_RATE);
        return tone;
    }




   /***********************************************************************
    * sample test client
    ***********************************************************************/
    public static void main(String[] args) {
        
        // 440 Hz for 1 sec
        double freq = 440.0;
        for (int i = 0; i < 44100; i++) {
            StdAudio.play(0.5 * Math.sin(2*Math.PI * freq * i / SAMPLE_RATE));
        }
        
        // scale increments
        int[] steps = { 0, 2, 4, 5, 7, 9, 11, 12 };
        for (int i = 0; i < steps.length; i++) {
            double hz = 440.0 * Math.pow(2, steps[i] / 12.0);
            StdAudio.play(note(hz, 1.0, 0.5));
        }


        // need to call this in non-interactive stuff so the program doesn't terminate
        // until all the sound leaves the speaker.
        StdAudio.close(); 

        // need to terminate a Java program with sound
        System.exit(0);
    }
}
