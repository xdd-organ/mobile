package com.java.mobile.common.utils;

import org.dom4j.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlUtils {

	public static final String UTF8_ENCODING = "UTF-8";

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

	public static String parseNodeValue(String nodeName, String xmlStr) {
		String nodeStart = "<" + nodeName + ">";
		String nodeEnd = "</" + nodeName + ">";
		return parseNodeValue(nodeStart, nodeEnd, xmlStr);
	}

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

	public static Document getDocumentFromXmlStr(String xmlStr) throws DocumentException {
		// 将xml字符串替转换为Document对象
		Document doc = DocumentHelper.parseText(xmlStr);
		return doc;
	}
	public static String getNodeValueFromDocument(Document doc, String nodeName) {
		return getNodeValueByXpath(doc, "//" + nodeName);
	}

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

	public static String getNodeValueFromXml(String nodeName, String xmlStr) {
		String nodeStart = "<" + nodeName + ">";
		String nodeEnd = "</" + nodeName + ">";
		return getNodeValueFromXml(nodeStart, nodeEnd, xmlStr);
	}

	public static String getNodeValueFromXml(String nodeStart, String nodeEnd, String src) {
		String rtnStr = "";
		int nodeStartLength = nodeStart.length();
		int start = src.indexOf(nodeStart);
		int end = src.indexOf(nodeEnd);
		if (start > -1 && end > -1) {
			rtnStr = src.substring(start + nodeStartLength, end);
		}

		if (org.apache.commons.lang3.StringUtils.isNotBlank(rtnStr) && rtnStr.contains("<![CDATA[")) {
			rtnStr = rtnStr.substring(9, rtnStr.lastIndexOf("]]>"));
		}

		return rtnStr;
	}
}
