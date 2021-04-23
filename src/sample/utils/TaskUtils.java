package sample.utils;

import sample.entity.Note;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 提醒任务工具
 */
public class TaskUtils {

    /** 定时任务缓存 */
    static Map<String, ScheduledFuture<?>> taskCacheMap = new Hashtable<>();

    /**
     * 启动一个今天要执行的定时任务
     * @param note 日期 提醒内容 id
     * @return boolean
     */
    public static boolean startRemindTaskToToday(Note note) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime remindTime = note.getRemindTime();
        LocalDateTime reminderDateTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                remindTime.getHour(), remindTime.getMinute());
        // 计算当前到指定时间还有多少毫秒
        if (now.compareTo(reminderDateTime) <= 0) {
            System.out.println("定时任务添加完成...");
            Duration between = Duration.between(now, reminderDateTime);
            ScheduledFuture<?> schedule = ThreadPoolManager.getInstance().schedule(() -> {
                System.out.println("执行定时提醒...");
                // 提醒
                DialogUtils.notice(note.getContent());
                // 修改下次提醒周期
                DomXmlUtils.setNextNoticeTime(note);
            }, between.toMillis(), TimeUnit.MILLISECONDS);
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
     * @param id note id
     */
    public static void stopTaskById(String id) {
        if (taskCacheMap.containsKey(id)) {
            ScheduledFuture<?> scheduledFuture = taskCacheMap.get(id);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        }
    }
}
