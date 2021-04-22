package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.web.HTMLEditor;
import sample.api.impl.OperationImpl;
import sample.entity.Note;
import sample.utils.DialogUtils;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Japoul
 */
public class Controller extends OperationImpl implements Initializable {

    @FXML
    public ListView<Note> noteList;
    @FXML
    public HTMLEditor content;
    /**
     * map缓存 k:id
     */
    private Map<String, Note> noteMap;
    /**
     * 当前选中item
     */
    private String selectId = null;

    @FXML
    public void newNote() {
        // 弹出创建笔记弹窗
        DialogUtils.createNote(this);
    }

    @FXML
    public void saveNote() {
        if (selectId != null) {
            Note note = noteMap.get(selectId);
            note.setContent(content.getHtmlText());
            updateNote(note);
        }
    }

    @Override
    public Note createNote(String title) {
        Note note = super.createNote(title);
        noteList.getItems().add(0, note);
        return note;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化笔记
        noteMap = initNotes().stream().collect(Collectors.toMap(Note::getId, Function.identity()));
        // 初始化笔记列表
        noteList.setItems(FXCollections.observableArrayList(noteMap.values()));
        // 点击事件
        noteList.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Note> ov, Note old_val,
                 Note new_val) -> {
                    selectId = new_val.getId();
                    content.setHtmlText(new_val.getContent());
                });
    }
}
