package it.unibo.conversational.datatypes;

import com.google.common.base.Optional;
import it.unibo.conversational.database.Cube;
import org.jgrapht.Graph;
import org.jgrapht.alg.lca.NaiveLCAFinder;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class DependencyGraph {
    private static Graph<String, DefaultEdge> getCovidWeeklyDependencies() {
        final DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
        // DATE
        g.addVertex("week");
        g.addVertex("month");
        g.addEdge("week", "month");
        g.addVertex("year");
        g.addEdge("month", "year");
        g.addVertex("all_date");
        g.addEdge("year", "all_date");

        // COUNTRY
        g.addVertex("country");
        g.addVertex("continent");
        g.addEdge("country", "continent");
        g.addVertex("all_country");
        g.addEdge("continent", "all_country");
        return g;
    }

    private static Graph<String, DefaultEdge> getCovidMartDependencies() {
        final DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
        // DATE
        g.addVertex("daterep");
        g.addVertex("month");
        g.addEdge("daterep", "month");
        g.addVertex("year");
        g.addEdge("month", "year");
        g.addVertex("all_date");
        g.addEdge("year", "all_date");

        // COUNTRY
        g.addVertex("geoid");
        g.addVertex("countriesandterritories");
        g.addEdge("geoid", "countriesandterritories");
        g.addVertex("continentexp");
        g.addEdge("countriesandterritories", "continentexp");
        g.addVertex("all_country");
        g.addEdge("continentexp", "all_country");
        return g;
    }

    public static Graph<String, DefaultEdge> getDependencies(final Cube cube) {
        switch (cube.getFactTable()) {
            case "ft":
                return getCovidWeeklyDependencies();
            case "covid":
                return getCovidMartDependencies();
            case "sales_fact_1997":
                return getFoodMartDependencies();
            case "ssb_test_lineorder":
            case "lineorder2": // ssb cube
                return getSSBDependencies();
        }
        throw new IllegalArgumentException(DependencyGraph.class + ": unknown schema " + cube.getFactTable());
    }

    private static Graph<String, DefaultEdge> getSSBDependencies() {
        final DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
        // PRODUCT
        g.addVertex("partkey");
        g.addVertex("product");
        g.addEdge("partkey", "product");
        g.addVertex("brand");
        g.addEdge("product", "brand");
        g.addVertex("category");
        g.addEdge("brand", "category");
        g.addVertex("allproducts");
        g.addEdge("category", "allproducts");
        // CUSTOMER
        g.addVertex("custkey");
        g.addVertex("customer");
        g.addEdge("custkey", "customer");
        g.addVertex("nation");
        g.addEdge("customer", "nation");
        g.addVertex("population");
        g.addVertex("region");
        g.addEdge("nation", "population");
        g.addEdge("nation", "region");
        g.addVertex("allcustomers");
        g.addEdge("region", "allcustomers");
        // SUPPLIER
        g.addVertex("suppkey");
        g.addVertex("supplier");
        g.addEdge("suppkey", "supplier");
        g.addVertex("s_nation");
        g.addEdge("supplier", "s_nation");
        g.addVertex("s_region");
        g.addEdge("s_nation", "s_region");
        g.addVertex("allsuppliers");
        g.addEdge("s_region", "allsuppliers");
        // DATE
        g.addVertex("datekey");
        g.addVertex("date");
        g.addEdge("datekey", "date");
        g.addVertex("month");
        g.addEdge("date", "month");
        g.addVertex("year");
        g.addEdge("month", "year");
        g.addVertex("alldates");
        g.addEdge("year", "alldates");
        return g;
    }

    private static Graph<String, DefaultEdge> getFoodMartDependencies() {
        final DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
        // PRODUCT
        g.addVertex("product_id");
        g.addVertex("brand_name");
        g.addEdge("product_id", "brand_name");
        g.addVertex("product_name");
        g.addEdge("product_id", "product_name");
        g.addVertex("product_subcategory");
        g.addEdge("product_id", "product_subcategory");
        g.addVertex("product_category");
        g.addEdge("product_subcategory", "product_category");
        g.addVertex("product_department");
        g.addEdge("product_subcategory", "product_department");
        g.addVertex("product_family");
        g.addEdge("product_subcategory", "product_family");
        g.addVertex("all_product");
        g.addEdge("product_category", "all_product");
        g.addEdge("product_department", "all_product");
        g.addEdge("product_family", "all_product");
        // STORE
        g.addVertex("store_id");
        g.addVertex("store_name");
        g.addEdge("store_id", "store_name");
        g.addVertex("florist");
        g.addEdge("store_id", "florist");
        g.addVertex("coffee_bar");
        g.addEdge("store_id", "coffee_bar");
        g.addVertex("salad_bar");
        g.addEdge("store_id", "salad_bar");
        g.addVertex("video_store");
        g.addEdge("store_id", "video_store");
        g.addVertex("store_type");
        g.addEdge("store_id", "store_type");
        g.addVertex("store_city");
        g.addEdge("store_id", "store_city");
        g.addVertex("store_state");
        g.addEdge("store_city", "store_state");
        g.addVertex("store_country");
        g.addEdge("store_state", "store_country");
        g.addVertex("all_store");
        g.addEdge("store_country", "all_store");
        g.addEdge("store_name", "all_store");
        g.addEdge("store_type", "all_store");
        g.addEdge("florist", "all_store");
        g.addEdge("coffee_bar", "all_store");
        g.addEdge("salad_bar", "all_store");
        g.addEdge("video_store", "all_store");
        // CUSTOMER
        g.addVertex("customer_id");
        g.addVertex("yearly_income");
        g.addEdge("customer_id", "yearly_income");
        g.addVertex("fullname");
        g.addEdge("customer_id", "fullname");
        g.addVertex("member_card");
        g.addEdge("customer_id", "member_card");
        g.addVertex("gender");
        g.addEdge("customer_id", "gender");
        g.addVertex("occupation");
        g.addEdge("customer_id", "occupation");
        g.addVertex("marital_status");
        g.addEdge("customer_id", "marital_status");
        g.addVertex("city");
        g.addEdge("customer_id", "city");
        g.addVertex("state_province");
        g.addEdge("city", "state_province");
        g.addVertex("country");
        g.addEdge("state_province", "country");
        g.addVertex("population");
        g.addEdge("country", "population");
        g.addVertex("all_customer");
        g.addEdge("yearly_income", "all_customer");
        g.addEdge("fullname", "all_customer");
        g.addEdge("member_card", "all_customer");
        g.addEdge("gender", "all_customer");
        g.addEdge("occupation", "all_customer");
        g.addEdge("marital_status", "all_customer");
        g.addEdge("country", "all_customer");
        // DATE
        g.addVertex("time_id");
        g.addVertex("the_date");
        g.addEdge("time_id", "the_date");
        g.addVertex("the_month");
        g.addEdge("the_date", "the_month");
        g.addVertex("quarter");
        g.addEdge("the_date", "quarter");
        g.addVertex("the_year");
        g.addEdge("the_month", "the_year");
        g.addEdge("quarter", "the_year");
        g.addVertex("all_time_by_day");
        g.addEdge("the_year", "all_time_by_day");
        // PROMOTION
        g.addVertex("promotion_id");
        return g;
    }

    public static Optional<String> lca(final Cube cube, final String s1, final String s2) {
        final NaiveLCAFinder<String, DefaultEdge> lca = new NaiveLCAFinder<String, DefaultEdge>(getDependencies(cube));
        return Optional.fromNullable(lca.getLCA(s1.toLowerCase(), s2.toLowerCase()));
    }
}
