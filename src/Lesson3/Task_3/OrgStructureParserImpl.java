package Lesson3.Task_3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

    /*
    Has 2 variants of file check when parsed:
    1 var - MINIMAL check of file: structure, formats, spaces, data requirements
    2 var - FULL Exceptions check for bad formatted files
     */

public class OrgStructureParserImpl implements OrgStructureParser {
    Map<Long, Employee> employeeMap = new HashMap<>();

/*
1 var - MINIMAL check of file: structure, formats, spaces, data requirements
 */
    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        Employee result = null;

        try (FileInputStream fileInputStream = new FileInputStream(csvFile)) {
            Scanner scanner = new Scanner(fileInputStream);
            boolean headerLineSkip = false;      // header line skip flag

            while (scanner.hasNextLine()) {
                String stringFromFile = scanner.nextLine();

                if (!headerLineSkip) {          // need to skip the header line
                    headerLineSkip = true;
                    continue;
                }

                List<String> personDataList = Arrays.stream(stringFromFile.trim().split(";")).toList();
                Employee employee;   // id, bossId, name, position

                try {
                    employee = new Employee(    // Was added constructor with 4 parameters
                        Long.parseLong(personDataList.get(0)),
                        (personDataList.get(1).isEmpty() ? null : Long.parseLong(personDataList.get(1))),
                        personDataList.get(2),
                        personDataList.get(3)
                        );
                } catch (Exception e) {
                    throw new IOException("CSV-file error: empty data or wrong format!");
                }

                if (personDataList.get(1).isEmpty()) {
                    result = employee;          // Boss detected
                }

                employeeMap.put(employee.getId(), employee);
            }
        }

        // Generates the structure of organization
        orgStructureGenerate(employeeMap);

        // Print the final employees' structure (OPTIONAL)
        orgStructurePrint(employeeMap);

        return result;
    }


/*
    2 var - is Alternative: with FULL Exceptions check for bad formatted files
 */

/*
    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        Employee result = null;

        try (FileInputStream fileInputStream = new FileInputStream(csvFile)) {
            Scanner scanner = new Scanner(fileInputStream);
            boolean headerLineSkip = false;      // header line skipped flag

            while (scanner.hasNextLine()) {
                String stringFromFile = scanner.nextLine();

                if (!headerLineSkip) {
                    if (stringFromFile.equals("id;boss_id;name;position")) { // file header format check
                        headerLineSkip = true;
                        continue;
                    } else {
                        throw new IOException("CSV-file error: wrong format of header!");
                    }
                }

                List<String> personDataList = Arrays.stream(stringFromFile.trim().split("\s*[;]\s*")).toList();
                Employee employee = new Employee();   // id, bossId, name, position

                if (isNumber(personDataList.get(0))) {  // should be number
                    employee.setId(Long.parseLong(personDataList.get(0)));
                } else {
                    throw new IOException("CSV-file error: wrong format of data (\"id\")!");
                }

                if (isNumber(personDataList.get(1))) {  // should be number, or null for Boss
                    employee.setBossId(Long.parseLong(personDataList.get(1)));
                } else if (personDataList.get(1).isEmpty()) {
                    result = employee;
                } else {    // if (!personDataList.get(1).isEmpty() && !isNumber(personDataList.get(1)))
                    throw new IOException("CSV-file error: wrong format of data (\"bossId\")!");
                }

                try {
                    if (personDataList.get(2).isEmpty())  // shouldn't be empty
                        throw new IOException("CSV-file error: wrong format of data (\"name\")!");
                    employee.setName(personDataList.get(2));

                    employee.setPosition(personDataList.get(3));
                } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                    throw new ArrayIndexOutOfBoundsException("CSV-file error: empty data (\"name\" or \"position\")!");
                }

                employeeMap.put(employee.getId(), employee);
            }
        }

        // Generates the structure of organization
        orgStructureGenerate(employeeMap);
        
        // Print the final employees' structure (OPTIONAL)
        orgStructurePrint(employeeMap);

        return result;
    }
*/

    private boolean isNumber(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException npe) { }
        return false;
    }

    private void orgStructureGenerate(Map<Long, Employee> employeeMap) {
        for (Employee empl : employeeMap.values()) {
            empl.setBoss(employeeMap.get(empl.getBossId()));
            for (Employee e : employeeMap.values()) {
                if (e.getBossId() == empl.getId())
                    empl.getSubordinate().add(e);
            }
        }
    }

    private void orgStructurePrint(Map<Long, Employee> employeeMap) {
        System.out.println("Full structure of organization:");
        for (Employee empl : employeeMap.values()) {
            employeePrinter(empl);
        }
    }
    
    public void employeePrinter(Employee employee) {
        System.out.println(
                "id=" + employee.getId()
                + " (" + employee.toString().substring(15) + ")"
                + ", name=" + employee.getName()
                + ", position=" + employee.getPosition()
                + ", \n  bossId=" + employee.getBossId()
                + ", boss=" + employee.getBoss()
                + ", subordinate=" + employee.getSubordinate().toString()
        );
    }
}
