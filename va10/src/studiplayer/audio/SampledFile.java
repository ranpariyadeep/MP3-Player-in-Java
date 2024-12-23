package studiplayer.audio;

import studiplayer.basic.BasicPlayer;

abstract public class SampledFile extends AudioFile {
    private long duration;

    public SampledFile() {
        super();
    }

    public SampledFile(String path) throws NotPlayableException {
        super(path);
    }

    public void play() throws NotPlayableException {
        String pathname = getPathname();
        try {
            BasicPlayer.play(pathname);
        } catch (Exception e) {
            throw new NotPlayableException(pathname, "errors occur when Playing");
        }
    }

    public String formatPosition() {
        return timeFormatter(BasicPlayer.getPosition());
    }

    public void togglePause() {
        // pause or resume
        BasicPlayer.togglePause();
    }

    

    public static String timeFormatter(long timeInMicroSeconds) {
        BasicPlayer.getPosition();
        // long seconds = (timeInMicroSeconds / 1000000);
        // long minutes = timeInMicroSeconds / 60000000;
        // long remainingSeconds = ((timeInMicroSeconds / 1000000) % 60);

        if (timeInMicroSeconds < 0 || timeInMicroSeconds >= 6000000000L) {
            if (timeInMicroSeconds < 0) {
                throw new RuntimeException("Invalid time value : Negative value");
            } else {
                throw new RuntimeException("Invalid time value : Time overflow");
            }
        }
        return String.format("%02d:%02d", timeInMicroSeconds / 60000000, ((timeInMicroSeconds / 1000000) % 60));
    }
    public String formatDuration() {
        return timeFormatter(getDuration());
    }

    public void stop() {
        // stop the audio
        BasicPlayer.stop();
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
