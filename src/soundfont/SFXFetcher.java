package soundfont;

import java.io.InputStream;

final public class SFXFetcher {
    private InputStream sfxFile;
    
    public SFXFetcher() {
        this.sfxFile = this.getClass().getResourceAsStream("sfx.sf2");
    }
    
    public InputStream getSFXFile() {
        return this.sfxFile;
    }
}