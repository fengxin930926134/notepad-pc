package sample.api.impl;

import sample.entity.Note;
import sample.api.Operation;
import sample.utils.DialogUtils;
import sample.utils.DomXmlUtils;

import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements Operation {

    @Override
    public Note createNote(String title) {
        Note note = new Note();
        try {
            note.setTitle(title);
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

    /**
     * 获取异常的提示信息
     * @param e Exception
     * @return msg
     */
    private String getExceptionMsg(Exception e) {
        return e.getMessage() == null? e.getClass().getName(): e.getMessage();
    }
}
