package com.pwr.project.repositories;

import com.pwr.project.entities.Notice;

import com.pwr.project.entities.datatypes.NoticeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface NoticeRepository extends JpaRepository<Notice, Long>, JpaSpecificationExecutor<Notice> {
    List<Notice> findAllByCreatedByAndNoticeStatusIsNot(String createdBy, NoticeStatus noticeStatus);
    List<Notice> findAllByNoticeStatusEquals(NoticeStatus noticeStatus);

    @Query(value = "SELECT public.notices_tags.tags FROM public.notices_tags GROUP BY public.notices_tags.tags ORDER BY COUNT(*) DESC", nativeQuery = true)
    List<String> populateTags();

}
