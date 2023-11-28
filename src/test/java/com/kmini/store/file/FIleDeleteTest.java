package com.kmini.store.file;

import com.kmini.store.dto.request.ItemBoardDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class FIleDeleteTest {

//    @Test
    void delete() throws Exception {
        String fileName = "testImage2";
        String contentType = "png";
        String absolutePath = "C:\\Users\\kmin\\images\\test\\" + fileName + "." + contentType;
        System.out.println("absolutePath = " + absolutePath);

        File file = new File(absolutePath);
        boolean deleted = file.delete();
        System.out.println("deleted = " + deleted);
//        assertThat(deleted).isTrue();
    }

//    @Test
    void write() throws IOException {

        String path = "C:\\Users\\kmin\\images\\test\\Hi.txt";
        Files.write(Paths.get(path),"테스트 파일입니다.".getBytes());
    }

}
