/**
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.sqlbatis.scripting.xmltags;

import ognl.OgnlContext;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 动态 SQL ，用于每次执行 SQL 操作时，记录动态 SQL 处理后的最终 SQL 字符串。
 *
 * @author Clinton Begin
 */
public class DynamicContext {

    /**
     * {@link #bindings} _parameter 的键，参数
     */
    public static final String PARAMETER_OBJECT_KEY = "_parameter";

    static {
        // 设置 OGNL 的属性访问器。其中，OgnlRuntime 是 ognl 库中的类。并且，ContextMap 对应的访问器是 ContextAccessor 类。
        OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
    }

    /**
     * 上下文的参数集合
     */
    private final ContextMap bindings;
    /**
     * 生成后的 SQL
     */
    private final StringJoiner sqlBuilder = new StringJoiner(" ");
    /**
     * 唯一编号。
     */
    private int uniqueNumber = 0;

    /**
     * DynamicContext 构造器
     *
     * @param parameterObject 当需要使用到 OGNL 表达式时，parameterObject 非空
     */
    public DynamicContext(Object parameterObject) {
        // 初始化 bindings 参数
        bindings = new ContextMap();
        // 添加 bindings 的默认值
        bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    public void appendSql(String sql) {
        sqlBuilder.add(sql);
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }

    public int getUniqueNumber() {
        return uniqueNumber++;
    }

    /**
     * ContextMap ，是 DynamicContext 的内部静态类，继承 HashMap 类，上下文的参数集合。
     */
    static class ContextMap extends HashMap<String, Object> {
        private static final long serialVersionUID = 2977601501966151582L;

        public ContextMap() {
        }

        @Override
        public Object get(Object key) {
            String strKey = (String) key;
            // 如果有 key 对应的值，直接获得
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }
            return null;
        }
    }

    /**
     * ContextAccessor ，是 DynamicContext 的内部静态类，实现 ognl.PropertyAccessor 接口，上下文访问器。
     */
    static class ContextAccessor implements PropertyAccessor {

        @Override
        public Object getProperty(Map context, Object target, Object name) {
            Map map = (Map) target;

            // 优先从 ContextMap 中，获得属性
            Object result = map.get(name);
            if (map.containsKey(name) || result != null) {
                return result;
            }

            // 如果没有，则从 PARAMETER_OBJECT_KEY 对应的 Map 中，获得属性
            Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
            if (parameterObject instanceof Map) {
                return ((Map) parameterObject).get(name);
            }

            return null;
        }

        @Override
        public void setProperty(Map context, Object target, Object name, Object value) {
            Map<Object, Object> map = (Map<Object, Object>) target;
            map.put(name, value);
        }

        @Override
        public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }

        @Override
        public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }
    }
}
