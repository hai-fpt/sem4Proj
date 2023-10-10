package com.lms.helper;

import com.lms.dto.UserDTO;
import com.lms.models.User;
import com.lms.models.UserLeave;
import com.lms.models.UserRole;
import com.lms.models.UserTeam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper<T> {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "FullName", "Email", "Phone", "Skill" , "Roles", "Teams", "experience", "Status", "University", "Level", "Joined date", "Department", "Graduated date", "Working time", "Create date", "Resigned date", "update date", "update by" };

    static String[] HEADERsUserLeave = { "FullName", "Leave from date", "No of days", "Leave type", "Reason to leave", "Create time", "Status", "Approval1", "Approval2", "Approval3"};
    static String[] SHEET = {"Users", "UsersLeave"};

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static <T> ByteArrayInputStream exportToExcel(List<T> list) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            Sheet sheet = workbook.createSheet(SHEET[0]);

            // Header
            Row headerRow = sheet.createRow(0);
            CellStyle dateStyle = workbook.createCellStyle();  // Create a new cell style
            CreationHelper creationHelper = workbook.getCreationHelper();  // Get the creation helper
            dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh-mm-ss"));

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (T item : list) {
                if (item instanceof User) {
                    User user = (User) item;
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(user.getName());
                    row.createCell(1).setCellValue(user.getEmail());
                    row.createCell(2).setCellValue(user.getPhone());
                    row.createCell(3).setCellValue(user.getSkills());
                    List<UserRole> userRoles = user.getUserRoles();
                    StringBuilder userRolesStr = new StringBuilder();
                    for (UserRole userRole : userRoles) {
                        userRolesStr.append(userRole.getRole().getName()).append(',');
                    }
                    row.createCell(4).setCellValue(userRolesStr.toString());
                    List<com.lms.models.UserTeam> userTeams = user.getUserTeams();
                    StringBuilder userTeamStr = new StringBuilder();
                    for (UserTeam userTeam : userTeams) {
                        userTeamStr.append((userTeam.getTeam().getTeamName())).append(',');
                    }
                    row.createCell(5).setCellValue(userTeamStr.toString());
                    if (user.getUniversity_graduate_date() != null) {
                        Period experienceTimes = Period.between(new java.sql.Date(user.getUniversity_graduate_date().getTime()).toLocalDate(), LocalDate.now());
                        //TODO should return 2 fields depends (expYear, expMonth)
                        user.setExperience_date(experienceTimes.getYears() + " Years, " + experienceTimes.getMonths() + " Months");
                    }
                    row.createCell(6).setCellValue(user.getExperience_date());
                    row.createCell(7).setCellValue(user.isStatus());
                    row.createCell(8).setCellValue(user.getUniversity());
                    row.createCell(9).setCellValue(user.getRank().toString());
                    Cell cellDepartment = row.createCell(10);
                    cellDepartment.setCellValue(user.getJoined_date());
                    cellDepartment.setCellStyle(dateStyle);
                    row.createCell(11).setCellValue(user.getDepartment());
                    Cell cellUniversityGraduateDate = row.createCell(12);
                    cellUniversityGraduateDate.setCellValue(user.getUniversity_graduate_date());
                    cellUniversityGraduateDate.setCellStyle(dateStyle);
                    if (user.getJoined_date() != null) {
                        Period workingTime = Period.between(new java.sql.Date(user.getJoined_date().getTime()).toLocalDate(), LocalDate.now());
                        user.setWorking_time(workingTime.getYears() + " Years, " + workingTime.getMonths() + " Months");
                    }
                    row.createCell(13).setCellValue(user.getWorking_time());
                    Cell cellCreatedDate = row.createCell(14);
                    cellCreatedDate.setCellValue(user.getJoined_date());
                    cellCreatedDate.setCellStyle(dateStyle);
                    Cell cellResignedDate = row.createCell(15);
                    cellResignedDate.setCellValue(user.getResigned_date());
                    cellResignedDate.setCellStyle(dateStyle);
                    Cell cell = row.createCell(16);
                    cell.setCellValue(user.getCreatedDate());
                    cell.setCellStyle(dateStyle);
                    row.createCell(17).setCellValue(user.getUpdatedBy());
                }  else {
                    System.out.println("Unknown type");
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static List<User> excelToUsers(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET[0]);
            Iterator<Row> rows = sheet.iterator();

            List<User> users = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                User user = new User();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            user.setName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            user.setEmail(currentCell.getStringCellValue());
                            break;
                        case 2:
                            user.setPhone(currentCell.getStringCellValue());
                            break;
                        case 3:
                            user.setSkills(currentCell.getStringCellValue());
                            break;
                        case 4:
                            user.setTeam_alias(currentCell.getStringCellValue());
                            break;
                        case 5:
                            user.setRole_alias((currentCell.getStringCellValue()));
                            break;
                        case 6:
                            user.setStatus((currentCell.getBooleanCellValue()));
                            break;

                        default:
                            break;
                    }
                    cellIdx++;
                }
                user.setRank(User.RankEnum.EMPLOYEE);
                users.add(user);
            }

            workbook.close();

            return users;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
