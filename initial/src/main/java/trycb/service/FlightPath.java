package trycb.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.query.dsl.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

@Service
public class FlightPath {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightPath.class);

    /**
     * Find all flight paths.
     */
    public static List<Map<String, Object>> findAll(final Bucket bucket, String from, String to, Calendar leave) {

        /*
         * WORKSHOP STEP 4: SQL-like Queries with Couchbase N1QL
         * Make a query that will get the source airport and destination faa code based on an airport name.
         * Hint, a union will be involved.
         */

        logQuery(query.toString());
        N1qlQueryResult result = bucket.query(N1qlQuery.simple(query));

        if (!result.finalSuccess()) {
            LOGGER.warn("Query returned with errors: " + result.errors());
            throw new DataRetrievalFailureException("Query error: " + result.errors());
        }

        String fromAirport = null;
        String toAirport = null;
        for (N1qlQueryRow row : result) {
            if (row.value().containsKey("fromAirport")) {
                fromAirport = row.value().getString("fromAirport");
            }
            if (row.value().containsKey("toAirport")) {
                toAirport = row.value().getString("toAirport");
            }
        }

        /*
         * WORKSHOP STEP 4: SQL-like Queries with Couchbase N1QL
         * Make a query that will get the airline name, flight, utc time, source and destination airports for
         * the route, and equipment.  There are three different document types involved in this query.
         */

        N1qlQueryResult otherResult = bucket.query(joinQuery);
        return extractResultOrThrow(otherResult);
    }

    /**
     * Extract a N1Ql result or throw if there is an issue.
     */
    private static List<Map<String, Object>> extractResultOrThrow(N1qlQueryResult result) {
        if (!result.finalSuccess()) {
            LOGGER.warn("Query returned with errors: " + result.errors());
            throw new DataRetrievalFailureException("Query error: " + result.errors());
        }

        List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
        for (N1qlQueryRow row : result) {
            content.add(row.value().toMap());
        }
        return content;
    }

    /**
     * Helper method to log the executing query.
     */
    private static void logQuery(String query) {
        LOGGER.info("Executing Query: {}", query);
    }
}
