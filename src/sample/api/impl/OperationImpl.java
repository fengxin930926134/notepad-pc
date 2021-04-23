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
            for (Note note: notes) {
                // 启动今天且未过期的任务
                if (note.getRemindDate() != null && note.getRemindDate().compareTo(LocalDate.now()) == 0 &&
                        note.getRemindTime().compareTo(LocalTime.now()) >= 0) {
                    if (!TimerTaskManager.getInstance().startRemindTaskToToday(note)) {
                        System.out.println("启动定时任务失败！");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.warn(getExceptionMsg(e));
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
