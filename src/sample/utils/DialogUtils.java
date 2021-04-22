package sample.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

import java.awt.*;

/**
 * 弹窗工具类
 */
public class DialogUtils {

    /**
     * 警告
     * @param content 警告内容
     */
    public static void warn(String content) {
        new Alert(Alert.AlertType.WARNING, content, new ButtonType[]{ButtonType.CLOSE}).show();
    }

    /**
     * 通知
     * @param content 通知内容
     */
    public static void notice(String content) {
        new Tips(content).show();
    }

    /**
     * 创建笔记弹窗
     * @param operation 操作
     */
    public static void createNote(Operation operation) {
        new Create(operation).show();
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
            input.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
                if(newValue && firstTime.get()){
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
