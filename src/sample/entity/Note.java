package sample.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * 笔记
 */
public class Note {

    /**
     * 标识
     */
    private String id = UUID.randomUUID().toString();

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 提醒日期
     */
    private LocalDate remindDate;

    /**
     * 提醒时间
     */
    private LocalTime remindTime;

    /**
     * 周期（天）
     */
    private Integer cycle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalTime getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(LocalTime remindTime) {
        this.remindTime = remindTime;
    }

    public LocalDate getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(LocalDate remindDate) {
        this.remindDate = remindDate;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", remindDate=" + remindDate +
                ", remindTime=" + remindTime +
                ", cycle=" + cycle +
                '}';
    }
}
