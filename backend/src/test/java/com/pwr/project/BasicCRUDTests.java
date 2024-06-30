package com.pwr.project;

import com.pwr.project.entities.Notice;
import com.pwr.project.entities.datatypes.NoticeStatus;
import com.pwr.project.repositories.NoticeRepository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BasicCRUDTests {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void createNoticeTest(){
        Notice notice = Notice.builder()
                .id(1L)
                .title("Test title 123")
                .description("Test description 123")
                .tags(new HashSet<>(Arrays.asList("test", "automation")))
                .noticeStatus(NoticeStatus.Live)
                .sellerNumber("845793234")
                .createdBy("test@automation.com")
                .build();
        noticeRepository.save(notice);
        assertThat(noticeRepository.findById(1L).get().getTitle())
                .isEqualTo("Test title 123");
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    public void getNoticeItemTest(){
        Notice notice = noticeRepository.findById(1L).get();
        assertThat(notice).isNotNull();
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    public void getNoticesTest(){
        List<Notice> noticeList = noticeRepository.findAll();
        assertThat(noticeList.size()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    public void updateNoticeTest(){
        Notice notice = noticeRepository.findById(1L).get();
        notice.setNoticeStatus(NoticeStatus.Archived);
        Notice updatedNotice = noticeRepository.save(notice);
        assertThat(updatedNotice.getNoticeStatus()).isEqualTo(NoticeStatus.Archived);
    }
}