package studiplayer.audio;

public class NotPlayableException extends Exception {

    private String pathname;

    public NotPlayableException(String pathname, String msg) {
        super(String.format("%s%s", pathname, msg));
        this.pathname = pathname;
    }

    public NotPlayableException(String pathname, Throwable t) {
        super(t);
        this.pathname = pathname;

    }

    public NotPlayableException(String pathname, String msg, Throwable t) {
        super(msg, t);
        this.pathname = pathname;

    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("studiplayer.audio.NotPlayableException{");
        builder.append("pathname='").append(pathname).append('\'');
        builder.append(", message='").append(getMessage()).append('\'');
        builder.append(", cause='").append(getCause() != null ? getCause().toString() : "null").append('\'');
        builder.append('}');
        return builder.toString();
    }

    public String getPathname() {
        return pathname;
    }

}
