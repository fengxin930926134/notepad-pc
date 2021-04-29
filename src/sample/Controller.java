package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
 * 控制层
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
        } else {
            DialogUtils.warn("未选择笔记！");
        }
    }

    @FXML
    public void setNotice() {
        if (noteMap.size() > 0) {
            DialogUtils.setRemind(this, noteList.getItems());
        } else {
            DialogUtils.warn("还未添加笔记！");
        }
    }

    @Override
    public Note createNote(String title) {
        Note note = super.createNote(title);
        noteMap.put(note.getId(), note);
        noteList.getItems().add(0, note);
        return note;
    }

    @Override
    public void deleteNote(String id) {
        super.deleteNote(id);
        noteList.getItems().remove(noteMap.remove(id));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化笔记
        noteMap = initNotes().stream().collect(Collectors.toMap(Note::getId, Function.identity()));
        // 初始化笔记列表
        noteList.setItems(FXCollections.observableArrayList(noteMap.values()));
        // 添加事件拦截器
        noteList.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            // 获取选中item
            Note selectedItem = noteList.getSelectionModel().getSelectedItem();
            // 单击操作
            if (event.getButton() == MouseButton.PRIMARY) {
                selectId = selectedItem.getId();
                content.setHtmlText(selectedItem.getContent());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // 右击判断
                Node node = event.getPickResult().getIntersectedNode();
                // 给node对象添加菜单
                ContextMenu contextMenu = new ContextMenu();
                MenuItem deleteItem = new MenuItem("Delete");
                MenuItem stopTaskItem = new MenuItem("Stop Notice");
                deleteItem.setOnAction(e -> deleteNote(selectedItem.getId()));
                stopTaskItem.setOnAction(e -> closeNotice(selectedItem.getId()));
                contextMenu.getItems().add(deleteItem);
                contextMenu.getItems().add(stopTaskItem);
                contextMenu.show(node, javafx.geometry.Side.BOTTOM,
                        200 - 50, -10);
            }
        });
        // 读取通知
        readNoticeByToday();
    }

    @FXML
    public void exit() {
        MinWindow.getInstance().exit();
    }

    @FXML
    public void about() {
        DialogUtils.message("关于", "QQ:930926134");
    }
}
