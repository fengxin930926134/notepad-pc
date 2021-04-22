package sample.api;

import sample.entity.Note;
import java.util.List;

/**
 * 操作接口
 */
public interface Operation {

    /**
     * 创建一个只有标题的笔记
     * @param title 标题
     * @return Note
     */
    Note createNote(String title);

    /**
     * 更新笔记
     * @param note 新
     */
    void updateNote(Note note);

    /**
     * 读取xml中的笔记
     * @return list
     */
    List<Note> initNotes();
}
