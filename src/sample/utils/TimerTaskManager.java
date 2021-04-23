package sample.utils;

import javafx.application.Platform;
import sample.entity.Note;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 提醒任务工具
 */
public class TimerTaskManager {

    /**
     * 定时任务缓存
     */
    static Map<String, ScheduledFuture<?>> taskCacheMap = new Hashtable<>();

    private static class Inner {
        private static final TimerTaskManager INSTANCE = new TimerTaskManager();
    }

    /**
     * 私有化构造方法
     */
    private TimerTaskManager() {
    }

    /**
     * 提供一个共有的可以返回类对象的方法
     */
    public static TimerTaskManager getInstance() {
        return TimerTaskManager.Inner.INSTANCE;
    }

    /**
     * 启动一个今天要执行的定时任务
     *
     * @param note 日期 提醒内容 id
     * @return boolean
     */
    public boolean startRemindTaskToToday(Note note) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime remindTime = note.getRemindTime();
        LocalDateTime reminderDateTime = LocalDateTime.of(LocalDate.now(), remindTime);
        // 计算当前到指定时间还有多少毫秒
        if (now.compareTo(reminderDateTime) <= 0) {
            Duration between = Duration.between(now, reminderDateTime);
            ScheduledFuture<?> schedule = ThreadPoolManager.getInstance().schedule(() -> {
                System.out.println("执行定时提醒...");
                // 提醒
                Platform.runLater(() -> DialogUtils.notice(note.getContent()));
                // 修改下次提醒周期
                DomXmlUtils.setNextNoticeTime(note);
            }, between.toMillis(), TimeUnit.MILLISECONDS);
            System.out.println("定时任务添加完成...还需时间:" + between.toMillis());
            // 停止可能存在的任务
            stopTaskById(note.getId());
            taskCacheMap.put(note.getId(), schedule);
            return true;
        } else {
            // 过期
            DialogUtils.warn("已经过了提醒时间！");
        }
        return false;
    }

    /**
     * 停止定时任务 根据id
     *
     * @param id note id
     */
    public void stopTaskById(String id) {
        if (taskCacheMap.containsKey(id)) {
            ScheduledFuture<?> scheduledFuture = taskCacheMap.get(id);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        }
    }

    /**
     * 停止所有任务
     */
    public void stopAll() {
        for (ScheduledFuture<?> scheduledFuture: taskCacheMap.values()) {
            scheduledFuture.cancel(true);
        }
    }
}
