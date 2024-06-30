package com.pwr.project.dto;

import com.pwr.project.entities.datatypes.File;
import com.pwr.project.entities.datatypes.NoticeStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class NoticeDTO {
    private long id;
    private String title;
    private String description;
    private Set<File> files;
    private Set<String> tags;
    private NoticeStatus noticeStatus;
    private String sellerNumber;
    private String createdBy;
}
