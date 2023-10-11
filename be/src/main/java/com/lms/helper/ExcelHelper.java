package com.lms.helper;

import com.lms.models.*;
import com.lms.repository.RoleRepository;
import com.lms.repository.TeamRepository;
//import com.lms.utils.ControllerUtils;
import com.lms.models.User;
import com.lms.models.UserRole;
import com.lms.models.UserTeam;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExcelHelper {

    private final RoleRepository roleRepository;

    private final TeamRepository teamRepository;

//    private final ControllerUtils controllerUtils;

    public ExcelHelper(RoleRepository roleRepository, TeamRepository teamRepository) {
        this.roleRepository = roleRepository;
        this.teamRepository = teamRepository;
    }

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"FullName", "Email", "Phone", "Skill", "Roles", "Teams", "experience", "Status", "University", "Level", "Joined date", "Department", "Graduated date", "Working time", "Create date", "Resigned date", "update date", "update by"};

    static String[] HEADERsUserLeave = {"FullName", "Leave from date", "No of days", "Leave type", "Reason to leave", "Create time", "Status", "Approval1", "Approval2", "Approval3"};
    static String[] SHEET = {"Users", "UsersLeave"};

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public ByteArrayInputStream exportToExcel(List<User> list) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

            Sheet sheet = workbook.createSheet(SHEET[0]);

            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            //common
            CellStyle dateStyle = workbook.createCellStyle();  // Create a new cell style
            CreationHelper creationHelper = workbook.getCreationHelper();  // Get the creation helper
            dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh-mm-ss"));


            int rowIdx = 1;

            if(list != null){
                for (User user : list) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(user.getName());
                    row.createCell(1).setCellValue(user.getEmail());
                    row.createCell(2).setCellValue(user.getPhone());
                    row.createCell(3).setCellValue(user.getSkills());
                    List<UserRole> userRoles = user.getUserRoles();
                    String userRolesStr = userRoles.stream()
                            .map(userTeam -> userTeam.getRole().getName().toString())
                            .collect(Collectors.joining(","));
                    row.createCell(4).setCellValue(userRolesStr.toString());
                    List<UserTeam> userTeams = user.getUserTeams();
                    String userTeamStr = userTeams.stream()
                            .map(userTeam -> userTeam.getTeam().getTeamName())
                            .collect(Collectors.joining(","));
                    row.createCell(5).setCellValue(userTeamStr);
                    row.createCell(6).setCellValue(user.getExperienceDateAsString());
                    row.createCell(7).setCellValue(user.isStatus());
                    row.createCell(8).setCellValue(user.getUniversity());
                    row.createCell(9).setCellValue(user.getRank().toString());
                    Cell cellDepartment = row.createCell(10);
                    cellDepartment.setCellValue(user.getJoinedDate());
                    cellDepartment.setCellStyle(dateStyle);
                    row.createCell(11).setCellValue(user.getDepartment());
                    Cell cellUniversityGraduateDate = row.createCell(12);
                    cellUniversityGraduateDate.setCellValue(user.getUniversityGraduateDate());
                    cellUniversityGraduateDate.setCellStyle(dateStyle);
                    row.createCell(13).setCellValue(user.getWorkingTimeAsString());
                    Cell cellCreatedDate = row.createCell(14);
                    cellCreatedDate.setCellValue(user.getJoinedDate());
                    cellCreatedDate.setCellStyle(dateStyle);
                    Cell cellResignedDate = row.createCell(15);
                    cellResignedDate.setCellValue(user.getResignedDate());
                    cellResignedDate.setCellStyle(dateStyle);
                    Cell cell = row.createCell(16);
                    cell.setCellValue(user.getCreatedDate());
                    cell.setCellStyle(dateStyle);
                    row.createCell(17).setCellValue(user.getUpdatedBy());
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public List<User> excelToUsers(InputStream is) {  //throws FormatException
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

                List<Role> roles = roleRepository.findAll();
                Map<String, Role> roleMap = roles.stream()
                        .collect(Collectors.toMap(role -> role.getName().toString(), Function.identity()));

                List<Team> teams = teamRepository.findAll();
                Map<String, Team> teamMap = teams.stream()
                        .collect(Collectors.toMap(Team::getTeamName, Function.identity()));

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    // ko co email

                    // ko join date

                    switch (cellIdx) {
                        case 0:
                            if(!(currentCell.getStringCellValue() instanceof String)){
                                throw new IllegalArgumentException("Row" + cellIdx + "has column 1 not a string.");
                            }
                            user.setName(currentCell.getStringCellValue());
                            break;
                        case 1:
//                            if (!controllerUtils.validateRequestedUser(currentCell.getStringCellValue())) {
//                                throw new NullPointerException("Email " + currentCell.getStringCellValue() + " does not exists");
//                            }
                            user.setEmail(currentCell.getStringCellValue());
                            break;
                        case 2:
                            user.setPhone(currentCell.getStringCellValue());
                            break;
                        case 3:
                            user.setSkills(currentCell.getStringCellValue());
                            break;
                        case 4:
                            String teamAliases = currentCell.getStringCellValue();
                            // Validate
                            List<String> teamAliasList = Arrays.asList(teamAliases.split(","));
                            List<UserTeam> listTeam = new ArrayList<>();
                            for (String item : teamAliasList) {
                                Team team = teamMap.get(item);
                                listTeam.add(new UserTeam(user, team));
                            }
                            user.setUserTeams(listTeam);
                            break;
                        case 5:
                            String roleAliases = currentCell.getStringCellValue();
                            List<String> roleAliasList = Arrays.asList(roleAliases.split(","));
                            List<UserRole> listRole = new ArrayList<>();
                            for (String item : roleAliasList) {
                                Role role = roleMap.get(item);
                                listRole.add(new UserRole(user, role));
                            }
                            user.setUserRoles(listRole);
                            break;
                        case 6:
                            user.setRank(User.RankEnum.valueOf(currentCell.getStringCellValue()));
                            break;
                        case 7:
                            user.setStatus(currentCell.getBooleanCellValue());
                            break;
                        case 8:
                            user.setUniversity(currentCell.getStringCellValue());
                            break;
//                        case 9:
//                            user.setJoinedDate(currentCell.getDateCellValue());
//                            break;
                        case 10:
                            user.setDepartment(currentCell.getStringCellValue());
                            break;
//                        case 11:
//                            user.setUniversityGraduateDate(currentCell.getDateCellValue());
//                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                users.add(user);
            }

            workbook.close();

            return users;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
