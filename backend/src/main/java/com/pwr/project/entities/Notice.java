package com.pwr.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pwr.project.entities.datatypes.File;
import com.pwr.project.entities.datatypes.NoticeStatus;
import jakarta.persistence.*;


import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Column(nullable = true)
    private Set<File> files = new HashSet<>();

    @ElementCollection
    @Column(nullable = false)
    private Set<String> tags;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NoticeStatus noticeStatus;

    @Column(nullable = false)
    private String sellerNumber;

    @Column(nullable = false)
    private String createdBy;

    public void addFile(File file){
        files.add(file);
        file.setNotice(this);
    }

    public void removeFile(File file){
        files.remove(file);
        file.setNotice(null);
    }
}
