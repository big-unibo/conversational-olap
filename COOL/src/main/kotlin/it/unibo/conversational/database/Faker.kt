package it.unibo.conversational.database

import com.github.javafaker.Faker
import com.google.common.collect.Maps
import java.util.*

val r = Random(3)
val faker: Faker = Faker(r)

fun main() {
    // where to apply to the changes
    val cube = Config.getCube("lineorder2")
    // key = {table, original column, column to fake}, value = { how to fake it }
    val tables = mapOf(
            Triple("customer2", "originalcustomer", "customer") to {
                faker.name().firstName() + " " + faker.name().lastName()
            },
            Triple("supplier2", "originalsupplier", "supplier") to {
                faker.company().name()
            },
            Triple("part2", "partkey", "product") to {
                when (val ran = r.nextInt(25)) {
                     0 -> faker.commerce().productName()
                     1 -> faker.commerce().material()
                     2 -> faker.beer().name()
                     3 -> faker.book().title()
                     4 -> faker.food().dish()
                     5 -> faker.pokemon().name()
                     6 -> faker.animal().name()
                     7 -> faker.food().spice() + " " + faker.food().fruit()
                     8 -> faker.food().spice() + " " + faker.food().ingredient()
                     9 -> faker.food().spice() + " " + faker.food().spice()
                    10 -> faker.food().spice() + " " + faker.food().sushi()
                    11 -> faker.food().spice() + " " + faker.food().vegetable()
                    12 -> faker.food().spice() + " " + faker.food().measurement()
                    13 -> faker.color().name() + " " + faker.food().fruit()
                    14 -> faker.color().name() + " " + faker.food().ingredient()
                    15 -> faker.color().name() + " " + faker.food().spice()
                    16 -> faker.color().name() + " " + faker.food().sushi()
                    17 -> faker.color().name() + " " + faker.food().vegetable()
                    18 -> faker.color().name() + " " + faker.food().measurement()
                    19 -> faker.food().spice() + " " + faker.lorem().word()
                    20 -> faker.lorem().word() + " " + faker.lorem().word()
                    21 -> faker.color().name() + " " + faker.lorem().word()
                    22 -> faker.color().name() + " " + faker.pokemon().name()
                    23 -> faker.color().name() + " " + faker.animal().name()
                    24 -> faker.color().name() + " " + faker.pokemon().name()
                    else -> error("Not all cases have been covered, such as $ran")
                }
            },
    )

    tables.forEach { (k, v) ->
        println(k)
        // Map original value to fake ones
        val map = Maps.newLinkedHashMap<String, String>()
        DBmanager.executeMetaQuery(cube, "select distinct ${k.second} from ${k.first}") { res ->
            val acc: MutableSet<String> = mutableSetOf()
            var item: Int = 0
            while (res.next()) {
                // if (++item % 1000 == 0) println(item)
                var fakedval: String = v()
                var retry: Int = 0
                // make sure that no value is repeated
                while (acc.contains(fakedval)) {
                    if (retry++ == 10000) {
                        error("Cannot generate a unique value for $k. Generated values: ${acc.size}")
                    }
                    fakedval = v()
                }
                acc += fakedval
                map[res.getString(1)] = fakedval
            }
        }
        println("Updating...")
        // Update the fake values
        DBmanager.insertMeta(cube, "update ${k.first} set ${k.third}=? where ${k.second}=?") { pstmt ->
            map.forEach { (key, value) ->
                pstmt.setString(1, value)
                pstmt.setString(2, key);
                pstmt.addBatch()
            }
            // pstmt.executeLargeUpdate()
            pstmt.executeBatch()
        }
    }
}