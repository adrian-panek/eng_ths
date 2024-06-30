package com.pwr.project.controllers;

import com.pwr.project.dto.NoticeDTO;
import com.pwr.project.entities.datatypes.File;
import com.pwr.project.entities.search.SearchCriteriaRequest;
import com.pwr.project.entities.search.SearchResponse;
import com.pwr.project.services.NoticeService;

import com.pwr.project.services.search.AdvanceSearchService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notices")
@CrossOrigin(origins = "http://localhost:4200")
public class NoticeController {

    private NoticeService noticeService;
    private AdvanceSearchService advanceSearchService;

    @PostMapping
    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO){
        NoticeDTO savedNotice = noticeService.createNotice(noticeDTO);
        return new ResponseEntity<>(savedNotice, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable("id") Long noticeId){
        NoticeDTO notice = noticeService.getNoticeById(noticeId);
        return new ResponseEntity<>(notice, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<NoticeDTO>> getAllNotices(){
        List<NoticeDTO> notices = noticeService.getAllNotices();
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<SearchResponse>> searchByCriteria(@RequestBody SearchCriteriaRequest searchCriteriaRequest){
        return ResponseEntity.ok(advanceSearchService.searchNoticeByCriteria(searchCriteriaRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateNotice(@PathVariable("id") Long noticeId, @RequestBody NoticeDTO notice){
        try {
            notice.setId(noticeId);
            NoticeDTO updatedNotice = noticeService.updateNotice(notice);
            return new ResponseEntity<>(String.format("Notice %s has been updated successfully!", noticeId), HttpStatus.OK);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(String.format("You don't have right to update notice with ID: %s", noticeId), HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(String.format("Notice with ID: %s is not present in database", noticeId), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable("id") Long noticeId){
        try {
            noticeService.deleteNotice(noticeId);
            return new ResponseEntity<>(String.format("Notice with id: %s has been deleted succesfully!", noticeId), HttpStatus.OK);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(String.format("You don't have right to delete notice with id: %s", noticeId), HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(String.format("Notice with id: %s does not exist in database", noticeId), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{noticeId}/files")
    public ResponseEntity<NoticeDTO> addFilesToNotice(@PathVariable("noticeId") Long noticeId, @RequestBody List<File> files) {
        for (File file : files) {
            noticeService.addFileToNotice(noticeId, file);
        }
        NoticeDTO updatedNotice = noticeService.getNoticeById(noticeId);
        return ResponseEntity.ok(updatedNotice);
    }

    @GetMapping("/{noticeId}/files")
    public ResponseEntity<List<File>> getAllFilesForNotice(@PathVariable("noticeId") Long noticeId){
        List<File> files = noticeService.getAllFiles(noticeId);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<String>> populateTags(){
        List <String> tags = noticeService.populateTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
