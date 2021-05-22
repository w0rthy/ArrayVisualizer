package utils;

import java.io.InputStream;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JOptionPane;

import main.ArrayVisualizer;
import soundfont.SFXFetcher;
import soundfont.SFXFetcher;
import templates.JErrorPane;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class Sounds {
    private int[] array;
    
    private ArrayVisualizer ArrayVisualizer;
    
    private Thread AudioThread;
    
    private Highlights Highlights;
    
    private Synthesizer synth;
    private MidiChannel[] channels;
    
    private volatile int noteDelay;
    
    private volatile boolean SOUND;
    private boolean MIDI;
    private int NUMCHANNELS; //Number of Audio Channels
    private double PITCHMIN; //Minimum Pitch
    private double PITCHMAX; //Maximum Pitch
    private double SOUNDMUL;
    private boolean SOFTERSOUNDS;
    
    final private int REVERB = 91;
    
    public Sounds(int[] array, ArrayVisualizer arrayVisualizer) {
        this.array = array;
        this.ArrayVisualizer = arrayVisualizer;
        this.Highlights = ArrayVisualizer.getHighlights();
        
        this.SOUND = true;
        this.MIDI = true;
        this.NUMCHANNELS = 16;
        this.PITCHMIN = 25d;
        this.PITCHMAX = 105d;
        this.SOUNDMUL = 1d;
        
        this.noteDelay = 1;
        
        try {
            MidiSystem.getSequencer(false);
            this.synth = MidiSystem.getSynthesizer();
            this.synth.open();
        } catch (MidiUnavailableException e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + ": The MIDI device is unavailable, possibly because it is already being used by another application. Sound is disabled.");
        }
        
        SFXFetcher sfxFetcher = new SFXFetcher();
        InputStream stream = sfxFetcher.getSFXFile();
        try {
            this.synth.loadAllInstruments(MidiSystem.getSoundbank(stream));
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }
        finally {
            try {
                stream.close();
            } catch (Exception e) {
                JErrorPane.invokeErrorMessage(e);
            }
        }
        this.channels = new MidiChannel[this.NUMCHANNELS];
        
        for(int i = 0; i < this.NUMCHANNELS; i++) {
            this.channels[i] = this.synth.getChannels()[i];
            //this.channels[i].programChange(this.synth.getLoadedInstruments()[197].getPatch().getProgram());
            this.channels[i].programChange(this.synth.getLoadedInstruments()[16].getPatch().getProgram()); // MIDI Instrument 16 is a Rock Organ.
            this.channels[i].setChannelPressure(1);
        }
        if(this.channels[0].getProgram() == -1) {
            JOptionPane.showMessageDialog(null, "Could not find a valid MIDI instrument. Sound is disabled.");
        }
        
        this.AudioThread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    for(MidiChannel channel : channels) {
                        channel.allNotesOff();
                    }
                    if(SOUND == false || MIDI == false || JErrorPane.errorMessageActive) {
                        continue;
                    }

                    int noteCount = Math.min(Highlights.getMarkCount(), NUMCHANNELS);
                    int voice = 0;

                    for(int i : Highlights.highlightList()) {
                        try {
                            if(i != -1) {
                                int currentLen = ArrayVisualizer.getCurrentLength();

                                //PITCH
                                double pitch = Sounds.this.array[Math.min(Math.max(i, 0), currentLen - 1)] / (double) currentLen * (PITCHMAX - PITCHMIN) + PITCHMIN;
                                int pitchmajor = (int) pitch;
                                int pitchminor = (int)((pitch-((int)pitch))*8192d)+8192;

                                int vel = (int) (Math.pow(PITCHMAX - pitchmajor, 2d) * (Math.pow(noteCount, -0.25)) * 64d * SOUNDMUL) / 2; //I'VE SOLVED IT!!

                                if(SOUNDMUL >= 1 && vel < 256) {
                                    vel *= vel;
                                }

                                channels[voice].noteOn(pitchmajor, vel);
                                channels[voice].setPitchBend(pitchminor);
                                channels[voice].controlChange(REVERB, 10);
                                
                                if((++voice % Math.max(noteCount, 1)) == 0)
                                    break;
                            }
                        }
                        catch (Exception e) {
                            JErrorPane.invokeErrorMessage(e);
                        }
                    }
                    try {
                        for(int i = 0; i < Sounds.this.noteDelay; i++) {
                            sleep(1);
                        }
                    } catch(Exception e) {
                        JErrorPane.invokeErrorMessage(e);
                    }
                }
            }
        };
    }
    
    public synchronized void toggleSounds(boolean val) {
        this.SOUND = val;
    }
    
    public synchronized void toggleSound(boolean val) {
        this.MIDI = val;
    }
    
    //Double check logic
    public void toggleSofterSounds(boolean val) {
        this.SOFTERSOUNDS = val;
        
        if(this.SOFTERSOUNDS) this.SOUNDMUL = 0.01;
        else                  this.SOUNDMUL = 1;
    }
    
    public double getVolume() {
        return this.SOUNDMUL;
    }
    public void changeVolume(double val) {
        this.SOUNDMUL = val;
    }
    
    public void changeNoteDelayAndFilter(int noteFactor) {
        if(noteFactor != this.noteDelay) {
            if(noteFactor > 1) {
                this.noteDelay = noteFactor;
                this.SOUNDMUL = 1d / noteFactor;
            }
            //Double check logic
            else {
                this.noteDelay = 1;
                
                if(this.SOFTERSOUNDS) this.SOUNDMUL = 0.01;
                else                  this.SOUNDMUL = 1;
            }
        }
    }
    
    public void startAudioThread() {
        AudioThread.start();
    }
}