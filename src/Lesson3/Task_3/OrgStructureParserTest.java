package Lesson3.Task_3;

import java.io.*;
import java.util.Scanner;

public class OrgStructureParserTest {
    public static void main(String[] args) throws IOException {
        File file1 = new File("src/Lesson3/Task_3/CSV_1.csv");          // Base example file
        File file2 = new File("src/Lesson3/Task_3/CSV_2.csv");          // Boss is the last in file
//        File file3 = new File("src/Lesson3/Task_3/CSV_3_empty.csv");    // Empty file (only for 2 var with full check)

        OrgStructureParser orgStructureParser = new OrgStructureParserImpl();

        System.out.println("\nFile 1 TEST");
        Employee boss = orgStructureParser.parseStructure(file1);
        employeePrinter(boss);

        System.out.println("\nFile 2 TEST");
        employeePrinter(orgStructureParser.parseStructure(file2));


//        testReadAndWrite();
    }

    private static void employeePrinter(Employee employee) {
        System.out.println("\nResult of parseStructure - getBoss:");
        System.out.println(" id=" + employee.getId()
                + ", bossId=" + employee.getBossId()
                + ", name=" + employee.getName()
                + ", position=" + employee.getPosition()
                + ",\n boss=" + employee.getBoss()
                + ", subordinate=" + employee.getSubordinate()
        );
    }

/*    private static void testReadAndWrite() throws IOException {
        File file1 = new File("src\\Lesson3\\Task_3\\CSV_0_test.csv");

        try (FileInputStream fileInputStream = new FileInputStream(file1)) {
            Scanner scanner = new Scanner(fileInputStream);
            while (scanner.hasNextLine()) {
                String stringFromFile = scanner.nextLine();
                System.out.println(stringFromFile);
            }
        }

        try (PrintWriter printWriter = new PrintWriter(file1)) {
            printWriter.println("WOWOWOWOW");
            printWriter.flush();
        }
    }*/
}
