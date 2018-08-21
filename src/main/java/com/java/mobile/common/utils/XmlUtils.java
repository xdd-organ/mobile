package com.java.mobile.common.utils;

import org.dom4j.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code XmlUtils}
 * 报文处理组件旨在提供一系列处理xml字符串的常用方法：提取相关节点的内容、替换节点内容……可选择的方法包括，通过字符串截取、替换、
 * 通过xpath方式处理。 基于xml字符串形式多样化，提供方法分以下几类：
 * <p>
 * 获取节点内容，如：parseNodeValueFromXml(java.lang.String nodeStart, java.lang.String
 * nodeEnd, java.lang.String src)
 * <p>
 * 获取节点内容，去除内容中的[CDATA]，如：getNodeValueFromXml(java.lang.String nodeStart,
 * java.lang.String nodeEnd, java.lang.String src)
 * <p>
 * 通过xpath获取节点内容，如：getNodeValueByXpath(Document doc, java.lang.String
 * xpathExpress)
 * 
 */
public class XmlUtils {

	public static final String UTF8_ENCODING = "UTF-8";

	/**
	 * 判断字符串中是否包含xml格式字符串
	 *
	 * @param str
	 * @return
	 */
	public static boolean isXmlStr(String str) {
		if (!StringUtils.hasText(str)) {
			return false;
		}

		try {
			getDocumentFromXmlStr(str);
			return true;
		} catch (DocumentException e) {
			return false;
		}
	}


	/**
	 * <p>
	 * 获取xml字符串中指定节点的值，对应节点中可能包含&lt;![CDATA[xxxx]]&gt;的(如：&lt;attach&gt;&lt; ![
	 * CDATA[支付测试]] &gt;&lt;/attach&gt;)使用此函数<br>
	 * 确定节点不包含&lt;![CDATA[xxxx]]&gt;建议使用 函数parseNodeValueFromXml
	 * </p>
	 * 
	 * @param nodeStart
	 *            节点开始标签 eg : &lt;TransactionID&gt;
	 * @param nodeEnd
	 *            节点结束标签 eg : &lt;/TransactionID&gt;
	 * @param src
	 *            原字符串
	 * @return
	 */
	public static String parseNodeValue(String nodeStart, String nodeEnd, String src) {
		String rtnStr = "";
		int nodeStartLength = nodeStart.length();
		int start = src.indexOf(nodeStart);
		int end = src.indexOf(nodeEnd);
		if (start > -1 && end > -1) {
			// 先从xml字符串中截取出对应节点的内容
			rtnStr = src.substring(start + nodeStartLength, end);
		}
		// 判断节点内容是否包含了"CDATA"，若有，需要对字符串再次截取以获得数据
		if (StringUtils.hasText(rtnStr)&& rtnStr.startsWith("<![CDATA[")) {
			rtnStr = rtnStr.substring(9, rtnStr.lastIndexOf("]]>"));
		}
		return rtnStr;
	}

	/**
	 * 获取xml字符串中指定节点的值，对应节点中可能包含"&lt;![CDATA[xxxx]]>"的(如：
	 * <attach>&lt;![CDATA[支付测试]]></attach>)使用此函数<br>
	 * 确定节点不包含"&lt;![CDATA[xxxx]]>"建议使用 函数parseNodeValueFromXml
	 * 
	 * @param nodeName
	 *            节点名字eg： xml
	 * @param xmlStr
	 *            原字符串
	 * @return
	 */
	public static String parseNodeValue(String nodeName, String xmlStr) {
		String nodeStart = "<" + nodeName + ">";
		String nodeEnd = "</" + nodeName + ">";
		return parseNodeValue(nodeStart, nodeEnd, xmlStr);
	}

	/**
	 * 替换xml中节点的值，只适合替换报文中只有一个指定名字的节点
	 * 
	 * @param nodeStart
	 *            节点开始标签 eg :&lt;TransactionID>
	 * @param nodeEnd
	 *            节点结束标签 eg :&lt;/TransactionID>
	 * @param relacement
	 *            节点替换的内容
	 * @param xml
	 *            原字符串
	 * @return
	 */
	public static String replaceNodeContent(String nodeStart, String nodeEnd, String relacement, String xml) {
		int nodeStartLength = nodeStart.length();
		int start = xml.indexOf(nodeStart);
		int end = xml.indexOf(nodeEnd);

		if (start > -1 && end > -1) {
			String segStart = xml.substring(0, start + nodeStartLength);
			String segEnd = xml.substring(end, xml.length());
			return segStart + relacement + segEnd;
		}
		return xml;
	}

	/**
	 * 从字符串(xml字符串)中获取一个org.dom4j.Document对象
	 * 
	 * @param xmlStr
	 *            具备xml格式的字符串
	 * @return org.dom4j.Document对象
	 * @throws DocumentException
	 */
	public static Document getDocumentFromXmlStr(String xmlStr) throws DocumentException {
		// 将xml字符串替转换为Document对象
		Document doc = DocumentHelper.parseText(xmlStr);
		return doc;
	}

	/**
	 * 利用节点名字从org.dom4j.Document对象中获取节点值
	 * 
	 * @param doc
	 *            org.dom4j.Document对象
	 * @param nodeName
	 *            节点名称
	 * @return
	 */
	public static String getNodeValueFromDocument(Document doc, String nodeName) {
		return getNodeValueByXpath(doc, "//" + nodeName);
	}

	/**
	 * 利用xpath表达式获取节点值
	 * 
	 * @param doc
	 *            org.dom4j.Document对象
	 * @param xpathExpress
	 *            xpath表达式
	 * @return
	 */
	public static String getNodeValueByXpath(Document doc, String xpathExpress) {
		// 利用xpath获取指定节点的值
		Node node = doc.selectSingleNode(xpathExpress);
		return node.getText();
	}


	/**
	 * 去除报文中的空格、回车、换行符、制表符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
			dest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + dest.substring(36);
		}
		return dest;
	}

	/**
	 * 将xml字符串转换为排序map
	 * 
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	public static SortedMap<String, String> xmlStrToMap(String xmlStr) throws DocumentException {
		SortedMap<String, String> map = new TreeMap<String, String>();
		// 将xml字符串转换为org.dom4j.Document对象
		Document doc;
		doc = DocumentHelper.parseText(xmlStr);
		// 获取根元素
		Element root = doc.getRootElement();
		getNodes(root, map);	
		return map;
	}

	/**
	 * 
	 * 递归访问xml子节点
	 *
	 */
	private static void getNodes(Element node, Map<String, String> map) {
		@SuppressWarnings("unchecked")
		List<Element> list = node.elements();
		if (list.size() > 0) {
			for (Element element : list) {
				getNodes(element, map);
			}
		} else {
			map.put(node.getName(), node.getTextTrim());
		}

	}
}
