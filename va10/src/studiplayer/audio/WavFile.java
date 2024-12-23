package studiplayer.audio;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {

    public WavFile() {
        super();
    }

    public WavFile(String path) throws NotPlayableException {
        super(path);
        readAndSetDurationFromFile();
    }

    public void readAndSetDurationFromFile() throws NotPlayableException {
        try {
            String pathname = getPathname();
            WavParamReader.readParams(pathname);
            setDuration(computeDuration(WavParamReader.getNumberOfFrames(), WavParamReader.getFrameRate()));
        } catch (Exception e) {
            throw new NotPlayableException(getPathname(), " file doesn exist");
        }

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
