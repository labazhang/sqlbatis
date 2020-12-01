/**
 * Copyright 2020-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.sqlbatis.builder;

import com.github.sqlbatis.mapping.BoundSql;
import com.github.sqlbatis.mapping.SqlSource;
import com.github.sqlbatis.scripting.xmltags.ForEachSqlNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现 SqlSource 接口，静态的 SqlSource 实现类。
 * <p>
 * StaticSqlSource 的静态，是相对于 DynamicSqlSource 和 RawSqlSource 来说呢。
 * 实际上，StaticSqlSource.sql 属性，上面还是可能包括 ? 占位符。
 *
 * @author Clinton Begin
 */
public class StaticSqlSource implements SqlSource {

    /**
     * 静态的 SQL
     */
    private final String sql;
    /**
     * 按照顺序存储动态SQL中 #{} 参数名称
     */
    private List<String> placeHolderParams;

    public StaticSqlSource(String sql) {
        this.sql = sql;
        placeHolderParams = new ArrayList<>();
    }

    public StaticSqlSource(String sql, List<String> placeHolderParams) {
        this.sql = sql;
        this.placeHolderParams = placeHolderParams;
    }

    /**
     * 创建 BoundSql 对象。通过 parameterMappings 和 parameterObject 属性，可以设置 sql 上的每个占位符的值。
     *
     * @param parameterObject 参数对象
     * @return
     */
    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        Map<Integer, Object> orderedParams = new HashMap<>(placeHolderParams.size());
        if (placeHolderParams.size() > 0) {
            Map object = (Map) parameterObject;
            for (int i = 0; i < placeHolderParams.size(); i++) {
                String placeHolderParam = placeHolderParams.get(i);
                // 处理 foreach
                if (placeHolderParam.startsWith(ForEachSqlNode.ITEM_PREFIX)) {
                    //         -3        -2   -1
                    // __frch_collection_item_0
                    // to do: need to consider map and list
                    String[] split = placeHolderParam.split("_");
                    int index = Integer.parseInt(split[split.length - 1]);
                    String paramName = split[split.length - 3];
                    List list = (List) object.get(paramName);
                    if (index <= list.size()) {
                        orderedParams.put(i + 1, list.get(index));
                    } else {
                        orderedParams.put(i + 1, null);
                    }
                } else {
                    orderedParams.put(i + 1, object.getOrDefault(placeHolderParam, null));
                }
            }
        }

        // 创建 BoundSql 对象
        return new BoundSql(sql, orderedParams);
    }

}
