//package sample;
//
//import javafx.application.Application;
//import javafx.concurrent.Task;
//import javafx.event.ActionEvent;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.layout.FlowPane;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//import sample.utils.MinWindow;
//
//import java.awt.*;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//public class Main extends Application {
//    // 线程池
//    ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);
//    // 定时提醒任务缓存
//    Map<String, Task<String>> taskMap = new HashMap<>();
//
//    // 定时提醒时间
//    int hour = 14;
//    int minute = 40;
//    // 提示的窗口高宽度
//    int width = 300;
//    int height = 275;
//    // 提示信息
//    String message = "马上三点了！";
//    Task<String> task = null;
//
//    @Override
//    public void start(Stage primaryStage) {
//        // 获取屏幕大小
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        double screenWidth = screenSize.getWidth();
//        double screenHeight = screenSize.getHeight();
//        FlowPane pane = new FlowPane();
//        // 内边距
//        pane.setPadding(new Insets(50, 50, 50, 50));
//        // 信息和时间设置
//        javafx.scene.control.TextField notification = new javafx.scene.control.TextField(message);
//        javafx.scene.control.TextField hourText = new javafx.scene.control.TextField();
//        javafx.scene.control.TextField minuteText = new javafx.scene.control.TextField();
//        javafx.scene.control.Button sm = new javafx.scene.control.Button("保存设置");
//
//        GridPane grid = new GridPane();
//        grid.setVgap(4);
//        grid.setHgap(10);
//        grid.setPadding(new Insets(5, 5, 5, 5));
//        grid.add(new Label("提示内容: "), 0, 0);
//        grid.add(notification, 1, 0);
//        grid.add(new Label("时: "), 0, 1);
//        grid.add(hourText, 1, 1);
//        grid.add(new Label("分: "), 0, 2);
//        grid.add(minuteText, 1, 2);
//        grid.add(sm, 0, 3, 2,1);
//        sm.setOnAction((ActionEvent e) -> {
//            message = notification.getText().trim();
//            if (!hourText.getText().trim().equals("") && !minuteText.getText().trim().equals("")) {
//                int hourInt = Integer.parseInt(hourText.getText().trim());
//                int minuteInt = Integer.parseInt(minuteText.getText().trim());
//                if (hourInt < 24 && hourInt >= 0) {
//                    hour = hourInt;
//                } else {
//                    new Alert(Alert.AlertType.NONE, "输错了憨批", new ButtonType[]{ButtonType.CLOSE}).show();
//                }
//                if (minuteInt < 60 && hourInt >= 0) {
//                    minute = minuteInt;
//                } else {
//                    new Alert(Alert.AlertType.NONE, "输错了憨批", new ButtonType[]{ButtonType.CLOSE}).show();
//                }
//            }
//            if (task != null) {
//                task.cancel();
//            }
//            startTaskRemindByToday(primaryStage, hour, minute);
//            task.run();
//        });
//        //组件加入面板
//        pane.getChildren().add(grid);
//        primaryStage.setTitle("");
//        //设置窗口的图标.
//        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("123.png")));
//        primaryStage.setScene(new Scene(pane, width, height));
//        primaryStage.setX(screenWidth - width);
//        primaryStage.setY(screenHeight - height);
////        primaryStage.show();
//        MinWindow.getInstance().listen(primaryStage);
//        MinWindow.getInstance().hide(primaryStage);
//        //创建一个对话框提醒线程
//        startTaskRemindByToday(primaryStage, hour, minute);
//        task.run();
//    }
//
//
//    /**
//     * 启动一个今天要执行的定时提醒
//     * @param primaryStage 提醒的窗体
//     * @param hour 小时
//     * @param minute 分钟
//     * @return 返回这个任务的id
//     */
//    private String startTaskRemindByToday(Stage primaryStage, int hour, int minute) {
//        String uuid = UUID.randomUUID().toString();
//        Task<String> task = new Task<String>() {
//            @Override
//            protected String call() {
//                LocalDateTime now = LocalDateTime.now();
//                LocalDateTime reminderTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), hour, minute);
//                // 计算当前到指定时间还有多少毫秒
//                if (now.compareTo(reminderTime) <= 0) {
//                    Duration between = Duration.between(now, reminderTime);
//                    threadPool.schedule(() -> MinWindow.getInstance().showStage(primaryStage), between.toMillis(), TimeUnit.MILLISECONDS);
//                }
//                return null;
//            }
//        };
//        task.run();
//        taskMap.put(uuid, task);
//        return uuid;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
