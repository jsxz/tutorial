package anjunact.xml;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
public class XmlParser {
    public static void main(String args[]) throws DocumentException {
        String path = "data/4.2/descript.xsd"; //本地xml路径
        SAXReader reader = new SAXReader();
        Document doc = reader.read(path); // 读取XML文件
        Element root = doc.getRootElement(); // 获取XML根元素
        getElementList(root, "");
    }

    /**
     * 递归遍历传入节点下所有节点元素
     * @param element
     * @param parentId
     */
    public static void getElementList(Element element, String parentId) {
        List<?> elements = element.elements();
        try {
            parentId = element.attribute("id").getText(); //取得id属性的值
        } catch (NullPointerException e) {
            parentId = "";
        }

        /**
         * 判断是否包含子节点，如果包含子节点则进行递归查询
         */
        if (!elements.isEmpty()) {
            Iterator<?> it = elements.iterator();
            while (it.hasNext()) {
                Element elem = (Element) it.next();
                System.out.println("------------------------");
                System.out.println("path:" + elem.getPath()); //输出节点路径
                System.out.println("parentId:" + parentId);   //输出父节点id
                for (Iterator<?> it1 = elem.attributeIterator(); it1.hasNext();) {
                    Attribute attribute = (Attribute) it1.next();
                    System.out.println(attribute.getName() + ":" + attribute.getText()); //输出节点属性名称和属性值
                }
                System.out.println("value:" + elem.getTextTrim()); //输出节点内容
                getElementList(elem, parentId); // 递归遍历
            }
        }
    }

}
