
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;
import java.util.Properties;

public class NetworkDiagnostic {
    public static void main(String[] args) {
        String host = "aws-1-ap-northeast-1.pooler.supabase.com";
        String user = "postgres.cwitbyqtrqcyhgmanfrm";
        String password = "1qazZAQ!q1w2e3r4!";

        System.out.println("=".repeat(70));
        System.out.println("🔍 NETWORK DIAGNOSTIC TOOL");
        System.out.println("=".repeat(70));
        System.out.println("Host: " + host);
        System.out.println("User: " + user);
        System.out.println("Password length: " + password.length() + " characters");
        System.out.println();

        // Test 1: DNS Resolution
        System.out.println("📡 TEST 1: DNS Resolution");
        try {
            InetAddress[] addresses = InetAddress.getAllByName(host);
            System.out.println("   ✅ Host resolved to:");
            for (InetAddress addr : addresses) {
                System.out.println("      - " + addr.getHostAddress());
            }
        } catch (Exception e) {
            System.err.println("   ❌ DNS resolution failed: " + e.getMessage());
            return;
        }
        System.out.println();

        // Test 2: Port Connectivity - 5432
        System.out.println("🔌 TEST 2: Port Connectivity - Session Pooler (5432)");
        testPort(host, 5432);
        System.out.println();

        // Test 3: Port Connectivity - 6543
        System.out.println("🔌 TEST 3: Port Connectivity - Transaction Pooler (6543)");
        testPort(host, 6543);
        System.out.println();

        // Test 4: SSL Connection Test
        System.out.println("🔐 TEST 4: SSL Connection Test");
        testSSLConnection(host, 6543, user, password);
        System.out.println();

        // Test 5: Direct JDBC Connection
        System.out.println("📊 TEST 5: Direct JDBC Connection");
        testDirectConnection(host, 6543, user, password);
        System.out.println();

        // Test 6: Simple Query
        System.out.println("📝 TEST 6: Simple Query Test");
        testSimpleQuery(host, 6543, user, password);
        System.out.println();

        // Test 7: Network Route
        System.out.println("🌐 TEST 7: Network Route Info");
        try {
            Process process = Runtime.getRuntime().exec("traceroute -m 15 " + host);
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream())
            );
            String line;
            System.out.println("   Traceroute output (first 5 hops):");
            int count = 0;
            while ((line = reader.readLine()) != null && count < 10) {
                System.out.println("   " + line);
                count++;
            }
        } catch (Exception e) {
            System.out.println("   ⚠️ Traceroute not available: " + e.getMessage());
        }
        System.out.println();

        System.out.println("=".repeat(70));
        System.out.println("📋 RECOMMENDATIONS:");
        System.out.println("=".repeat(70));
        System.out.println("1. Check if your IP is allowed in Supabase Dashboard");
        System.out.println("   → Go to: https://app.supabase.com/project/cwitbyqtrqcyhgmanfrm/settings/database");
        System.out.println("   → Look for 'Network Restrictions' section");
        System.out.println();
        System.out.println("2. Get your current IP address:");
        System.out.println("   → Run: curl ifconfig.me");
        System.out.println("   → Or visit: https://ifconfig.me");
        System.out.println();
        System.out.println("3. Try connecting from a different network (e.g., mobile hotspot)");
        System.out.println();
        System.out.println("4. Verify Supabase project status:");
        System.out.println("   → Check if project is paused in Supabase Dashboard");
        System.out.println();
        System.out.println("5. Test with SSL disabled (temporary, for diagnosis only):");
        System.out.println("   jdbc:postgresql://" + host + ":6543/postgres?sslmode=disable");
    }

    private static void testPort(String host, int port) {
        try (Socket socket = new Socket()) {
            long start = System.currentTimeMillis();
            socket.connect(new java.net.InetSocketAddress(host, port), 5000);
            long end = System.currentTimeMillis();
            System.out.println("   ✅ Port " + port + " is open (connected in " + (end-start) + "ms)");
        } catch (Exception e) {
            System.err.println("   ❌ Port " + port + " is not reachable: " + e.getMessage());
            if (e.getMessage().contains("Connection timed out")) {
                System.err.println("      → Connection timeout - firewall or network issue");
            } else if (e.getMessage().contains("Connection refused")) {
                System.err.println("      → Port is closed - Supabase might not be accepting connections");
            }
        }
    }

    private static void testSSLConnection(String host, int port, String user, String password) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/postgres?sslmode=require&connectTimeout=10";

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("   ✅ PostgreSQL JDBC driver loaded");

            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            props.setProperty("ssl", "true");
            props.setProperty("sslmode", "require");
            props.setProperty("connectTimeout", "10");

            long start = System.currentTimeMillis();
            DriverManager.getConnection(url, props);
            long end = System.currentTimeMillis();
            System.out.println("   ✅ SSL Connection successful (" + (end-start) + "ms)");

        } catch (ClassNotFoundException e) {
            System.err.println("   ❌ PostgreSQL driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("   ❌ SSL Connection failed: " + e.getMessage());
            System.err.println("      SQL State: " + e.getSQLState());
            System.err.println("      Error Code: " + e.getErrorCode());

            if (e.getMessage().contains("timeout")) {
                System.err.println("      → Connection timeout - network issue");
            } else if (e.getMessage().contains("SSL")) {
                System.err.println("      → SSL handshake failed");
            } else if (e.getMessage().contains("authentication")) {
                System.err.println("      → Authentication failed - check credentials");
            }
        }
    }

    private static void testDirectConnection(String host, int port, String user, String password) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/postgres?sslmode=require&connectTimeout=10";

        try {
            long start = System.currentTimeMillis();
            Connection conn = DriverManager.getConnection(url, user, password);
            long end = System.currentTimeMillis();
            System.out.println("   ✅ Direct connection successful (" + (end-start) + "ms)");
            conn.close();
        } catch (SQLException e) {
            System.err.println("   ❌ Direct connection failed: " + e.getMessage());

            if (e.getMessage().contains("timeout")) {
                System.err.println("      → Connection timeout - check network/firewall");
            } else if (e.getMessage().contains("password")) {
                System.err.println("      → Password authentication failed");
            } else if (e.getMessage().contains("database")) {
                System.err.println("      → Database not found");
            }
        }
    }

    private static void testSimpleQuery(String host, int port, String user, String password) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/postgres?sslmode=require&connectTimeout=10";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Test basic query
            ResultSet rs = stmt.executeQuery("SELECT 1 as test, current_timestamp, version()");
            if (rs.next()) {
                System.out.println("   ✅ Basic query successful:");
                System.out.println("      Test value: " + rs.getInt("test"));
                System.out.println("      Server time: " + rs.getTimestamp("current_timestamp"));
                System.out.println("      PostgreSQL version: " + rs.getString("version"));
            }

            // Test connection settings
            rs = stmt.executeQuery("SHOW server_version");
            if (rs.next()) {
                System.out.println("      Server version: " + rs.getString(1));
            }

        } catch (SQLException e) {
            System.err.println("   ❌ Query failed: " + e.getMessage());
        }
    }
}