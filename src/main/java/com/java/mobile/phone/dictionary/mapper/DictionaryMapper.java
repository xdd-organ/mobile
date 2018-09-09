package com.java.mobile.phone.dictionary.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DictionaryMapper {

    int insert(Map<String, Object> params);

    Map<String, Object> getByKey(@Param("key") String key);

    List<Map<String, Object>> listByDictionary(Map<String, Object> params);

    int updateByKey(Map<String, Object> params);
}
