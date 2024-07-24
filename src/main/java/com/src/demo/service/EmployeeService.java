package com.src.test.service;

import com.src.test.Entity.Employee;
import com.src.test.Entity.TaxDetails;
import com.src.test.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public TaxDetails calculateTax(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        double yearlySalary = calculateYearlySalary(employee);
        double taxAmount = calculateTaxAmount(yearlySalary);
        double cessAmount = calculateCessAmount(yearlySalary);

        return new TaxDetails(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                yearlySalary,
                taxAmount,
                cessAmount
        );
    }

    private double calculateYearlySalary(Employee employee) {
        LocalDate currentDate = LocalDate.now();
        LocalDate financialYearStart = LocalDate.of(currentDate.getYear(), Month.APRIL, 1);
        LocalDate financialYearEnd = LocalDate.of(currentDate.getYear() + 1, Month.MARCH, 31);

        if (employee.getDoj().isAfter(financialYearEnd) || employee.getDoj().isBefore(financialYearStart)) {
            financialYearStart = LocalDate.of(employee.getDoj().getYear(), Month.APRIL, 1);
            financialYearEnd = LocalDate.of(employee.getDoj().getYear() + 1, Month.MARCH, 31);
        }

        LocalDate startDate = employee.getDoj().isAfter(financialYearStart) ? employee.getDoj() : financialYearStart;
        long daysWorked = ChronoUnit.DAYS.between(startDate, financialYearEnd) + 1;
        double dailySalary = employee.getSalary() / 30.0;

        return daysWorked * dailySalary;
    }

    private double calculateTaxAmount(double yearlySalary) {
        double tax = 0;
        if (yearlySalary > 1000000) {
            tax += (yearlySalary - 1000000) * 0.2;
            tax += 500000 * 0.1;
            tax += 250000 * 0.05;
        } else if (yearlySalary > 500000) {
            tax += (yearlySalary - 500000) * 0.1;
            tax += 250000 * 0.05;
        } else if (yearlySalary > 250000) {
            tax += (yearlySalary - 250000) * 0.05;
        }
        return tax;
    }

    private double calculateCessAmount(double yearlySalary) {
        if (yearlySalary > 2500000) {
            return (yearlySalary - 2500000) * 0.02;
        }
        return 0;
    }

}
