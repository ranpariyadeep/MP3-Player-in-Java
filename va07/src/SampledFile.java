import studiplayer.basic.BasicPlayer;

abstract class SampledFile extends AudioFile {
    private long duration;

    public SampledFile() {
        super();
    }

    public SampledFile(String path) {
        super(path);
    }

    public void play() {
        // play the audio file
        BasicPlayer.play(getPathname());
    }

    public void togglePause() {
        // pause or resume
        BasicPlayer.togglePause();
    }

    public void stop() {
        // stop the audio
        BasicPlayer.stop();
    }

    String formatDuration() {
        return timeFormatter(getDuration());
    }

    String formatPosition() {
        return timeFormatter(BasicPlayer.getPosition());
    }

    public static String timeFormatter(long timeInMicroSeconds) {
        BasicPlayer.getPosition();
        long seconds = (timeInMicroSeconds / 1000000);
        long minutes = timeInMicroSeconds / 60000000;
        long remainingSeconds = ((timeInMicroSeconds / 1000000) % 60);

        if (timeInMicroSeconds < 0 || timeInMicroSeconds >= 6000000000L) {
            if (timeInMicroSeconds < 0) {
                throw new RuntimeException("Invalid time value : Negative value");
            } else {
                throw new RuntimeException("Invalid time value : Time overflow");
            }
        }
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
