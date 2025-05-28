import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class Employee {
    String firstName;
    String lastName;
    String department;
    String gender;
    int empId;
    double salary;
    String email;
    Address address;

    Employee(String f, String l, String dept, String g, int id, double s, String e, Address a) {
        firstName = f; lastName = l; department = dept;
        gender = g; empId = id; salary = s; email = e; address = a;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String toString() {
        return getFullName() + " - " + department;
    }
}

class Address {
    String city;
    String country;

    Address(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public String toString() {
        return city + ", " + country;
    }
}

class StreamAssignment {
    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("John", "Doe", "IT", "MALE", 101, 60000, "john@xyz.com", new Address("New York", "USA")),
                new Employee("Jane", "Smith", "HR", "FEMALE", 102, 55000, null, null),
                new Employee("Alice", "Brown", "Finance", "FEMALE", 103, 70000, "alice@xyz.com", new Address("London", "UK")),
                new Employee("Bob", "Taylor", "IT", "MALE", 104, 75000, "bob@xyz.com", new Address("Toronto", "Canada"))
        );

        // 1. Full name of the first employee
        System.out.println(employees.stream().findFirst().map(Employee::getFullName).orElse("No employee"));

        // 2. Map<Department, Count>
        Map<String, Long> deptCount = employees.stream().collect(Collectors.groupingBy(e -> e.department, Collectors.counting()));
        System.out.println(deptCount);

        // 3. Filter employees by name containing substring (case-insensitive)
        Map<String, List<Employee>> deptEmpMap = Map.of("IT", employees);
        String search = "Jo";
        List<Employee> result = deptEmpMap.values().stream()
                .flatMap(List::stream)
                .filter(e -> e.firstName.toLowerCase().contains(search.toLowerCase()) ||
                        e.lastName.toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println(result);

        // 4. Pad storeId
        String storeId = "23";
        //System.out.println(String.format("%04d", Integer.parseInt(storeId)));
        System.out.printf("%04d%n", Integer.parseInt(storeId));

        // 5. Employees not in IT
        List<Employee> notInIT = employees.stream().filter(e -> !e.department.equals("IT")).collect(Collectors.toList());
        System.out.println(notInIT);

        // 6. Sort by first name
        employees.stream().sorted(Comparator.comparing(e -> e.firstName)).forEach(System.out::println);

        // 7. Employee with highest empId
        employees.stream().max(Comparator.comparingInt(e -> e.empId)).ifPresent(System.out::println);

        // 8. Full names with pipe
        String allNames = employees.stream().map(Employee::getFullName).collect(Collectors.joining("|"));
        System.out.println(allNames);

        // 9. Get 8th employee from list of 10 (simulate here)
        List<Employee> tenEmployees = new ArrayList<>(employees);
        while (tenEmployees.size() < 10) tenEmployees.addAll(employees);
        Employee e8 = tenEmployees.get(7);
        System.out.println(e8.getFullName() + " - " + e8.department);

        // 10. Matching employees by ID
        List<Integer> ids = List.of(101, 104);
        List<Employee> matched = employees.stream().filter(e -> ids.contains(e.empId)).collect(Collectors.toList());
        System.out.println(matched);

        // 11. Group by gender
        Map<String, Long> genderCount = employees.stream().collect(Collectors.groupingBy(e -> e.gender, Collectors.counting()));
        System.out.println(genderCount);

        // 12. Group to string by gender
        Map<String, List<String>> genderNames = employees.stream().collect(Collectors.groupingBy(e -> e.gender, Collectors.mapping(Employee::getFullName, Collectors.toList())));
        genderNames.forEach((g, names) -> System.out.println(g + ": " + names));

        // 13. Sort by salary
        employees.stream().sorted(Comparator.comparingDouble(e -> e.salary)).forEach(System.out::println);

        // 14. Optional fields
        employees.stream().map(e -> Optional.ofNullable(e.email).orElse("no-email@domain.com")).forEach(System.out::println);

        // 15. Default address
        employees.stream().map(e -> Optional.ofNullable(e.address).orElse(new Address("Unknown", "Unknown"))).forEach(System.out::println);

        // 16. Raise salary if IT
        employees.stream().filter(e -> Optional.ofNullable(e.department).orElse("").equals("IT")).forEach(e -> e.salary += 5000);

        // 17. Sort by city then country
        List<Address> addresses = employees.stream().map(e -> Optional.ofNullable(e.address).orElse(new Address("", ""))).collect(Collectors.toList());
        addresses.stream().sorted(Comparator.comparing((Address a) -> a.city).thenComparing(a -> a.country)).forEach(System.out::println);

        // 18. Map<FullName, Address>
        Map<String, Address> nameAddressMap = employees.stream()
                .filter(e -> e.address != null)
                .collect(Collectors.toMap(Employee::getFullName, e -> e.address));
        System.out.println(nameAddressMap);

        // 19. findAny() and findFirst()
        employees.stream().findAny().ifPresent(System.out::println);
        employees.stream().findFirst().ifPresent(System.out::println);

        // 20. anyMatch, allMatch, noneMatch
        System.out.println("Any HR? " + employees.stream().anyMatch(e -> e.department.equals("HR")));
        System.out.println("All have email? " + employees.stream().allMatch(e -> e.email != null));
        System.out.println("None has null name? " + employees.stream().noneMatch(e -> e.firstName == null));
    }
}
