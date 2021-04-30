package sample.utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import sample.entity.Note;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
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
            final ScheduledFuture<?>[] schedule = new ScheduledFuture<?>[1];
            new Task<String>() {
                @Override
                protected String call() {
                    schedule[0] = ThreadPoolManager.getInstance().schedule(() -> {
                        System.out.println("执行定时提醒...");
                        // 提醒
                        Platform.runLater(() -> DialogUtils.notice(note.getContent()));
                        // 修改下次提醒周期
                        setNextNoticeTime(note);
                    }, between.toMillis(), TimeUnit.MILLISECONDS);
                    return null;
                }
            }.run();
            System.out.println("定时任务添加完成...还需时间:" + between.toMillis());
            // 停止可能存在的任务
            stopTaskById(note.getId());
            // 保存任务
            setTask(note.getId(), schedule[0]);
            return true;
        } else {
            // 过期
            DialogUtils.warn("已经过了提醒时间！");
        }
        return false;
    }

    /**
     * 设置通知的下个周期提醒
     *
     * @param note Note
     */
    public void setNextNoticeTime(Note note) {
        if (note.getCycle() != null) {
            // 修改提醒时间
            note.setRemindDate(note.getRemindDate().plusDays(note.getCycle()));
            try {
                DomXmlUtils.updateNote(note);
            } catch (Exception e) {
                e.printStackTrace();
                DialogUtils.warn("设置周期提醒失败！");
            }
        }
    }

    /**
     * 获取所有任务笔记id
     *
     * @return ids
     */
    public Set<String> getAllTaskId() {
        return taskCacheMap.keySet();
    }

    /**
     * 设置任务
     *
     * @param key      String
     * @param schedule ScheduledFuture<?>
     */
    public void setTask(String key, ScheduledFuture<?> schedule) {
        if (taskCacheMap == null) {
            taskCacheMap = new Hashtable<>();
        }
        taskCacheMap.put(key, schedule);
    }

    /**
     * 停止定时任务 根据id
     *
     * @param id note id
     */
    public void stopTaskById(String id) {
        if (taskCacheMap != null && id != null && taskCacheMap.containsKey(id)) {
            ScheduledFuture<?> scheduledFuture = taskCacheMap.get(id);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                System.out.println("通知停止...");
            }
        }
    }

    /**
     * 停止所有任务
     */
    public void stopAll() {
        if (taskCacheMap != null) {
            for (ScheduledFuture<?> scheduledFuture : taskCacheMap.values()) {
                scheduledFuture.cancel(true);
            }
        }
    }
}
