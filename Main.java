import java.util.Scanner;
public class Main {
    public static String url = "jdbc:mysql://MySQL-8.0/collage";
    public static String user = "root";
    public static String password = "";

    public static void main(String[] args) {
        MyDataBase myDataBase = new MyDataBase();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Добавить студента");
            System.out.println("2. Вывести отсортированный список студентов с вариантами");
            System.out.println("3. Вывести список студентов, сгруппированных по вариантам");
            System.out.println("4. Вывести вариант студента по фамилии");
            System.out.println("5. Вывести список студентов с определенным вариантом");
            System.out.println("6. Вывести количество студентов с каждым вариантом");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Введите фамилию: ");
                    String surname = scanner.nextLine();
                    System.out.print("Введите имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Введите отчество: ");
                    String lastname = scanner.nextLine();
                    System.out.print("Введите ID группы: ");
                    int id_group = scanner.nextInt();
                    scanner.nextLine();
                    myDataBase.addStudent(surname, name, lastname, id_group);
                    break;
                case 2:
                    myDataBase.showStudentsWithRandomVariants();
                    RandomVar randomVar = new RandomVar();
                    randomVar.randVar();
                    break;
                case 3:
                    myDataBase.showStudentsGroupedByVariant();
                    break;
                case 4:
                    System.out.print("Введите фамилию: ");
                    String studentSurname = scanner.nextLine();
                    scanner.nextLine();
                    myDataBase.showStudentVariantBySurname(studentSurname);
                    break;
                case 5:
                    System.out.print("Введите вариант: ");
                    int variant  = scanner.nextInt();
                    scanner.nextInt();
                    myDataBase.showStudentsByVariant(variant);
                    break;
                case 6:
                    myDataBase.showStudentCountByVariant();
                    break;
            }
        }
    }
}
