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

import com.github.sqlbatis.mapping.SqlSource;
import com.github.sqlbatis.parsing.GenericTokenParser;
import com.github.sqlbatis.parsing.TokenHandler;

/**
 * 继承 BaseBuilder 抽象类，SqlSource 构建器，
 * 负责将 SQL 语句中的 #{} 替换成相应的 ? 占位符，
 * 并获取该 ? 占位符对应的 org.apache.ibatis.mapping.ParameterMapping 对象。
 *
 * @author Clinton Begin
 */
public class SqlSourceBuilder extends BaseBuilder {

    public SqlSourceBuilder() {
        super();
    }

    /**
     * 执行解析原始 SQL ，成为 SqlSource 对象
     *
     * @param originalSql 原始 SQL
     * @return SqlSource 对象
     * <p>
     * 感叹：设计模式真的是随手拈来。
     */
    public SqlSource parse(String originalSql) {
        // 1. 创建 ParameterMappingTokenHandler 对象
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        // 2. 创建 GenericTokenParser 对象，并指定左右token符号，作为站位符查找边界。
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        // 3. 执行解析，并会调用 ParameterMappingTokenHandler 的 handleToken 方法
        GenericTokenParser.TokenResult result = parser.parseForResult(originalSql);
        // 4. 创建 StaticSqlSource 对象
        return new StaticSqlSource(result.getSql(),result.getPlaceHolderParams());
    }

    /**
     * 实现 TokenHandler 接口，继承 BaseBuilder 抽象类，
     * 负责将匹配到的 #{ 和 } 对，替换成相应的 ? 占位符，
     * 并获取该 ? 占位符对应的 org.apache.ibatis.mapping.ParameterMapping 对象。
     */
    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        public ParameterMappingTokenHandler() {
            super();
        }

        @Override
        public String handleToken(String content) {
            // <1> 返回 ? 占位符
            return "?";
        }
    }
}
