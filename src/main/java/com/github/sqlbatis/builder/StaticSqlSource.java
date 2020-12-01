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
package com.github.sqlbatis.builder;

import com.github.sqlbatis.mapping.BoundSql;
import com.github.sqlbatis.mapping.SqlSource;

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

    public StaticSqlSource(String sql) {
        this.sql = sql;
    }

    /**
     * 创建 BoundSql 对象。通过 parameterMappings 和 parameterObject 属性，可以设置 sql 上的每个占位符的值。
     *
     * @param parameterObject 参数对象
     * @return
     */
    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        // 创建 BoundSql 对象
        return new BoundSql(sql);
    }

}
