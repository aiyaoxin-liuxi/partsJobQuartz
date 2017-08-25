package com.dhb.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.type.TypeReference;

import com.google.gson.Gson;

/**
 * 
 *
 * <strong>JsonUtil</strong>. <br> 
 * <strong>Description : Gson&Jackson类库的封装工具类，专门负责解析json数据.内部分别实现了Gson、Jackson对象的单例.</strong> <br>
 * <strong>Create on : 2016年9月21日 上午11:34:16</strong>. <br>
 * <p>
 * <strong>Copyright (C) zhl Co.,Ltd.</strong> <br>
 * </p>
 * @author zts zhaotisheng@qq.com <br>
 * @version <strong>zhl-0.1.0</strong> <br>
 * <br>
 * <strong>修改历史: .</strong> <br>
 * 修改人 修改日期 修改描述<br>
 * -------------------------------------------<br>
 * <br>
 * <br>
 */
public class JsonUtil {
	
	public static void main(String[] args) {
		System.out.println("d");
	}

	private static ObjectMapper objMapper = null;
	private static Gson gson = null;

	static {
		if (null == objMapper) {
			objMapper = new ObjectMapper();
		}
		
		if (null == gson) {
			gson = new Gson();
		}
		
	}

	/**
	 * @author by  Jan 22, 2015
	 * 
	 * @desc 构造函数.
	 */
	private JsonUtil() {
	}

	/**
	 * @author by  May 28, 2015
	 * 
	 * @desc 将对象转换成json格式
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static StringBuilder toJson(Object obj) {
		if(Stringer.isNullOrEmpty(obj)){
			return null;
		}
		StringBuilder json = new StringBuilder();
		try {
			StringWriter writer = new StringWriter();
			JsonGenerator generator = new JsonFactory().createJsonGenerator(writer);
			objMapper.writeValue(generator, obj);
			if (generator != null) {
				generator.close();
			}
			json.append(writer.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * @author by  May 28, 2015
	 * 
	 * @desc 将json格式转换成list对象，并准确指定类型
	 * 
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> List<T> toList(StringBuilder json, Class<T> clazz) {
		if(Stringer.isNullOrEmpty(json)){
			return null;
		}
		List<T> objList = null;
		try {
			CollectionType type = objMapper.getTypeFactory().constructCollectionType(List.class, clazz);
			if (objMapper != null) {
				objList = objMapper.readValue(json.toString(), type);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return objList;
	}

	/**
	 * @author by  May 28, 2015
	 * 
	 * @desc 将json格式转换成map对象
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Object> toMap(StringBuilder json) {
		if(Stringer.isNullOrEmpty(json)){
			return null;
		}
		Map<String, Object> maps = null;
		try {
			//如果是map类型  
			maps = objMapper.readValue(json.toString(), new TypeReference<Map<String, Object>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return maps;
	}

	/***
	 * @author by  May 28, 2015
	 * 
	 * @desc 将json转换成bean对象
	 * 
	 * @param json
	 * @return
	 */
	public static Object toBean(StringBuilder json, Class<?> clazz) {
		if(Stringer.isNullOrEmpty(json)){
			return null;
		}
		Object obj = null;
		try {
			/*if (objMapper != null) {
				obj = objMapper.readValue(json, clazz);
			}*/
			if (gson != null) {
				obj = gson.fromJson(json.toString(), clazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
	}
	
	/**
	 * @author by  May 28, 2015
	 *
	 * @desc 根据key获得对应节点值.
	 * @param json
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static String toValue(String json, String key) throws Exception {
		if(Stringer.isNullOrEmpty(json) || Stringer.isNullOrEmpty(key)){
			return null;
		}
		JsonNode node = objMapper.readTree(json);
		return node.get(key).toString().replace("\"", "");
	}
	
	/**
	 * @author by  Aug 12, 2015
	 *
	 * @desc 将json转换成JsonNode节点树.
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static JsonNode toJsonNode(String json) throws Exception {
		if(Stringer.isNullOrEmpty(json)){
		}
		return objMapper.readTree(json);
	}

	/**
	 * @author by  May 28, 2015
	 * 
	 * @desc 根据json的keyPath路径获得对应的value，keyPath用 :分隔，e.g "data:list:name"
	 * 
	 * @param json
	 * @param keyPath
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static List<String> toValues(String json, String keyPath) throws Exception {
		if(Stringer.isNullOrEmpty(json) || Stringer.isNullOrEmpty(keyPath)){
			return null;
		}
		// 返回值
		List<String> value = new ArrayList<String>();
		if (Stringer.isNullOrEmpty(json) || (Stringer.isNullOrEmpty(keyPath))) {
			return value;
		}
		String[] path = keyPath.split(":");
		JsonNode node = objMapper.readTree(json);
		setValues(node, path, value, 1);
		return value;
	}

	@Deprecated
	private static void setValues(JsonNode node, String[] path, List<String> values, int nextIndex) {
		if (Stringer.isNullOrEmpty(node)) {
			return;
		}
		// 是路径的最后就直接取值
		if (nextIndex == path.length) {
			if (node.isArray()) {
				for (int i = 0; i < node.size(); i++) {
					JsonNode child = node.get(i).get(path[nextIndex - 1]);
					if (Stringer.isNullOrEmpty(child)) {
						continue;
					}
					values.add(child.toString());
				}
			} else {
				JsonNode child = node.get(path[nextIndex - 1]);
				if (!Stringer.isNullOrEmpty(child)) {
					values.add(child.toString());
				}
			}
			return;
		}
		// 判断是Node下是集合还是一个节点
		node = node.get(path[nextIndex - 1]);
		if (node.isArray()) {
			for (int i = 0; i < node.size(); i++) {
				setValues(node.get(i), path, values, nextIndex + 1);
			}
		} else {
			setValues(node, path, values, nextIndex + 1);
		}
	}


}
