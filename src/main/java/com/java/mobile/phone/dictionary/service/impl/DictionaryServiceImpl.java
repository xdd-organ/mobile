package com.java.mobile.phone.dictionary.service.impl;

import com.java.mobile.phone.dictionary.mapper.DictionaryMapper;
import com.java.mobile.phone.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public Long insert(Map<String, Object> params) {
        dictionaryMapper.insert(params);
        return Long.valueOf(params.get("id").toString());
    }

    @Override
    public Map<String, Object> getByKey(String key) {
        return dictionaryMapper.getByKey(key);
    }

    @Override
    public int updateByKey(Map<String, Object> params) {
        return dictionaryMapper.updateByKey(params);
    }

    @Override
    public List<Map<String, Object>> listByDictionary(Map<String, Object> params) {
        return dictionaryMapper.listByDictionary(params);
    }

    @Override
    public int getPrice(String lockNo) {
        Map<String, Object> obj = this.getByKey("unit_price");
        return Integer.valueOf(obj.get("value").toString());
    }
}
