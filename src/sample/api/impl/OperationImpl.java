package sample.api.impl;

import sample.entity.Note;
import sample.api.Operation;
import sample.utils.DialogUtils;
import sample.utils.DomXmlUtils;
import sample.utils.TimerTaskManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * 操作实现
 */
public class OperationImpl implements Operation {

    @Override
    public Note createNote(String title) {
        Note note = new Note();
        try {
            note.setTitle(title);
            note.setContent("");
            DomXmlUtils.appendXml(note);
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.warn(getExceptionMsg(e));
        }
        return note;
    }

    @Override
    public void updateNote(Note note) {
        try {
            DomXmlUtils.updateNote(note);
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.warn(getExceptionMsg(e));
        }
    }

    @Override
    public List<Note> initNotes() {
        try {
            return DomXmlUtils.readNotes();
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.warn(getExceptionMsg(e));
        }
        return new ArrayList<>();
    }

    @Override
    public boolean setNotice(Note note) {
        // 更改保存到数据库
        updateNote(note);
        // 判断通知时间是否是今天
        if (note.getRemindDate() != null && note.getRemindDate().compareTo(LocalDate.now()) == 0) {
            // 开始提醒任务
            return TimerTaskManager.getInstance().startRemindTaskToToday(note);
        }
        return false;
    }

    @Override
    public void readNoticeByToday() {
        try {
            List<Note> notes = DomXmlUtils.readNotes();
            List<Note> updates = new ArrayList<>();
            for (Note note: notes) {
                if (note.getRemindDate() != null) {
                    // 设置周期不为空且日期过期的到今日或之后
                    if (note.getRemindDate().compareTo(LocalDate.now()) < 0 && note.getCycle() != null && note.getCycle() != 0) {
                        note.setRemindDate(getCycleDate(note.getCycle(), note.getRemindDate()));
                        updates.add(note);
                    }
                    // 启动今天且未过期的任务
                    if (note.getRemindDate().compareTo(LocalDate.now()) == 0 &&
                            note.getRemindTime().compareTo(LocalTime.now()) >= 0) {
                        if (!TimerTaskManager.getInstance().startRemindTaskToToday(note)) {
                            System.out.println("启动定时任务失败！");
                        }
                    }
                }
            }
            // 更新周期日期
            for (Note note:updates) {
                updateNote(note);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.warn(getExceptionMsg(e));
        }
    }

    /**
     * 获取周期日期
     * @param cycle 周期
     * @param date 已有日期
     * @return 需要设置的日期
     */
    private static LocalDate getCycleDate(Integer cycle, LocalDate date) {
        if (date.compareTo(LocalDate.now()) < 0 && cycle > 0) {
            LocalDate localDate = date.plusDays(cycle);
            // 递归增加周期 直到满足条件
            return getCycleDate(cycle, localDate);
        } else {
            return date;
        }
    }

    /**
     * 获取异常的提示信息
     * @param e Exception
     * @return msg
     */
    private String getExceptionMsg(Exception e) {
        return e.getMessage() == null? e.getClass().getName(): e.getMessage();
    }
}
