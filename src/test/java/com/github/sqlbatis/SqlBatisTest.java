package com.github.sqlbatis;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test for sqlbatis
 *
 * @author Laba Zhang
 */
class SqlBatisTest {

    @Test
    void testParseForSql() {
        String xml = "<select id=\"findUserById\">\n" +
                "           select * from user where 1 = 1 \n" +
                "           <if test=\"id != null\">\n" +
                "               AND id = #{id}\n" +
                "           </if>\n" +
                "           <if test=\"username != null\">\n" +
                "               AND username like ${username}\n" +
                "           </if>\n" +
                "      </select>";
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        data.put("username", "'%zhang%'");
        SqlResult sqlResult = SqlBatis.parseForSql(xml, data);

        System.out.println(sqlResult.getFormatSql());
        System.out.println(sqlResult.getSql());
        System.out.println(sqlResult.getParams());
    }


}