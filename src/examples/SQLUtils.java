package examples;

import java.sql.SQLException;
import java.sql.SQLWarning;

public class SQLUtils {
    public static String formatSQLException(SQLException e) {
        StringBuilder msg = new StringBuilder("");
        if (e != null) {
            msg.append(" SQLState: " + e.getSQLState() + "\n");
            msg.append("     Code: " + e.getErrorCode() + "\n");
            msg.append("  Message: " + e.getMessage() + "\n\n");
        }
        return msg.toString();
    }

    public static String formatSQLExceptions(SQLException e) {
        String msg = "";
        while (e != null) {
            msg += formatSQLException(e);
            e = e.getNextException();
        }
        return msg;
    }

    public static void printSQLErrors(SQLException e) {
        if (e != null) {
            System.err.println("SQL Error:\n" + formatSQLExceptions(e));
        }
    }

    public static boolean printSQLWarnings(SQLWarning w)
            throws SQLException {

        if (w != null) {
            System.out.println("SQL Warning:");
            while (w != null) {
                System.out.println(formatSQLException(w));
                w = w.getNextWarning();
            }
            return true;
        }
        else {
            return false;
        }
    }

    public static String rPadTrunc(String s, int len) {
        return rPadTrunc(s, len, ' ');
    }

    public static String rPadTrunc(String s, int len, char c) {
        if (s == null) {
            s = "";
        }

        if (len <= s.length()) {
            return s.substring(0, len);
        }

        StringBuilder sb = new StringBuilder(s);
        for (int k = 0; k < (len - s.length()); k++) {
            sb.append(c);
        }

        return sb.toString();
    }
}
