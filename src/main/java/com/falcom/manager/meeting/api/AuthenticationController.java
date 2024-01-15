package com.falcom.manager.meeting.api;

import com.falcom.manager.meeting.api.dto.AuthenticationRequest;
import com.falcom.manager.meeting.api.dto.AuthenticationResponse;
import com.falcom.manager.meeting.api.dto.RegisterRequest;
import com.falcom.manager.meeting.persistence.user.Role;
import com.falcom.manager.meeting.persistence.user.User;
import com.falcom.manager.meeting.service.AuthenticationService;
import com.falcom.manager.meeting.untils.ConstantMessages;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="Authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    @Operation(
            description = "authentication ",
            summary = "This is api authentication ",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
            }

    )
//    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        String email = request.getEmail();
        String password = request.getPassword();
        String fullName = request.getFullName();
//        String birthDay = request.getBirthDay();
        String role = request.getRole();
        if(email== null|| StringUtils.isBlank(email)) {
            errors.put(ConstantMessages.EMAIL, ConstantMessages.ERROR_USER_EMAIL_MUST_NOT_NULL);
        }
        else if(service.checkEmailExist(email)) {
            errors.put(ConstantMessages.EMAIL, ConstantMessages.ERROR_USER_EMAIL_EXISTED);
        } else {
            email = email.trim();
        }
        if (password == null || StringUtils.isBlank(password)) {
            errors.put(ConstantMessages.PASSWORD, ConstantMessages.ERROR_USER_PASSWORD_MUST_NOT_NULL);
        } else {
            password = password.trim();
        }
        if (fullName == null || StringUtils.isBlank(fullName)) {
            errors.put(ConstantMessages.NAME, ConstantMessages.ERROR_USER_FULLNAME_MUST_NOT_NULL);
        } else {
            fullName = fullName.trim();
        }
        if (role == null || StringUtils.isBlank(role)) {
            role = "USER";
        }  else  {
            role = role.trim();
            if (!role.equals("USER") && !role.equals("ADMIN")) {
                errors.put(ConstantMessages.ROLE, ConstantMessages.ERROR_USER_ROLE_NOT_EXIST);
            }
        }
//        if(birthDay == null || StringUtils.isBlank(birthDay)) {
//            birthDay = birthDay.trim();
//        } else {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
//            sdf.setLenient(false); // Tắt tính linh hoạt của định dạng
//            try {
//                // Parse chuỗi thành đối tượng Date
//                Date date = sdf.parse(birthDay);
//
//                // Nếu không có lỗi, chuỗi đúng định dạng
//                System.out.println("Chuỗi đúng định dạng.");
//            } catch (ParseException e) {
//                // Nếu có lỗi, chuỗi không đúng định dạng
//                System.out.println("Chuỗi không đúng định dạng.");
//            }
//        }
        if (!errors.isEmpty()) {
            response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_INVALID_INPUT);
            response.put(ConstantMessages.ERRORS, errors);
            return new ResponseEntity<>(new JSONObject(response), HttpStatus.BAD_REQUEST);
        }
        User user;
        try {
//            Role roleNameConvertRole = Role.fromName(role);
//            RegisterRequest newRegRquest = new RegisterRequest(fullName, email, password, birthDay, role);
            RegisterRequest newRegRquest = new RegisterRequest(fullName, email, password,  role);
            user = service.register(newRegRquest);
        } catch (Exception ex) {
            response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if( user== null) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_USER_REGISTER_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        // invalid input: email or password null/empty
        String email = request.getEmail();
        if(email== null|| StringUtils.isBlank(email)) {
            errors.put(ConstantMessages.EMAIL, ConstantMessages.ERROR_USER_EMAIL_MUST_NOT_NULL);
        } else {
            email = email.trim();
        }
        String password = request.getPassword();
        if (password == null || StringUtils.isBlank(password)) {
            errors.put(ConstantMessages.PASSWORD, ConstantMessages.ERROR_USER_PASSWORD_MUST_NOT_NULL);
        } else {
            password = password.trim();
        }
        if (!errors.isEmpty()) {
            response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_INVALID_INPUT);
            response.put(ConstantMessages.ERRORS, errors);
            return new ResponseEntity<>(new JSONObject(response), HttpStatus.BAD_REQUEST);
        }
        User user;
        try {
           user = service.authenticate(email, password);
        } catch (Exception ex) {
            response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if( user== null) {
            return this.handleLoginFailed(email, response);
        }
        return this.handleLoginSuccess(user, response);
    }

    @PreAuthorize("hasAuthority('admin:create')")
    @Operation(summary = "Register User Excell")
    @PostMapping(path="/register/excel", consumes = "multipart/form-data",
            produces = "application/json")
    public ResponseEntity<Object> registerExcel(@RequestPart("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
//        List<Map<String, Object>> listError = new ArrayList<>();
        List<String> excellHeader = new ArrayList<>();
        List<RegisterRequest> excellRow = new ArrayList<>();
            if (!file.isEmpty()) {
                try (InputStream inputStream = file.getInputStream()) {
                    // Tạo một Workbook từ tệp Excel đầu vào
                    Workbook workbook = new XSSFWorkbook(inputStream);

                    // Lấy sheet cần đọc (ở đây mình lấy sheet đầu tiên)
                    Sheet sheet = workbook.getSheetAt(0);
                    Row headerRow = sheet.getRow(0);
                    for (Cell cell : headerRow) {
                        excellHeader.add(cell.getStringCellValue());
                    }

                    // Lặp qua từng dòng trong sheet
                    for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                        Row row = sheet.getRow(rowIndex);
                        RegisterRequest rowData = new RegisterRequest();
                        for (int columnIndex = 0; columnIndex < excellHeader.size(); columnIndex++) {
                            Cell cell = row.getCell(columnIndex);
                            String columnHeader = excellHeader.get(columnIndex);
//                          columnHeader = columnHeader.trim();

                            switch (columnHeader) {
                                case "fullName":
                                    String fullName = getCellValueAsString(cell);
                                    if (fullName == null || StringUtils.isBlank(fullName)) {
                                        errors.put(ConstantMessages.NAME, ConstantMessages.ERROR_USER_FULLNAME_MUST_NOT_NULL);
                                    } else {
                                        fullName = fullName.trim();
                                        rowData.setFullName(fullName);
                                    }
                                    break;
                                case "email":
                                    String email = getCellValueAsString(cell);
                                    if(email== null|| StringUtils.isBlank(email)) {
                                        errors.put(ConstantMessages.EMAIL, ConstantMessages.ERROR_USER_EMAIL_MUST_NOT_NULL);
                                    }
                                    else if(service.checkEmailExist(email)) {
                                        errors.put(ConstantMessages.EMAIL, ConstantMessages.ERROR_USER_EMAIL_EXISTED);
                                    } else {
                                        email = email.trim();
                                        rowData.setEmail(email);
                                    }
                                    break;
                                case "password":
                                    String password = getCellValueAsString(cell);
                                    if (password == null || StringUtils.isBlank(password)) {
                                        errors.put(ConstantMessages.PASSWORD, ConstantMessages.ERROR_USER_PASSWORD_MUST_NOT_NULL);
                                    } else {
                                        password = password.trim();
                                        rowData.setPassword(password);
                                    }
                                    break;
//                                case "birthDay":
//                                    rowData.setBirthDay(getCellValueAsString(cell));
//                                    break;
                                case "role":
                                    String role = getCellValueAsString(cell);
                                    if (role == null || StringUtils.isBlank(role)) {
                                        role = "USER";
                                    }  else  {
                                        role = role.trim();
                                        if (!role.equals("USER") && !role.equals("ADMIN")) {
                                            errors.put(ConstantMessages.ROLE, ConstantMessages.ERROR_USER_ROLE_NOT_EXIST);
                                        } else {
                                            rowData.setRole(role);
                                        }
                                    }
                                    break;
                                default:
                                    response.put(ConstantMessages.EXCELL_TITLE, ConstantMessages.MESSAGE_EXCELL_TITLE_INVALID);
                                    return new ResponseEntity<>(new JSONObject(response), HttpStatus.BAD_REQUEST);
                            }
                        }
                        if (!errors.isEmpty()) {
                            response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_INVALID_INPUT);
                            response.put(ConstantMessages.ERRORS, errors);
                            return new ResponseEntity<>(new JSONObject(response), HttpStatus.BAD_REQUEST);
                        }
                        excellRow.add(rowData);
                    }
                    response.put("data", excellRow);
                    response.put("title", excellHeader);
                    // Đóng workbook sau khi hoàn thành
                    workbook.close();
                } catch (IOException e) {
                    // Xử lý lỗi nếu có
                    e.printStackTrace();
                }
            }
            System.out.println(response.get("data"));
        service.insertExcellUser(excellRow);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
    private Map<String, String> buildReturnedUser(User user) {
        Map<String, String> returnedUser = new HashMap<>();
        returnedUser.put("id", Integer.toString(user.getId()));
        returnedUser.put("email", user.getEmail());
        returnedUser.put("role", user.getRole().getName());
        return returnedUser;
    }

    private ResponseEntity<Object> handleLoginFailed(String email, Map<String, Object> response) {

//        if (authenticationService.handleLoginFailed(email)) {
//            response.put(ConstantMessages.MESSAGE, ConstantMessages.ERROR_ACCOUNT_LOCKED + this.userLockedTimeMinute + "Minutes");
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }

        response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_INVALID_CREDENTIALS);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> handleLoginSuccess(User user, Map<String, Object> response) {
        String jwtToken;
        try {
            jwtToken = service.handleLoginSuccess(user);
        } catch (Exception ex) {
            response.put(ConstantMessages.MESSAGE, ConstantMessages.MESSAGE_SOMETHING_WENT_WRONG);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("accessToken", jwtToken);
        response.put("user", this.buildReturnedUser(user));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
//    private String getCellValueAsString(Cell cell) {
//        // Xử lý và trả về giá trị của ô cell dưới dạng String
//        String cellValue = "";
//        if (cell != null) {
//            switch (cell.getCellType()) {
//                case STRING:
//                    cellValue = cell.getStringCellValue();
//                    break;
//                case NUMERIC:
//                    cellValue = String.valueOf(cell.getNumericCellValue());
//                    break;
//                // Xử lý các kiểu dữ liệu khác nếu cần
//            }
//        }
//        return cellValue;
//    }
private String getCellValueAsString(Cell cell) {
    // Tạo một đối tượng DataFormatter để định dạng lại giá trị từ ô Excel
    DataFormatter dataFormatter = new DataFormatter();

    // Xử lý và trả về giá trị của ô cell dưới dạng String
    String cellValue = dataFormatter.formatCellValue(cell);

    return cellValue;
}


}