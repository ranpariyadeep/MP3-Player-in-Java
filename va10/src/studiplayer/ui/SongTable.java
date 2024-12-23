package studiplayer.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.SampledFile;
import studiplayer.audio.TaggedFile;

public class SongTable extends TableView<Song> {
    private ObservableList<Song> tableData;
    private PlayList playList;

    /**
     * Initialises the table with the data from the PlayList and sets table headers
     * 
     * @param playList
     */
    public SongTable(PlayList playList) {
        this.playList = playList;
        this.tableData = FXCollections.observableArrayList();
        setItems(tableData);

        TableColumn<Song, String> interpretColumn = new TableColumn<>("Artist");
        interpretColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("interpret"));
        interpretColumn.setSortable(false);
        TableColumn<Song, String> titelColumn = new TableColumn<>("Title");
        titelColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("titel"));
        titelColumn.setSortable(false);
        TableColumn<Song, String> albumColumn = new TableColumn<>("Album");
        albumColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));
        albumColumn.setSortable(false);
        TableColumn<Song, String> laengeColumn = new TableColumn<>("Duration");
        laengeColumn.setCellValueFactory(new PropertyValueFactory<Song, String>("laenge"));
        laengeColumn.setSortable(false);
        getColumns().add(interpretColumn);
        getColumns().add(titelColumn);
        getColumns().add(albumColumn);
        getColumns().add(laengeColumn);
        setEditable(false);
        refreshSongs();
    }

    /**
     * Registers an event handler for mouse clicks on a table row
     * 
     * @param handler
     */
    public void setRowSelectionHandler(EventHandler<? super MouseEvent> handler) {
        setOnMouseClicked(handler);
    }

    /**
     * Redisplays the table after changes (entries, configuration) to the play list
     */
    public void refreshSongs() {
        tableData.clear();

        playList.forEach(af -> {
            String album = (af instanceof TaggedFile) ? ((TaggedFile) af).getAlbum() : "";
            String duration = (af instanceof SampledFile) ? ((SampledFile) af).formatDuration() : "";

            tableData.add(new Song(af, af.getAuthor(), af.getTitle(), album, duration));
        });
    }

    /**
     * Selects audiofile "song" in the table
     * 
     * @param song
     */
    public void selectSong(AudioFile song) {
        AudioFile currentAudioFile = playList.currentAudioFile();

        for (int index = 0; index < tableData.size(); index++) {
            if (tableData.get(index).getAudioFile() == currentAudioFile) {
                getSelectionModel().select(index);
                break; // Exit the loop once the song is selected
            }
        }
    }

    public ObservableList<Song> getTableData() {
        return tableData;
    }
}

