package com.kmini.store.controller.api.admin;

import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.response.admin.AdminUsersResponseDto;
import com.kmini.store.dto.response.admin.AdminUsersResponseDto.AdminUserDto;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class UserAdminApiController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> selectUsers(
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
            @RequestParam(required = false) Optional<Integer> draw) {
        Page<AdminUserDto> page = userService.selectAllUsers(pageable)
                                            .map(AdminUserDto::toDto);
        int intDraw = draw.orElse(0);

        return ResponseEntity
                .ok(new CommonRespDto<>(1, "성공", new AdminUsersResponseDto<>(intDraw, page)));
    }
}
