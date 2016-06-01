package com.pkit.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JSONUtils {
	private static JsonConfig config = new JsonConfig();

	/**
	 * 把对象 格式化为 json字符串
	 * @param rootObject 对象
	 * @return json字符串
	 */
	public static String object2json(Object rootObject) {
		return object2json(rootObject,null);
	}
	
	/**
	 * 把对象 格式化为 json字符串
	 * @param rootObject 对象
	 * @param dataFromStr 日期对象的转化字符串格式
	 * @return json字符串
	 */
	public static String object2json(Object rootObject,String dataFromStr) {
		String json = "";
		if(dataFromStr!=null){
			config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor(dataFromStr));
		}else{
			config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor());
		}
		try {
			if(rootObject instanceof String){
				json=rootObject+"";
			}else{
				if (rootObject instanceof List) {
					json = JSONArray.fromObject(rootObject, config).toString();
				} else {
					json = JSONObject.fromObject(rootObject, config).toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 把javaBean对象 格式化为 json字符串 指定输出属性
	 * @param rootObject
	 * @param jsonConfig
	 * @return
	 * @date:2016年2月24日
	 */
	public static String object2jsonByConfig(Object rootObject,JsonConfig jsonConfig){
		String json = "";
		try {
			if(rootObject instanceof String){
				json=rootObject+"";
			}else{
				if (rootObject instanceof List) {
					json = JSONArray.fromObject(rootObject, jsonConfig).toString();
				} else {
					json = JSONObject.fromObject(rootObject, jsonConfig).toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
    public static String list2json(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
	 * json字符串 格式如下:
	 * @param jsonStr
	 * @return
	 */
	public static Map<String , ?> jsonToMap(String jsonStr){
		JSONObject jsonObject=JSONObject.fromObject(jsonStr);
		Map<String, ?> map=(Map<String, ?>) JSONObject.toBean(jsonObject, Map.class);
		return map;
	}
	
	public static class DateJsonValueProcessor implements JsonValueProcessor {
		private String format = "yyyy-MM-dd HH:mm:ss";

		public DateJsonValueProcessor() {
		}

		public DateJsonValueProcessor(String format) {
			this.format = format;
		}

		public Object processArrayValue(Object value, JsonConfig jsonConfig) {
			String[] obj = {};
			if (value instanceof Date[]) {
				SimpleDateFormat sf = new SimpleDateFormat(format);
				Date[] dates = (Date[]) value;
				obj = new String[dates.length];
				for (int i = 0; i < dates.length; i++) {
					obj[i] = sf.format(dates[i]);
				}
			}
			return obj;
		}

		public Object processObjectValue(String key, Object value,JsonConfig jsonConfig) {
			String str="";
			if(value != null){
				if (value instanceof Date) {
					try {
						str = new SimpleDateFormat(format).format((Date) value);
					} catch (Exception e) {
					}
				}else{
					str=value.toString();
				}
			}
			return str;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

	}

	public static JsonConfig getConfig() {
		return config;
	}

	public static void setConfig(JsonConfig config) {
		JSONUtils.config = config;
	}
	
	
	
	public static void main(String[] args) {
		String s="{tt:{id:1, type:'uuu哈哈哈', t:{'id':22,str:'dfdfdfdf'},s:{\"id\":3232,uu:\"hfahfigieg\"}},dd:{id:2, type:'uuu哈哈哈', t:{'id':22,str:'dfdfdfdf'},s:{\"id\":3232,uu:\"hfahfigieg\"}}}";
		JSONObject jsonObject=JSONObject.fromObject(s);
		Map<String, ?> map=(Map<String, ?>) JSONObject.toBean(jsonObject, Map.class);
		System.out.println(map.get("id")+"..."+map.get("type"));
		System.out.println(map);




	}
}
