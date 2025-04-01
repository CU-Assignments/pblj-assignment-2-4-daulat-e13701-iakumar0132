import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static String url = "jdbc:mysql://localhost:3306/your_database";
    static String user = "your_username";
    static String password = "your_password";

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(url, user, password);
             Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("1. Create 2. Read 3. Update 4. Delete 5. Exit");
                int choice = sc.nextInt();
                if (choice == 5) break;
                con.setAutoCommit(false);
                try (PreparedStatement ps = switch (choice) {
                    case 1 -> con.prepareStatement("INSERT INTO Product VALUES (?, ?, ?, ?)");
                    case 2 -> con.prepareStatement("SELECT * FROM Product");
                    case 3 -> con.prepareStatement("UPDATE Product SET Price = ? WHERE ProductID = ?");
                    case 4 -> con.prepareStatement("DELETE FROM Product WHERE ProductID = ?");
                    default -> throw new IllegalStateException("Invalid choice");
                }) {
                    if (choice == 1) {
                        ps.setInt(1, sc.nextInt());
                        ps.setString(2, sc.next());
                        ps.setDouble(3, sc.nextDouble());
                        ps.setInt(4, sc.nextInt());
                        ps.executeUpdate();
                    } else if (choice == 2) {
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getDouble(3) + " " + rs.getInt(4));
                        }
                    } else if (choice == 3 || choice == 4) {
                        ps.setDouble(1, sc.nextDouble());
                        if (choice == 3) ps.setInt(2, sc.nextInt());
                        ps.executeUpdate();
                    }
                    con.commit();
                } catch (Exception e) {
                    con.rollback();
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
