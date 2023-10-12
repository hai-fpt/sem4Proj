package com.lms.helper;

import com.lms.dto.RankEnum;
import com.lms.dto.projection.UserProjection;
import com.lms.models.*;
import com.lms.repository.RoleRepository;
import com.lms.repository.TeamRepository;
import com.lms.models.User;
import com.lms.models.UserRole;
import com.lms.models.UserTeam;

import com.lms.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ExcelHelper {

    private static final int emailColumn = 1;

    private static final int rankColumn = 6;

    private static final int statusColumn = 7;

    private static final String regaxEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final RoleRepository roleRepository;

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;


    public ExcelHelper(RoleRepository roleRepository, TeamRepository teamRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"FullName", "Email", "Phone", "Skill", "Roles", "Teams", "experience", "Status", "University", "Level", "Joined date", "Department", "Graduated date", "Working time", "Create date", "Resigned date", "update date", "update by"};
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

            if (list != null) {
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
                    Row.MissingCellPolicy policy = Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;
                    currentCell = currentRow.getCell(cellIdx, policy);
                    switch (cellIdx) {
                        case 0:
                            user.setName(getCellValid(currentCell, cellIdx, rowNumber, String.class));
                            break;
                        case 1:
                            user.setEmail(getCellValid(currentCell, cellIdx, rowNumber, String.class));
                            break;
                        case 2:
                            user.setPhone(getCellValid(currentCell, cellIdx, rowNumber, String.class));
                            break;
                        case 3:
                            user.setSkills(getCellValid(currentCell, cellIdx, rowNumber, String.class));
                            break;
                        case 4:
                            String teamAliases = getCellValid(currentCell,cellIdx, rowNumber, String.class);
                            List<String> teamAliasList = Arrays.asList(teamAliases.split(","));
                            List<UserTeam> listTeam = new ArrayList<>();
                            for (String item : teamAliasList) {
                                Team team = teamMap.get(item);
                                if(team == null){
                                    throw new NullPointerException(rowAndColumnNumber(rowNumber, cellIdx) + " Invalid Team value: ");
                                }
                                listTeam.add(new UserTeam(user, team));
                            }
                            user.setUserTeams(listTeam);
                            break;
                        case 5:
                            String roleAliases = getCellValid(currentCell,cellIdx, rowNumber, String.class);
                            List<String> roleAliasList = Arrays.asList(roleAliases.split(","));
                            List<UserRole> listRole = new ArrayList<>();
                            for (String item : roleAliasList) {
                                Role role = roleMap.get(item);
                                if(role == null){
                                    throw new NullPointerException(rowAndColumnNumber(rowNumber, cellIdx) + " Invalid Role value: ");
                                }
                                listRole.add(new UserRole(user, role));
                            }
                            user.setUserRoles(listRole);
                            break;
                        case 6:
                            user.setRank(RankEnum.valueOf(getCellValid(currentCell, cellIdx, rowNumber, String.class)));
                            break;
                        case 7:
                            user.setStatus(getCellValid(currentCell, cellIdx, rowNumber, Boolean.class));
                            break;
                        case 8:
                            user.setUniversity(getCellValid(currentCell, cellIdx, rowNumber, String.class));
                            break;
                        case 9:
                            user.setJoinedDate(getCellValid(currentCell, cellIdx, rowNumber, LocalDateTime.class));
                            break;
                        case 10:
                            user.setDepartment(getCellValid(currentCell, cellIdx, rowNumber, String.class));
                            break;
                        case 11:
                            user.setUniversityGraduateDate(getCellValid(currentCell, cellIdx, rowNumber,LocalDateTime.class));
                            break;
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

    private <T> T getCellValid(Cell cell, int columnNumber, int rowNumber, Class<T> valueType) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            //check null
            if(columnNumber == emailColumn){
                throw new NullPointerException(rowAndColumnNumber(rowNumber, columnNumber) + " Email cannot be null");
            } else if (columnNumber == rankColumn){
                throw new NullPointerException(rowAndColumnNumber(rowNumber, columnNumber) + " Rank cannot be  null");
            } else if (columnNumber == statusColumn){
                throw new NullPointerException(rowAndColumnNumber(rowNumber, columnNumber) + " Status cannot be null");
            }
            return null;
        } else if (valueType.equals(String.class) && (cell.getCellType() == CellType.STRING || cell.getCellType() == CellType.NUMERIC)) {
            //check value
            if(columnNumber == emailColumn){
                if(validateRequestedUser((String) valueType.cast(cell.getStringCellValue()))){
                    throw new NullPointerException(rowAndColumnNumber(rowNumber, columnNumber) + " Email " + cell.getStringCellValue() + " does not exists");
                }
                if (!checkEmail(cell.getStringCellValue())) {
                    throw new IllegalArgumentException(rowAndColumnNumber(rowNumber, columnNumber) + " Invalid email format: " + cell.getStringCellValue());
                }
            }
            if(columnNumber == rankColumn){
                String rankCellValue = cell.getStringCellValue();
                try {
                    RankEnum.valueOf(rankCellValue);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(rowAndColumnNumber(rowNumber, columnNumber) + " Invalid rank value");
                }
            }
            return valueType.cast(cell.getStringCellValue());
        } else if (valueType.equals(Boolean.class) && cell.getCellType() == CellType.BOOLEAN) {
            return valueType.cast(cell.getBooleanCellValue());
        }  else if (valueType.equals(LocalDateTime.class) && cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return valueType.cast(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        else {
            throw new IllegalArgumentException( rowAndColumnNumber(rowNumber, columnNumber) + " has invalid cell format." );
        }
    }

    private Boolean validateRequestedUser(String email) {
        Optional<UserProjection> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    private String rowAndColumnNumber(int rowNumber, int columnNUmber){
        return "Row "+ (rowNumber + 1) +" Column " + (columnNUmber + 1);
    }

    private Boolean checkEmail(String email){
        String emailRegex = regaxEmail;
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
