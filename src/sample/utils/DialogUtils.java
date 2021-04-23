package sample.utils;

import com.sun.glass.ui.Screen;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringEscapeUtils;
import sample.Main;
import sample.api.Operation;
import sample.entity.Note;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 弹窗工具类
 */
public class DialogUtils {

    /**
     * 信息框
     *
     * @param title   标题
     * @param content 内容
     */
    public static void message(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
        alert.setTitle("");
        alert.setHeaderText(title);
        alert.show();
    }

    /**
     * 警告
     *
     * @param content 警告内容
     */
    public static void warn(String content) {
        new Alert(Alert.AlertType.WARNING, content, new ButtonType[]{ButtonType.CLOSE}).show();
    }

    /**
     * 通知
     *
     * @param content 通知内容
     */
    public static void notice(String content) {
        new Tips(content).show();
        System.out.println("执行通知完成...");
    }

    /**
     * 创建笔记弹窗
     *
     * @param operation 操作
     */
    public static void createNote(Operation operation) {
        new Create(operation).show();
    }

    /**
     * 设置定时通知弹窗
     *
     * @param noteList 笔记列表
     */
    public static void setRemind(Operation operation, List<Note> noteList) {
        new SetRemind(operation, noteList).show();
    }

    /**
     * 设置提醒弹窗
     */
    private static class SetRemind extends Stage {

        int width = 285;
        int height = 220;
        int itemWidth = 186;

        SetRemind(Operation operation, List<Note> noteList) {
            FlowPane pane = new FlowPane();
            // 内边距
            pane.setPadding(new Insets(20, 20, 20, 20));
            // 下拉列表
            final ChoiceBox<Note> cb = new ChoiceBox<>(
                    FXCollections.observableArrayList(noteList));
            cb.setMinWidth(itemWidth);
            cb.setMaxWidth(itemWidth);
            // 日期选择器
            DatePicker checkInDatePicker = new DatePicker(LocalDate.now());
            javafx.scene.control.TextField time = new javafx.scene.control.TextField();
            time.setPromptText("时间：hh:mm:ss");
            javafx.scene.control.TextField cycle = new javafx.scene.control.TextField();
            cycle.setPromptText("触发周期：天");
            javafx.scene.control.Button sbm = new javafx.scene.control.Button("保存设置");
            // 格子
            GridPane grid = new GridPane();
            grid.setVgap(15);
            grid.setHgap(15);
            grid.setPadding(new Insets(5, 5, 5, 5));
            grid.add(new Label("笔记: "), 0, 0);
            grid.add(cb, 1, 0);
            grid.add(new Label("日期: "), 0, 1);
            grid.add(checkInDatePicker, 1, 1);
            grid.add(new Label("时间: "), 0, 2);
            grid.add(time, 1, 2);
            grid.add(new Label("周期: "), 0, 3);
            grid.add(cycle, 1, 3);
            grid.add(sbm, 0, 4, 2, 1);
            // 保存
            sbm.setOnAction(event -> {
                if (cb.getValue() == null) {
                    DialogUtils.warn("请选择需要提醒的笔记！");
                }
                // 读取设置数据
                Note note = cb.getValue();
                note.setRemindDate(checkInDatePicker.getValue());
                if (VerifyUtils.verify(time.getText(), VerifyUtils.IS_TIME)) {
                    note.setRemindTime(LocalTime.parse(time.getText()));
                } else {
                    DialogUtils.warn("提醒时间不能为空！");
                    return;
                }
                if (cycle.getText() != null && VerifyUtils.verify(cycle.getText(), VerifyUtils.IS_CYCLE)) {
                    note.setCycle(Integer.parseInt(cycle.getText()));
                } else {
                    note.setCycle(null);
                }
                // 设置通知
                if (!operation.setNotice(note)) {
                    return;
                }
                // 关闭窗口
                close();
                System.out.println("设置通知完成...");
            });
            // 合并
            GridPane.setRowSpan(sbm, 2);
            GridPane.setHalignment(sbm, HPos.CENTER);
            // 组件加入面板
            pane.getChildren().add(grid);
            setTitle("设置定时提醒");
            // 设置窗口的图标.
            getIcons().add(new Image(Main.class.getResourceAsStream(Constant.ICO_PATH)));
            setScene(new Scene(pane, width, height));
        }
    }

    /**
     * 创建笔记弹窗
     */
    private static class Create extends Stage {

        // 提示的窗口高宽度
        int width = 260;
        int height = 80;

        Create(Operation operation) {
            // Variable to store the focus on stage load
            final BooleanProperty firstTime = new SimpleBooleanProperty(true);
            FlowPane pane = new FlowPane();
            // 内边距
            pane.setPadding(new Insets(20, 20, 20, 20));
            // 组件
            javafx.scene.control.TextField input = new javafx.scene.control.TextField();
            javafx.scene.control.Button save = new javafx.scene.control.Button("保存");
            input.setPromptText("笔记标题");
            // 清除焦点
            input.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue && firstTime.get()) {
                    save.requestFocus(); // Delegate the focus to container
                    firstTime.setValue(false); // Variable value changed for future references
                }
            });
            // 设置保存点击事件
            save.setOnAction(event -> {
                operation.createNote(input.getText().trim());
                close();
            });
            // 布局
            GridPane grid = new GridPane();
            grid.setVgap(4);
            grid.setHgap(10);
            grid.setPadding(new Insets(5, 5, 5, 5));
            grid.add(input, 0, 0);
            grid.add(save, 1, 0);

            // 组件加入面板
            pane.getChildren().add(grid);
            setTitle("创建笔记");
            // 设置窗口的图标.
            getIcons().add(new Image(Main.class.getResourceAsStream(Constant.ICO_PATH)));
            setScene(new Scene(pane, width, height));
        }
    }

    /**
     * 右下角提示弹窗
     */
    private static class Tips extends Stage {

        // 提示的窗口高宽度
        int width = 360;
        int height = 270;

        Tips(String message) {
            // 获取屏幕大小
            Screen screen = (Screen) com.sun.javafx.tk.Toolkit.getToolkit().getPrimaryScreen();
            double screenWidth = screen.getWidth();
            double screenHeight = screen.getHeight();
            FlowPane pane = new FlowPane();
            // 提示信息
            WebView webView = new WebView();
            webView.getEngine().loadContent(StringEscapeUtils.unescapeXml(message));
            webView.setMaxWidth(width);
            webView.setMaxHeight(height);
            // 组件加入面板
            pane.getChildren().add(webView);
            setTitle(Constant.APP_NAME);
            getIcons().add(new Image(Main.class.getResourceAsStream(Constant.ICO_PATH)));
            setScene(new Scene(pane, width, height));
            setX(screenWidth - width - 10);
            setY(screenHeight - height - 33);
            // 保持最前
            setAlwaysOnTop(true);
            // 可拉动大小
            setResizable(false);
        }
    }
}
