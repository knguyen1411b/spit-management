package com.spring.backend.configs;

import com.spring.backend.common.constants.SuperUserProperties;
import com.spring.backend.modules.member.Member;
import com.spring.backend.modules.member.MemberRepository;
import com.spring.backend.modules.semester.Semester;
import com.spring.backend.modules.semester.SemesterRepository;
import com.spring.backend.modules.user.User;
import com.spring.backend.modules.user.UserRepository;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitializer {

  PasswordEncoder passwordEncoder;
  SuperUserProperties superuser;

  /**
   * Initializes the application with a default admin user if it does not exist. This method runs at
   * application startup and checks if an admin user is present. If not, it creates one with a
   * default username and password.
   */
  @Bean
  CommandLineRunner initData(
      UserRepository userRepository,
      SemesterRepository semesterRepository,
      MemberRepository memberRepository) {

    return args -> {
      Semester currentSemester = semesterRepository.findByCurrentTrue();

      if (currentSemester == null) {
        currentSemester =
            Semester.builder()
                .code("HK1-2025-2026")
                .name("Học kỳ 1 năm học 2025-2026")
                .description("Học kỳ 1 của CLB năm học 2025–2026")
                .startDate(Instant.parse("2025-09-01T00:00:00Z"))
                .endDate(Instant.parse("2026-01-15T23:59:59Z"))
                .semesterOrder(1)
                .current(true)
                .build();

        semesterRepository.save(currentSemester);
        log.info("Created default semester");
      }

      Semester finalCurrentSemester = currentSemester;
      User admin =
          userRepository
              .findByUsername(superuser.getUsername())
              .orElseGet(
                  () -> {
                    User u =
                        User.builder()
                            .username(superuser.getUsername())
                            .password(passwordEncoder.encode(superuser.getPassword()))
                            .superuser(true)
                            .semesterId(finalCurrentSemester.getId())
                            .build();

                    userRepository.save(u);
                    log.warn("Admin user created with default password, please change it.");
                    return u;
                  });

      if (memberRepository.count() == 0) {
        Member member =
            Member.builder()
                .username(admin.getUsername())
                .firstName("Quản trị")
                .lastName("Hệ thống")
                .description("Quản trị viên CLB")
                .user(admin)
                .gender(true)
                .birthday(Instant.parse("2000-01-01T00:00:00Z"))
                .className("CNTT")
                .generation("01")
                .email("clbhtlt@husc.edu.vn")
                .phone("0702291317")
                .build();

        memberRepository.save(member);
        log.info("Default admin member created");
      }

      log.info("Application initialization completed...");
    };
  }
}
