package uz.pdp.googleapitest.service;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import uz.pdp.googleapitest.entity.Student;
import uz.pdp.googleapitest.enums.ClassLevel;
import uz.pdp.googleapitest.enums.Gender;
import uz.pdp.googleapitest.enums.Major;
import uz.pdp.googleapitest.enums.State;
import uz.pdp.googleapitest.exceptions.RestException;
import uz.pdp.googleapitest.mapper.StudentMapper;
import uz.pdp.googleapitest.payload.StudentDTO;
import uz.pdp.googleapitest.utils.AppConstants;
import uz.pdp.googleapitest.utils.MessageConstants;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentSheetServiceImpl implements StudentSheetService {
    @Value("${spreadsheets.spreadsheetId}")
    private String spreadsheetId;
    @Value("${spreadsheets.sheetName}")
    private String sheetName;
    @Value("${spreadsheets.sheetId}")
    private Integer sheetId;
    @Value("${spreadsheets.owner}")
    private String owner;
    @Value("${spreadsheets.range.begin.col}")
    private String beginCol;
    @Value("${spreadsheets.range.begin.row}")
    private String beginRow;
    @Value("${spreadsheets.range.end.col}")
    private String endCol;
    private final String ROWS = "ROWS";

    private final Sheets sheets;
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentSheetServiceImpl(Sheets sheets,
                                   @Lazy StudentService studentService,
                                   StudentMapper studentMapper) {
        this.sheets = sheets;
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @Override
    public void saveAll() {
        try {
            final List<List<Object>> values = getValues(sheetName + "!" + beginCol + beginRow + ':' + endCol).getValues();
            Student student;
            for (int i = 0; i < values.size(); i++) {
                student = buildStudent(values.get(i));
                if (student != null) {
                    boolean colUpdatable = student.getId() == null;
                    if (colUpdatable) {
                        Student savedStudent = studentService.create(studentMapper.toStudentDTO(student));
                        update(sheetName + '!' + (endCol + (i + AppConstants.additionRows)), savedStudent.getId().toString());
                    } else
                        studentService.update(student.getId(), studentMapper.toStudentDTO(student));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void setValidationRules() {
        GridRange statesRange = createGridRangeForOneColumn(AppConstants.startRowIndex, AppConstants.stateCol);
        GridRange genderRange = createGridRangeForOneColumn(AppConstants.startRowIndex, AppConstants.genderCol);
        GridRange classLevelRange = createGridRangeForOneColumn(AppConstants.startRowIndex, AppConstants.classLevelCol);
        GridRange majorRange = createGridRangeForOneColumn(AppConstants.startRowIndex, AppConstants.majorCol);
        GridRange pKeyRange = createGridRangeForOneColumn(AppConstants.startRowIndex, AppConstants.idCol);
        List<Request> requests = Arrays.asList(
                createDataValidationRequestForList(statesRange, Arrays.stream(State.values()).map(Enum::name).toList(), true),
                createDataValidationRequestForList(genderRange, Arrays.stream(Gender.values()).map(Enum::name).toList(), true),
                createDataValidationRequestForList(classLevelRange, Arrays.stream(ClassLevel.values()).map(Enum::name).toList(), true),
                createDataValidationRequestForList(majorRange, Arrays.stream(Major.values()).map(Enum::name).toList(), true),
                new Request().setAddProtectedRange(
                        new AddProtectedRangeRequest()
                                .setProtectedRange(new ProtectedRange()
                                        .setRange(pKeyRange)
                                        .setEditors(
                                                new Editors().setUsers(List.of(owner)
                                                )
                                        )
                                        .setDescription("Only owner can change this column")
                                )
                )
        );
        BatchUpdateSpreadsheetResponse response;
        try {
            BatchUpdateSpreadsheetRequest body =
                    new BatchUpdateSpreadsheetRequest().setRequests(requests);
            response = sheets.spreadsheets().batchUpdate(spreadsheetId, body).execute();
            System.out.printf("%d cells updated.", response.getReplies().size());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheetId);
                e.printStackTrace();
            }
            System.out.println(e.getMessage());
            System.out.println(e.getDetails().getErrors());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StudentDTO save(Student student) {
        try {
            ValueRange valueRange = new ValueRange();
            valueRange.setValues(List.of(
                    toListOfObjects(student)
            ));

            sheets.spreadsheets().values()
                    .append(spreadsheetId, sheetName + '!' + beginCol + beginRow, valueRange)
                    .setValueInputOption(AppConstants.USER_ENTERED).execute();
            return studentMapper.toStudentDTO(student);
        } catch (IOException e) {
            throw RestException.restThrow(e.getMessage());
        }
    }

    private GridRange createGridRangeForOneColumn(int startRowIndex, int column) {
        return new GridRange()
                .setSheetId(sheetId)
                .setStartRowIndex(startRowIndex)
                .setStartColumnIndex(column)
                .setEndColumnIndex(column + 1);
    }

    private Student buildStudent(List<Object> values) {
        int size = values.size();
        UUID id;
        Gender gender;
        ClassLevel classLevel;
        State state;
        Major major;
        id = AppConstants.idCol < size
             && values.get(AppConstants.idCol).toString().matches(AppConstants.UUID_REGEX)
                ? UUID.fromString(values.get(AppConstants.idCol).toString()) : null;
        gender = valuateEnum(AppConstants.genderCol, size, Gender.class, values);
        classLevel = valuateEnum(AppConstants.classLevelCol, size, ClassLevel.class, values);
        state = valuateEnum(AppConstants.stateCol, size, State.class, values);
        major = valuateEnum(AppConstants.majorCol, size, Major.class, values);
        String name = AppConstants.nameCol < size ? values.get(AppConstants.nameCol).toString() : null;
        String activity = AppConstants.activityCol < size ? values.get(AppConstants.activityCol).toString() : null;
        return Student.builder()
                .id(id)
                .name(name)
                .gender(gender)
                .classLevel(classLevel)
                .state(state)
                .major(major)
                .activity(activity)
                .build();
    }

    /**
     * @param range Range that needs to be updated. Example: {@code 'A2:B2'} or {@code 'sheetName!A2:B2'}
     * @return ValueRange of data that was fetched from spreadsheet
     */
    private ValueRange getValues(String range) throws IOException {
        return sheets.spreadsheets().values()
                .get(spreadsheetId, range)
                .setMajorDimension("ROWS").execute();
    }

    /**
     * @param range Range that needs to be updated. Example: {@code 'A2'} or {@code 'sheetName:A1'} <br>
     *              If range has more than two cells method will update only top-left cell.
     * @param value Value that needs to be inserted into this range
     */
    private void update(String range, String value) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        BatchUpdateValuesRequest content = new BatchUpdateValuesRequest();
        ValueRange valueRange = new ValueRange();
        valueRange.setRange(range);
        valueRange.setValues(
                List.of(
                        List.of(value)
                )
        );
        content.setData(List.of(valueRange));
        content.setValueInputOption(AppConstants.USER_ENTERED);
        try {
            sheets.spreadsheets().values().batchUpdate(spreadsheetId, content).execute();
        } catch (IOException e) {
            throw RestException.restThrow(e.getMessage());
        }
    }

    private List<Object> toListOfObjects(Student student) {
        if (Objects.isNull(student.getId()))
            throw RestException.restThrow(MessageConstants.INVALID_STUDENT);
        String id = student.getId().toString();
        String name = getNotNullData(student.getName());
        String gender = getNotNullData(student.getGender());
        String classLevel = getNotNullData(student.getClassLevel());
        String state = getNotNullData(student.getState());
        String major = getNotNullData(student.getMajor());
        String activity = getNotNullData(student.getActivity());
        return new ArrayList<>(AppConstants.companyRowSize) {{
            add(AppConstants.nameCol, name);
            add(AppConstants.genderCol, gender);
            add(AppConstants.classLevelCol, classLevel);
            add(AppConstants.stateCol, state);
            add(AppConstants.majorCol, major);
            add(AppConstants.activityCol, activity);
            add(AppConstants.idCol, id);
        }};
    }

    private <T extends CharSequence> Request createDataValidationRequestForList(GridRange range, List<T> values, boolean strict) {
        return new Request().setSetDataValidation(
                new SetDataValidationRequest()
                        .setRange(range)
                        .setRule(new DataValidationRule()
                                .setCondition(new BooleanCondition()
                                        .setType(AppConstants.ONE_OF_LIST)
                                        .setValues(
                                                values.stream()
                                                        .map(value ->
                                                                new ConditionValue()
                                                                        .setUserEnteredValue(value.toString())
                                                        ).collect(Collectors.toList())
                                        )
                                )
                                .setStrict(strict))
        );
    }

    private String getNotNullData(String text) {
        return Objects.isNull(text) ? "" : text;
    }

    private <T extends Enum<T>> String getNotNullData(T value) {
        return Objects.isNull(value) ? "" : value.name();
    }

    private <T extends Enum<T>> T valuateEnum(int colNumber, int size, Class<T> enumClass, List<Object> values) {
        try {
            return colNumber < size ? Enum.valueOf(enumClass, values.get(colNumber).toString().trim().toUpperCase()) : null;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
