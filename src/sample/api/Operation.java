package sample.api;

/**
 * 操作接口
 */
public interface Operation {

    /**
     * 创建一个只有标题的笔记
     * @param title 标题
     */
    void createNote(String title);
}
