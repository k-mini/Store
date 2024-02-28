package com.kmini.store.controller.api;

import com.kmini.store.service.BoardService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dataTables")
@RequiredArgsConstructor
@Slf4j
public class DataTablesController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> getData(
            // form Data Map으로 받는 방법
            @RequestBody MultiValueMap<String, String> formData
            // json형태 Map으로 받기
//            @RequestBody LinkedHashMap json
    ) {

        System.out.println("formData = " + formData);
        System.out.println("olumns[0][data] = " + formData.get("columns[0][data]"));
//        System.out.println("json = " + json);
//        System.out.println("json type = " + json.getClass());
//        System.out.println("columns = " + json.get("columns"));
//        System.out.println("columns = " + json.get("columns").getClass());
//        System.out.println("columns[0] = " + ((ArrayList) json.get("columns")).get(0).getClass());
//        System.out.println("search = " + json.get("search"));
//        System.out.println("search = " + json.get("search").getClass());


        MultiValueMap<String, SampleDataDto> body = new LinkedMultiValueMap<>();
        ResultDto resultDto = new ResultDto();
        int start = Integer.parseInt(formData.get("start").get(0));
        int length = Integer.parseInt(formData.get("length").get(0));

        for (int i = start; i < start + length ; i++) {
            SampleDataDto dto = new SampleDataDto("Tiger Nixon" + i, "System Architecture" + i,
                    "Edinburgh" + i,  ""+i,
                    "2011/04/2" + i, "$320,800" + i);
            body.add("data", dto);
            resultDto.getData().add(dto);
        }
        resultDto.setDraw(Integer.parseInt(formData.get("draw").get(0)));
        resultDto.setRecordsTotal(200);
        resultDto.setRecordsFiltered(73);

        return ResponseEntity.ok(resultDto);
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class SampleDataDto {
        private String name;
        private String position;
        private String office;
        private String age;
        private String startDate;
        private String salary;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ResultDto {

        private int draw;
        private int recordsTotal;
        private int recordsFiltered;
        private List<SampleDataDto> data = new ArrayList<>();
    }

}
