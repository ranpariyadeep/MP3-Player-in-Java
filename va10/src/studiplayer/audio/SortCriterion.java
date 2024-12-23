package studiplayer.audio;

public enum SortCriterion {
    DEFAULT("Default"), AUTHOR("Author"), TITLE("Title"), ALBUM("Album"), DURATION("Duration");

    private String displayName;

    SortCriterion(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
