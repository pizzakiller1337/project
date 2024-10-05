import java.sql.*;
import java.util.List;
import java.util.Random;

public class MyDataBase {
    public static String url = "jdbc:mysql://MySQL-8.0/collage";
    public static String user = "root";
    public static String password = "";
    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void addStudent(String surname, String name, String lastname, int id_group) {
        Connection connection = null;
        try {
            connection = openConnection();
            String query = "INSERT INTO `Student` (`id_student`, `surname`, `name`, `lastname`, `id_group`) " +
                    "VALUES (NULL, ? , ? , ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, surname);
            statement.setString(2, name);
            statement.setString(3, lastname);
            statement.setInt(4, id_group);
            int rowsTrue = statement.executeUpdate();
            if (rowsTrue > 0) System.out.println("Добавлен");
            else System.out.println("не хочу работать");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
    public void showStudentsWithRandomVariants() {
        Connection connection = null;
        try {
            connection = openConnection();
            String query = "SELECT id_group, surname, name, lastname FROM Student ORDER BY id_group, surname";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            Random random = new Random();
            while (resultSet.next()) {
                int id_group = resultSet.getInt("id_group");
                String surname = resultSet.getString("surname");
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastname");
                int variant = random.nextInt(10)+1;
                System.out.println("Группа: " + id_group + ", " + surname + " " + name + " " + lastname + " - Вариант: " + variant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
    public void selectOneColum(String columName, String tableName, List<Integer> arr)
    {
        arr.clear();
        Connection connection = null;
        try {
            connection = openConnection();
            String query = "SELECT `"+columName+"` FROM `"+tableName+"`;";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                int a = resultSet.getInt(columName);
                arr.add(a);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            closeConnection(connection);
        }
    }

    public void addVariant()
    {
        RandomVar randomVar = new RandomVar();
        selectOneColum("id_student", "Student", randomVar.idArray);
        randomVar.randVar();
        Connection connection = null;
        try {
            connection = openConnection();
            String query = "SELECT COUNT(*) FROM `Variation`";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                if (resultSet.getInt(1)!=0)
                {//Если есть записи в таблице, то удалить все
                    query = "DELETE FROM `Variation`";
                    statement.executeUpdate(query);
                }
                for (int i=0; i<randomVar.idArray.size();i++)
                {
                    query = "INSERT INTO `Variation` (`id_var`, `id_student`, `variations`) " +
                            "VALUES (NULL, "+randomVar.idArray.get(i)+" ,"+randomVar.arr[i]+");";
                    statement = connection.prepareStatement(query);
                    statement.executeUpdate();
                }
            }
        }

        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            closeConnection(connection);
        }
    }
    public void showStudentsGroupedByVariant() {
        Connection connection = null;
        try {
            connection = openConnection();
            String query = "SELECT Student.surname, Student.name, Student.lastname, Variation.variations " +
                    "FROM Student INNER JOIN Variation ON Student.id_student = Variation.id_student " +
                    "ORDER BY Variation.variations, Student.surname, Student.name, Student.lastname";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            int currentVariant = -1;
            while (resultSet.next()) {
                int variant = resultSet.getInt("variations");
                String surname = resultSet.getString("surname");
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastname");

                if (variant != currentVariant) {
                    if (currentVariant != -1) {
                        System.out.println();
                    }
                    currentVariant = variant;
                    System.out.println("Вариант " + variant + ":");
                }

                System.out.println(" " + surname + " " + name + " " + lastname);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
    public void showStudentVariantBySurname(String surname) {
        Connection connection = null;
        try {
            connection = openConnection();
            String query = "SELECT Student.surname, Student.name, Student.lastname, Variation.id_var " +
                    "FROM Student INNER JOIN Variation ON Student.id_student = Variation.id_student WHERE Student.surname = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, surname);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String studentSurname = resultSet.getString("surname");
                String studentName = resultSet.getString("name");
                String studentLastname = resultSet.getString("lastname");
                int variant = resultSet.getInt("variations");

                System.out.println("Студент: " + studentSurname + " " + studentName + " " + studentLastname);
                System.out.println("Вариант: " + variant);
            } else {
                System.out.println("Студент с такой фамилией не найден.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
    public void showStudentsByVariant(int variant) {
        Connection connection = null;
        try {
            connection = openConnection();
            String query = "SELECT Student.surname, Student.name, Student.lastname " +
                    "FROM Student INNER JOIN Variation ON Student.id_student = Variation.id_student " +
                    "WHERE Variation.variations = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, variant - 1);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Студенты с вариантом " + variant + ":");
                do {
                    String surname = resultSet.getString("surname");
                    String name = resultSet.getString("name");
                    String lastname = resultSet.getString("lastname");
                    System.out.println(" " + surname + " " + name + " " + lastname);
                } while (resultSet.next());
            } else {
                System.out.println("Студенты с вариантом " + variant + " не найдены.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
    public void showStudentCountByVariant() {
        Connection connection = null;
        try {
            connection = openConnection();
            String query = "SELECT Variation.variations, COUNT(*) AS student_count " +
                    "FROM Variation GROUP BY Variation.variations ORDER BY Variation.variations";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int variant = resultSet.getInt("variations") + 1;
                int count = resultSet.getInt("student_count");
                System.out.println("Вариант " + variant + ": " + count + " студентов");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }
}

