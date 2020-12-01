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
package com.github.sqlbatis.scripting.defaults;

import com.github.sqlbatis.builder.SqlSourceBuilder;
import com.github.sqlbatis.mapping.BoundSql;
import com.github.sqlbatis.mapping.SqlSource;
import com.github.sqlbatis.scripting.xmltags.DynamicContext;
import com.github.sqlbatis.scripting.xmltags.DynamicSqlSource;
import com.github.sqlbatis.scripting.xmltags.SqlNode;

/**
 * Static SqlSource. It is faster than {@link DynamicSqlSource} because mappings are
 * calculated during startup.
 * <p>
 * 适用于仅使用 #{} 表达式，或者不使用任何表达式的情况，所以它是静态的，仅需要在构造方法中，直接生成对应的 SQL 。
 *
 * @author Eduardo Macarron
 * @since 3.2.0
 */
public class RawSqlSource implements SqlSource {
    /**
     * SqlSource 对象
     */
    private final SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {
        // 1. 获得 Sql：getSql()
        this(getSql(rootSqlNode));
    }

    /**
     * 创建 SqlSource，并将SQL中替换 #{} => ?
     *
     * @param sql           sql
     */
    public RawSqlSource(String sql) {
        // 2. 创建 SqlSourceBuilder 对象
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder();
        // 3. 获得 SqlSource 对象
        // 将占位符“#{}”，替换为“?”，并获取该占位符对应的 ParameterMapping 对象。
        // 同时创建SqlSource对象，类型是 StaticSqlSource 类。
        sqlSource = sqlSourceParser.parse(sql);
    }

    /**
     *
     * @param rootSqlNode
     * @return
     */
    private static String getSql(SqlNode rootSqlNode) {
        // 创建 DynamicContext 对象
        DynamicContext context = new DynamicContext(null);
        // 将 DynamicContext 应用 rootSqlNode，相当于生成动态 SQL 。
        rootSqlNode.apply(context);
        // 获得 sql
        return context.getSql();
    }

    /**
     * 获得 BoundSql 对象
     *
     * @param parameterObject 参数对象
     * @return BoundSql
     */
    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

}
