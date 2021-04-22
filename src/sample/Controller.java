package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import sample.utils.DialogUtils;
import sample.utils.DomXmlUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Operation, Initializable {

    @FXML
    public void newNote() {
        // 弹出创建笔记弹窗
        DialogUtils.CreateNote(this);
    }

    @Override
    public void createNote(String title) {
        try {
            Note note = new Note();
            note.setTitle(title);
            DomXmlUtils.appendXML(note);
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.warn(getExceptionMsg(e));
        }
    }

    /**
     * 获取异常的提示信息
     * @param e Exception
     * @return msg
     */
    private String getExceptionMsg(Exception e) {
        return e.getMessage() == null? e.getClass().getName(): e.getMessage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("初始化方法");
    }
}
