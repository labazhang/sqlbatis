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
package com.github.sqlbatis.mapping;

/**
 * 参数映射。
 *
 * @author Clinton Begin
 */
public class ParameterMapping {

    /**
     * 属性的名字
     */
    private String property;

    /**
     * Java 类型
     */
    private String javaType;


    private ParameterMapping() {
    }

    public String getProperty() {
        return property;
    }

    /**
     * Used for handling output of callable statements.
     *
     * @return
     */
    public String getJavaType() {
        return javaType;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ParameterMapping{");
        sb.append("property='").append(property).append('\'');
        sb.append(", javaType=").append(javaType);
        sb.append('}');
        return sb.toString();
    }
}
