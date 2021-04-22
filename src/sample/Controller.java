package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import sample.api.impl.OperationImpl;
import sample.utils.DialogUtils;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends OperationImpl implements Initializable {

    @FXML
    public void newNote() {
        // 弹出创建笔记弹窗
        DialogUtils.CreateNote(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("初始化方法");
    }
}
