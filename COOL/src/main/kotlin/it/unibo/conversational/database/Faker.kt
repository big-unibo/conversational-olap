package it.unibo.conversational.database

import com.github.javafaker.Faker
import com.google.common.collect.Lists
import com.google.common.collect.Maps

val faker: Faker = Faker()

fun main() {
    // where to apply to the changes
    val cube = Config.getCube("lineorder2")
    // tables to update
    val tables = Lists.newArrayList("supplier") // "customer",
    // key = column to fake, value = how to fake it
    val fake = mapOf(
            "customer" to { faker.name().firstName() + " " + faker.name().lastName() },
            "supplier" to { faker.company().name() }
    )

    tables.forEach { table ->
        val map = Maps.newLinkedHashMap<String, String>()
        DBmanager.executeMetaQuery(cube, "select $table from ${table}2") { res ->
            while (res.next()) {
                map[res.getString(1)] = (fake[table] ?: error("$table not found"))()
            }
        }

        DBmanager.insertMeta(cube, "update ${table}2 set ${table}=? where original${table}=?") { pstmt ->
            map.forEach { (key, value) ->
                pstmt.setString(1, value)
                pstmt.setString(2, key);
                pstmt.addBatch()
            }
            pstmt.executeBatch()
        }
    }
}