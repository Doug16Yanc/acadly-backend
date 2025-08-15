package douglas.events.application.controller;

import douglas.events.application.dto.request.AuthEmployeeDto;
import douglas.events.application.dto.request.AuthEmployeeResponseDto;
import douglas.events.application.dto.request.CreateEmployeeDto;
import douglas.events.application.dto.request.UpdateEmployeeDto;
import douglas.events.application.dto.response.ApiResponse;
import douglas.events.application.dto.response.EmployeeResponseDto;
import douglas.events.business.service.AuthService;
import douglas.events.business.service.EmployeeService;
import douglas.events.infraestructure.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static douglas.events.application.util.UtilApi.getApiResponseEntity;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody CreateEmployeeDto createDto) {
        Person newEmployee = employeeService.createEmployee(createDto);
        EmployeeResponseDto responseDto = EmployeeResponseDto.fromEntity(newEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getAllEmployees(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var employees = employeeService.getAllEmployees(page, pageSize);
        return getApiResponseEntity(employees, EmployeeResponseDto::fromEntity);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        Person employee = employeeService.getEmployeeById(id);
        EmployeeResponseDto responseDto = EmployeeResponseDto.fromEntity(employee);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeDto updateDto) {
        Person updatedEmployee = employeeService.updateEmployee(id, updateDto);
        EmployeeResponseDto responseDto = EmployeeResponseDto.fromEntity(updatedEmployee);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthEmployeeResponseDto> employeeAuth(@RequestBody AuthEmployeeDto employeeDto) {
        var employee = authService.authenticateEmployee(employeeDto.email(), employeeDto.password());
        return ResponseEntity.ok(employee);
    }
}