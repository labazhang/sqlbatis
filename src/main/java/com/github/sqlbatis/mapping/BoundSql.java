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
package com.github.sqlbatis.mapping;

import com.github.sqlbatis.SqlResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An actual SQL String got from an {@link SqlSource} after having processed any dynamic content.
 * The SQL may have SQL placeholders "?" and an list (ordered) of an parameter mappings
 * with the additional information for each parameter (at least the property name of the input object to read
 * the value from).
 * <p>
 * Can also have additional parameters that are created by the dynamic language (for loops, bind...).
 * 一次可执行的 SQL 封装。
 * <p>
 * 在 BoundSql中封装了可执行的SQL语句，例如：select * from user where id = ?
 * 同时，还会持有此条SQL的参数类型，使用{@link ParameterMapping} 进行管理，其中包含参数的名称，参数的类型
 * 还包括用户在使用此SQL是传的参数。例如：1
 *
 * @author Clinton Begin
 */
public class BoundSql {

    /**
     * SQL 语句
     */
    private final String sql;
    /**
     * 排序后的参数 {@link SqlResult#setParams(Map)}
     */
    private Map<Integer, Object> orderedParams;

    public BoundSql(String sql) {
        this(sql, new HashMap<>());
    }

    public BoundSql(String sql, Map<Integer, Object> orderedParams) {
        this.sql = sql;
        this.orderedParams = orderedParams;
    }

    public String getSql() {
        return sql;
    }

    public Map<Integer, Object> getOrderedParams() {
        return orderedParams;
    }
}
