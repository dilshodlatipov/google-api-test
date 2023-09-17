package uz.pdp.googleapitest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.pdp.googleapitest.payload.SpreadsheetDTO;
import uz.pdp.googleapitest.payload.StudentDTO;
import uz.pdp.googleapitest.service.SheetService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sheets")
public class SheetController {
    private final SheetService sheetService;

    @GetMapping("/")
    public List<StudentDTO> list() throws IOException {
        return sheetService.list();
    }

    @PostMapping("/student")
    public StudentDTO createStudent(@RequestBody StudentDTO studentDTO) throws IOException {
        return sheetService.createStudent(studentDTO);
    }

    @PostMapping("/spreadsheet")
    public String createSpreadsheet(@RequestBody SpreadsheetDTO spreadsheetDTO) throws IOException {
        return sheetService.createSpreadsheet(spreadsheetDTO);
    }
}
