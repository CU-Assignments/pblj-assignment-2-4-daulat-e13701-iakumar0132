import java.sql.*;
import java.util.Scanner;

class Student {
    int id;
    String name, dept;
    double marks;
    Student(int id, String name, String dept, double marks) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.marks = marks;
    }
}

class StudentController {
    private Connection con;

    StudentController() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "your_username", "your_password");
    }

    void create(Student s) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, s.id);
            ps.setString(2, s.name);
            ps.setString(3, s.dept);
            ps.setDouble(4, s.marks);
            ps.executeUpdate();
        }
    }

    void read() throws SQLException {
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM Student")) {
            while (rs.next()) System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getDouble(4));
        }
    }

    void update(int id, double marks) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE Student SET Marks = ? WHERE StudentID = ?")) {
            ps.setDouble(1, marks);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    void delete(int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM Student WHERE StudentID = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

public class StudentManagement {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in); StudentController ctrl = new StudentController()) {
            while (true) {
                System.out.println("1. Create 2. Read 3. Update 4. Delete 5. Exit");
                int ch = sc.nextInt();
                if (ch == 5) break;
                if (ch == 1) ctrl.create(new Student(sc.nextInt(), sc.next(), sc.next(), sc.nextDouble()));
                else if (ch == 2) ctrl.read();
                else if (ch == 3) ctrl.update(sc.nextInt(), sc.nextDouble());
                else if (ch == 4) ctrl.delete(sc.nextInt());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
