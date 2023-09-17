package uz.pdp.googleapitest.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.googleapitest.entity.Student;
import uz.pdp.googleapitest.enums.ClassLevel;
import uz.pdp.googleapitest.enums.Sex;
import uz.pdp.googleapitest.mapper.StudentMapper;
import uz.pdp.googleapitest.payload.SpreadsheetDTO;
import uz.pdp.googleapitest.payload.StudentDTO;
import uz.pdp.googleapitest.repository.StudentRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SheetService {
    private final String range = "A2:F";
    private final Sheets service;
    private final StudentRepository repository;
    private final StudentMapper mapper;
    private final String spreadsheetId = "1kSEYFghWygiDGCZeiO3AHe2x3fo-Z3tUYkFMVu64akw";


    public String createSpreadsheet(SpreadsheetDTO spreadsheetDTO) throws IOException {
        List<Sheet> sheets = spreadsheetDTO.getSheets().stream().map(
                string -> new Sheet().setProperties(
                        new SheetProperties().setTitle(string)
                )
        ).collect(Collectors.toList());
        SpreadsheetProperties spreadsheetProperties =
                new SpreadsheetProperties().setTitle(spreadsheetDTO.getName());
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(spreadsheetProperties)
                .setSheets(sheets);
        return service.spreadsheets().create(spreadsheet).execute().getSpreadsheetUrl();
    }

    public List<StudentDTO> list() throws IOException {
        ValueRange valueRange = getValueRange();
        List<Student> students = valueRange.getValues().stream().map(
                objects -> Student.builder().name(objects.get(0).toString())
                        .sex(Sex.valueOf(objects.get(1).toString()))
                        .classLevel(ClassLevel.valueOf(objects.get(2).toString()))
                        .homeState(objects.get(3).toString())
                        .major(objects.get(4).toString())
                        .activity(objects.get(5).toString())
                        .build()).collect(Collectors.toList());
        repository.saveAll(students);
        return mapper.toStudentDTOList(students);
    }

    private ValueRange getValueRange() throws IOException {
        ValueRange valueRange = service.spreadsheets()
                .values().get(spreadsheetId, range)
                .setMajorDimension("ROWS").execute();
        return valueRange;
    }

    public StudentDTO createStudent(StudentDTO studentDTO) throws IOException {
        int size = getValueRange().getValues().size();
        final BatchUpdateValuesRequest content = new BatchUpdateValuesRequest();
        ValueRange valueRange = new ValueRange();
        valueRange.setValues(List.of(
                List.of(
                        studentDTO.getName(), studentDTO.getSex().toString(),
                        studentDTO.getClassLevel().toString(), studentDTO.getHomeState(),
                        studentDTO.getMajor(), studentDTO.getActivity()
                )
        ));
        valueRange.setRange("A" + (size + 2) + ":F" + (size + 2));
        content.setData(List.of(
                valueRange
        ));
        content.setValueInputOption("USER_ENTERED");
        service.spreadsheets().values().batchUpdate(spreadsheetId, content).execute();
        Student student = repository.save(mapper.toStudent(studentDTO));
        return mapper.toStudentDTO(student);
    }
}
