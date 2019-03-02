package fractal.code.sql;

/**
 * Created by sorin.nica in August 2016
 */
public class Clause {

    private static final String AND = " AND ";

    private static final String OR = " OR ";

    private static final String NOT = "NOT";

    private static final String IN = " IN ";

    private static final String NOT_IN = " NOT IN ";

    private static final String LIKE = " LIKE ";

    private static final String OPEN_PARAN = "(";

    private static final String CLOSE_PARAN = ")";

    private final StringBuilder sb;

    private int clauseCount;

    private Clause(String firstClause) {
        sb = new StringBuilder(firstClause);
        clauseCount = sb.length() == 0 ? 0 : 1;
    }

    public static Clause of(String firstClause) {
        return new Clause(firstClause);
    }

    public static Clause empty() {
        return new Clause("");
    }

    public static String not(String negatedClause) {
        Clause clause = new Clause(negatedClause);
        clause.sb.insert(0, OPEN_PARAN).append(CLOSE_PARAN);
        clause.sb.insert(0, NOT);
        return clause.toString();
    }

    public static String in(String columnName, String concatenatedValues) {
        Clause clause = new Clause(columnName);
        clause.sb.append(IN).append(OPEN_PARAN).append(concatenatedValues).append(CLOSE_PARAN);
        return clause.toString();
    }

    public static String notIn(String columnName, String concatenatedValues) {
        Clause clause = new Clause(columnName);
        clause.sb.append(NOT_IN).append(OPEN_PARAN).append(concatenatedValues).append(CLOSE_PARAN);
        return clause.toString();
    }

    public static String like(String columnName, String concatenatedValues) {
        Clause clause = new Clause(columnName);
        clause.sb.append(LIKE).append("'").append("%").append(concatenatedValues).append("%").append("'");
        return clause.toString();
    }

    public Clause and(Clause clause) {
        return appendClause(clause, AND);
    }

    public Clause and(String clause) {
        return appendClause(clause, AND);
    }

    public Clause or(String clause) {
        return appendClause(clause, OR);
    }

    public Clause or(Clause clause) {
        return appendClause(clause, OR);
    }

    private Clause appendClause(String clause, String operator) {
        if (sb.length() != 0) sb.append(operator);
        sb.append(clause);
        clauseCount++;
        return this;
    }

    private Clause appendClause(Clause clause, String operator) {
        enclose();
        return appendClause(clause.enclose().toString(), operator);
    }

    public Clause enclose() {
        if (clauseCount > 1) sb.insert(0, OPEN_PARAN).append(CLOSE_PARAN);
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
