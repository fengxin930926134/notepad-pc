package sample.utils;

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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;
import sample.api.Operation;
import sample.entity.Note;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 弹窗工具类
 */
public class DialogUtils {

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
                System.out.println(cb.getValue());
                System.out.println(checkInDatePicker.getValue());
                System.out.println(time.getText());
                System.out.println(cycle.getText());

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
        int width = 300;
        int height = 275;
        // 提示字体大小
        int fontSize = 28;

        Tips(String message) {
            // 获取屏幕大小
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double screenWidth = screenSize.getWidth();
            double screenHeight = screenSize.getHeight();
            FlowPane pane = new FlowPane();
            // 内边距
            pane.setPadding(new Insets(10, 10, 10, 10));
            // 提示信息
            Text t = new Text();
            t.setCache(true);
            t.setX(10.0);
            t.setY(70.0);
            t.setFill(Color.CHOCOLATE);
            t.setText(message);
            t.setFont(Font.font(null, FontWeight.BOLD, fontSize));
            //组件加入面板
            pane.getChildren().add(t);
            setTitle(Constant.APP_NAME);
            getIcons().add(new Image(Main.class.getResourceAsStream(Constant.ICO_PATH)));
            setScene(new Scene(pane, width, height));
            setX(screenWidth - width);
            setY(screenHeight - height);
        }
    }
}
