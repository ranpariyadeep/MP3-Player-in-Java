
import studiplayer.basic.WavParamReader;

class WavFile extends SampledFile {
 
   

    public WavFile() {
        super();
    }

    public WavFile(String path) {
        super(path);
        readAndSetDurationFormFile();
    }

    public void readAndSetDurationFormFile() {
        WavParamReader.readParams(getPathname());
        long numberOfFrames;
        float frameRate;
        long duration;

        numberOfFrames = WavParamReader.getNumberOfFrames();
        frameRate = WavParamReader.getFrameRate();
        duration = computeDuration(numberOfFrames, frameRate);
        setDuration(duration);

    }

    public String toString() {
      
        return getAuthor() + " - " + getTitle() + " - " + formatDuration();
    }

    public static long computeDuration(long numberOfFrames, float frameRate) {
        if (numberOfFrames > 0 || frameRate > 0) {
            return (long) ((numberOfFrames / frameRate) * 1000000);
        }
        return 0;

    }

}
