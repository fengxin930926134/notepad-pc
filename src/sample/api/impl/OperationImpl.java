package sample.api.impl;

import sample.entity.Note;
import sample.api.Operation;
import sample.utils.DialogUtils;
import sample.utils.DomXmlUtils;

public class OperationImpl implements Operation {

    @Override
    public void createNote(String title) {
        try {
            Note note = new Note();
            note.setTitle(title);
            DomXmlUtils.appendXML(note);
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
