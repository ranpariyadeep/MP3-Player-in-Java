package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {

    public int compare(AudioFile o1, AudioFile o2) {
        if (o1.equals(null) || o2.equals(null)) {
            throw new NullPointerException("One or both AudioFiles are null");
        }

        int sume = 0;
        if (o1 instanceof SampledFile) {

            sume += 1;
        }
        if (o2 instanceof SampledFile) {

            sume += 2;
        }

        switch (sume) {
        case 1:
            return 1;
        case 2:
            return -1;
        case 3:
            SampledFile sampledFile1 = (SampledFile) o1;
            SampledFile sampledFile2 = (SampledFile) o2;

            return Long.compare(sampledFile1.getDuration(), sampledFile2.getDuration());
        default:
            return 0;
        }

    }
}