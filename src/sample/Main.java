package sample;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);

    // 定时提醒时间
    int hour = 14;
    int minute = 40;
    // 提示的窗口高宽度
    int width = 300;
    int height = 275;
    // 提示信息
    String message = "马上三点了！";

    @Override
    public void start(Stage primaryStage) {
        // 获取屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();
        FlowPane pane = new FlowPane();
        // 内边距
        pane.setPadding(new Insets(50, 50, 50, 50));
        Label label = new Label(message);
        //组件加入面板
        pane.getChildren().add(label);
        primaryStage.setTitle("");
        //设置窗口的图标.
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("123.png")));
        primaryStage.setScene(new Scene(pane, width, height));
        primaryStage.setX(screenWidth - width);
        primaryStage.setY(screenHeight - height);
        primaryStage.show();
        MinWindow.getInstance().listen(primaryStage);
        MinWindow.getInstance().hide(primaryStage);
        //创建一个对话框提醒线程
        Task<String> task = new Task<String>() {
            @Override
            protected String call() {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime reminderTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), hour, minute);
                // 计算当前到指定时间还有多少毫秒
                if (now.compareTo(reminderTime) <= 0) {
                    Duration between = Duration.between(now, reminderTime);
                    threadPool.schedule(() -> MinWindow.getInstance().showStage(primaryStage), between.toMillis(), TimeUnit.MILLISECONDS);
                }
                return null;
            }
        };
        task.run();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
