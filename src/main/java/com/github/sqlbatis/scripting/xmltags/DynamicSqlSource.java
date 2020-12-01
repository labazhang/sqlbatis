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

import com.github.sqlbatis.builder.SqlSourceBuilder;
import com.github.sqlbatis.mapping.BoundSql;
import com.github.sqlbatis.mapping.SqlSource;

/**
 * 实现 SqlSource 接口，动态的 SqlSource 实现类。
 * <p>
 * 适用于使用了 OGNL 表达式，或者使用了 ${} 表达式的 SQL ，
 * 所以它是动态的，需要在每次执行 #getBoundSql(Object parameterObject) 方法，根据参数，生成对应的 SQL 。
 *
 * @author Clinton Begin
 */
public class DynamicSqlSource implements SqlSource {

    /**
     * 根 SqlNode 对象
     */
    private final SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        // 1. 创建DynamicContext，并应用 rootSqlNode，相当于生成动态 SQL 。
        DynamicContext context = new DynamicContext(parameterObject);
        rootSqlNode.apply(context);
        // 2. 创建 SqlSourceBuilder 对象
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder();
        // 3. 将占位符“#{}”，替换为“?”，并获取该占位符对应的 ParameterMapping 对象。
        // 同时创建SqlSource对象，类型是 StaticSqlSource 类。
        SqlSource sqlSource = sqlSourceParser.parse(context.getSql());
        // 4. 返回 BoundSql 对象return boundSql;
        return sqlSource.getBoundSql(parameterObject);
    }

}
