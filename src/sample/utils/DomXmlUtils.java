package sample.utils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sample.entity.Note;

public class DomXmlUtils {

  public static void main(String[] args) {

//		Note note = new Note();
//		note.setId("b2eda452-83c9-41b6-a466-e1bf6b7a3ba3");
//		note.setTitle("标题");
//		note.setContent("内容修改");
//		note.setRemindDate(LocalDate.now());
//		note.setRemindTime(LocalTime.now());
//		appendXML(note);
//		updateNote(note);
//		deleteNoteById("123");
  }

  /**
   * 配置文件名
   */
  private static final String CONFIG_NOTE_FILE = "configNoteFile.xml";
  /**
   * root节点
   */
  private static final String CONFIG_NOTE_FILE_ROOT = "configNoteFileRoot";
  /**
   * 子节点名
   */
  private static final String CONFIG_CHILD_NODE = "note";

  /**
   * 更新笔记
   *
   * @param note 笔记
   */
  public static void updateNote(Note note) throws Exception {
    // 获取xml文档
    Document document = getDocument();
    NodeList childNodes = document.getDocumentElement().getChildNodes();
    if (childNodes != null) {
      for (int i = 0; i < childNodes.getLength(); i++) {
        // 节点中包含元素节点和文本节点
        Node nodes = childNodes.item(i);
        if (nodes.getNodeType() == Node.ELEMENT_NODE) {
          boolean is = false;
          // 获取笔记的属性的值
          for (Node node = nodes.getFirstChild(); node != null; node = node.getNextSibling()) {
            // TEXT_NODE 说明该节点是文本节点
            // ELEMENT_NODE 说明该节点是个元素节点
            if (node.getNodeType() == Node.ELEMENT_NODE) {
              String textContent = null;
              if ("id".equals(node.getNodeName())) {
                is = node.getTextContent().equals(note.getId());
              }
              // 是这个对象
              if (is) {
                switch (node.getNodeName()) {
                  case "title":
                    textContent = note.getTitle();
                    break;
                  case "content":
                    textContent = note.getContent();
                    break;
                  case "remindTime":
                    textContent = note.getRemindTime() == null ? null : note.getRemindTime().toString();
                    break;
                  case "remindDate":
                    textContent = note.getRemindDate() == null ? null : note.getRemindDate().toString();
                    break;
                  case "cycle":
                    textContent = note.getCycle() == null ? null : note.getCycle().toString();
                    break;
                  default:
                }
                if (textContent != null) {
                  node.setTextContent(textContent);
                }
              }
            }
          }
        }
      }
      // 保存
      saveXml(document);
    }
  }

  /**
   * 根据笔记id删除笔记
   *
   * @param id id
   */
  public static void deleteNoteById(String id) throws Exception {
    // 获取xml文档
    Document document = getDocument();
    NodeList childNodes = document.getDocumentElement().getChildNodes();
    if (childNodes != null) {
      for (int i = 0; i < childNodes.getLength(); i++) {
        // 节点中包含元素节点和文本节点
        Node nodes = childNodes.item(i);
        if (nodes.getNodeType() == Node.ELEMENT_NODE) {
          Node delNode = null;
          // 获取笔记的属性的值
          for (Node node = nodes.getFirstChild(); node != null; node = node.getNextSibling()) {
            // TEXT_NODE 说明该节点是文本节点
            // ELEMENT_NODE 说明该节点是个元素节点
            if (node.getNodeType() == Node.ELEMENT_NODE
                    && "id".equals(node.getNodeName())
                    && node.getTextContent().equals(id)) {
              delNode = node.getParentNode();
            }
          }
          // 移除节点
          if (delNode != null) {
            childNodes.item(i).getParentNode().removeChild(delNode);
          }
        }
      }
      // 保存
      saveXml(document);
    }
  }

  /**
   * 读取xml中的笔记
   *
   * @return list
   */
  public static List<Note> readNotes() throws Exception {
    List<Note> noteList = new ArrayList<>();
    // 获取xml文档
    Document document = getDocument();
    // root节点
    Element root = document.getDocumentElement();
    NodeList notes = root.getChildNodes();
    if (notes != null) {
      for (int i = 0; i < notes.getLength(); i++) {
        // 节点中包含元素节点和文本节点
        Node nodes = notes.item(i);
        if (nodes.getNodeType() == Node.ELEMENT_NODE) {
          Note note = new Note();
          // 获取笔记的属性的值
          for (Node node = nodes.getFirstChild(); node != null; node = node.getNextSibling()) {
            // TEXT_NODE 说明该节点是文本节点
            // ELEMENT_NODE 说明该节点是个元素节点
            if (node.getNodeType() == Node.ELEMENT_NODE) {
              String textContent = node.getTextContent();
              if (textContent != null && !"".equals(textContent)) {
                switch (node.getNodeName()) {
                  case "id":
                    note.setId(textContent);
                    break;
                  case "title":
                    note.setTitle(textContent);
                    break;
                  case "content":
                    note.setContent(textContent);
                    break;
                  case "remindTime":
                    note.setRemindTime(LocalTime.parse(textContent));
                    break;
                  case "remindDate":
                    note.setRemindDate(LocalDate.parse(textContent));
                    break;
                  case "cycle":
                    note.setCycle(Integer.parseInt(textContent));
                    break;
                  default:
                }
              }
            }
          }
          noteList.add(note);
        }
      }
    }
    return noteList;
  }

  /**
   * 向xml root节点追加一个节点 属性根据对象属性
   *
   * @param t   对象
   * @param <T> 泛型
   */
  public static <T> void appendXml(T t) throws Exception {
    // 获取xml文档
    Document document = getDocument();
    // root节点
    Element root = document.getDocumentElement();
    Element element = document.createElement(CONFIG_CHILD_NODE);
    Field[] fields = t.getClass().getDeclaredFields();
    for (Field field : fields) {
      // 子属性
      Element child = document.createElement(field.getName());
      // 当isAccessible()的结果是false时不允许通过反射访问该字段
      field.setAccessible(true);
      child.setTextContent(field.get(t) == null ? "" : field.get(t).toString());
      // 加入父节点
      element.appendChild(child);
    }
    root.appendChild(element);
    // 保存
    saveXml(document);
  }

  /**
   * 获取xml
   *
   * @return Document
   */
  private static Document getDocument() throws Exception {
    // 调用 DocumentBuilderFactory.newInstance() 方法得到创建 DOM 解析器的工厂
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    // 调用工厂对象的 newDocumentBuilder方法得到 DOM 解析器对象
    DocumentBuilder db = factory.newDocumentBuilder();
    // 代表整个文档的 Document 对象
    Document document;
    // 判断是否存在文件
    if (FileUtils.isExist(CONFIG_NOTE_FILE)) {
      document = db.parse(new FileInputStream(CONFIG_NOTE_FILE));
    } else {
      document = db.newDocument();
      // 不显示standalone="no"
      document.setXmlStandalone(true);
      // 创建TransformerFactory对象
      TransformerFactory tff = TransformerFactory.newInstance();
      // 创建Transformer对象
      Transformer tf = tff.newTransformer();
      // 创建根节点
      Element root = document.createElement(CONFIG_NOTE_FILE_ROOT);
      // 将root节点添加到dom树中
      document.appendChild(root);
      // 输出内容是否使用换行
      tf.setOutputProperty(OutputKeys.INDENT, "yes");
      // 创建xml文件并写入内容
      tf.transform(new DOMSource(document), new StreamResult(new File(CONFIG_NOTE_FILE)));
    }
    return document;
  }

  /**
   * xml保存
   *
   * @param document Document
   */
  private static void saveXml(Document document) throws Exception {
    // 创建TransformerFactory对象
    TransformerFactory tff = TransformerFactory.newInstance();
    // 创建 Transformer对象
    Transformer tf = tff.newTransformer();
    // 输出内容是否使用换行
    tf.setOutputProperty(OutputKeys.INDENT, "yes");
    // 创建xml文件并写入内容
    tf.transform(new DOMSource(document), new StreamResult(new File(CONFIG_NOTE_FILE)));
  }
}