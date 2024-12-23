package studiplayer.audio;

import java.io.File;

abstract public class AudioFile {

    private String pathname;
    private String filename;
    private String author;
    private String title;

    public AudioFile() {
        pathname = "";
        filename = "";
        author = "";
        title = "";
    }

    public AudioFile(String path) throws NotPlayableException {

        parsePathname(path);
        parseFilename(filename);

        // create a File instance
        File file = new File(pathname);
        boolean canRead = file.canRead();
        // Check file readable or not
        if (!canRead) {
            throw new NotPlayableException(path, "can not read path !!");
        }

    }

//***********************************************************************************************   

    public void parsePathname(String path) {

        String sep = System.getProperty("file.separator");
        String seperatore = sep;

        path = path.replace("\\\\", sep);
        path = path.replace("//", sep);
        path = path.replace("\\", sep);
        path = path.replace("/", sep);
        path = path.replaceAll(sep + "{2,}", sep);
        path = path.replaceAll("\\\\\\\\", "\\\\");

// Check index of separator in path , -1 = to not exist in path      
        int index = path.indexOf(seperatore);
        int lastindex = path.lastIndexOf(seperatore);
        int index2 = path.lastIndexOf(":");
        String finaleFileName = path.substring(lastindex + 1);

//this condition for without Separator.
        if (index == -1 && index2 != 1) {
            pathname = path.trim();
            filename = path.trim();
            return;
        }

// this condition for  with separator    
        if (lastindex > -1 && index2 != 1) {
            pathname = path.trim();
            filename = path.substring(lastindex + 1).trim();
            return;
        }

// z.B C:, Z: , D: ........
        if (path.matches("[A-Za-z]:.*")) {
            path = path.substring(0, 1).toUpperCase() + path.substring(1);

            // check OS is windows or not
            if (isWindows()) {
                pathname = path;
                filename = finaleFileName.trim();
                ;
                return;
            } else {
                pathname = sep + path.charAt(0) + path.substring(2);
                filename = finaleFileName.trim();

                return;
            }
        }

    }

//**************************************************************************************************

    public String getFilename() {
        return filename;
    }

    public String getPathname() {
        return pathname;
    }

//***************************************************************************************************   
    public void parseFilename(String filename) {

        if (filename.equals("-")) {
            author = "";
            title = filename;
            return;
        }

        int index = filename.lastIndexOf(".");
        int index1 = filename.indexOf(" - ");

        if (filename.equals("") || filename.equals(" ") || filename.equals(" - ") || index == 0) {
            author = "";
            title = "";
            return;
        }

        if (index > 0 && index1 == -1) {
            author = "";
            title = filename.substring(0, index);
            return;
        }

        if (index > -1 && index1 > -1 && index > index1) {
            author = filename.substring(0, index1);
            title = filename.substring(index1 + 3, index);
            return;
        }

        if (index == 1 && index1 > 1) {
            int index3 = filename.indexOf("   -   ");
            author = filename.substring(0, index3);
            int index4 = filename.lastIndexOf("   .");
            title = filename.substring(index3 + 7, index4);
            return;
        }

    }

//***************************************************************************************************
    public String getAuthor() {
        if (author != null) {
            return author.trim();
        } else {
            return "";
        }
    }

  
    public String getTitle() {
        if (title != null) {
            return title.trim();
        } else {
            return "";
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }


//***************************************************************************************************       
    public String toString() {

        if (author.isEmpty()) {
            return title;
        } else {
            String tostring = author + " - " + title;
            return tostring;
        }

    }

    public abstract void play() throws NotPlayableException;

    public abstract String formatDuration();

    public abstract void stop();

    public abstract String formatPosition();

    public abstract void togglePause();

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
    }

}



