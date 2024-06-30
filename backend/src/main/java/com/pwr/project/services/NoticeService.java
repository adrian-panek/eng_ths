package com.pwr.project.services;

import com.pwr.project.dto.NoticeDTO;
import com.pwr.project.entities.Notice;
import com.pwr.project.entities.User;
import com.pwr.project.entities.datatypes.File;
import com.pwr.project.entities.datatypes.NoticeStatus;
import com.pwr.project.repositories.FileRepository;
import com.pwr.project.repositories.NoticeRepository;
import com.pwr.project.repositories.UserRepository;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final UserRepository userRepository;

    private final FileRepository fileRepository;

    private final ModelMapper modelMapper;

    public NoticeService(NoticeRepository noticeRepository, UserRepository userRepository, FileRepository fileRepository, ModelMapper modelMapper) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<NoticeDTO> getAllNotices(){
        List<Notice> notices = noticeRepository.findAllByNoticeStatusEquals(NoticeStatus.Live);
        String currentUser = getCurrentUsername();
        if (!currentUser.equals("anonymousUser")){
            notices.addAll(noticeRepository.findAllByCreatedByAndNoticeStatusIsNot(currentUser, NoticeStatus.Live));
        }
        return notices
                .stream()
//                .map(NoticeMapper::mapToNoticeDTO)
                .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public NoticeDTO getNoticeById(Long id){
        Optional<Notice> optionalNotice = noticeRepository.findById(id);
        Notice notice = optionalNotice.get();
        return modelMapper.map(notice, NoticeDTO.class);
    }

    @Transactional
    public NoticeDTO createNotice(NoticeDTO noticeDTO) {
        User user = userRepository.findUserByLogin(getCurrentUsername());
        if (!user.getIsSeller()) {
            throw new SecurityException("Current user is not seller!");
        } else {
            Notice notice = modelMapper.map(noticeDTO, Notice.class);
            notice.setCreatedBy(getCurrentUsername());
            Notice savedNotice = noticeRepository.save(notice);
            return modelMapper.map(savedNotice, NoticeDTO.class);
        }
    }

    public NoticeDTO updateNotice(NoticeDTO notice) throws IllegalAccessException {
        Optional<Notice> existingNoticeOptional = noticeRepository.findById(notice.getId());
        if (existingNoticeOptional.isPresent()) {
            Notice existingNotice = existingNoticeOptional.get();
            String currentUser = getCurrentUsername();
            if (existingNotice.getCreatedBy().equals(currentUser)) {
                existingNotice.setTitle(notice.getTitle());
                existingNotice.setDescription(notice.getDescription());
                existingNotice.setTags(notice.getTags());
                existingNotice.setSellerNumber(notice.getSellerNumber());
                existingNotice.setNoticeStatus(notice.getNoticeStatus());
                Notice updatedNotice = noticeRepository.save(existingNotice);
                return modelMapper.map(updatedNotice, NoticeDTO.class);
            } else {
                throw new IllegalAccessException("You dont have right to edit notice with ID: " + notice.getId());
            }
        } else {
            throw new IllegalArgumentException("Notice with given ID: " + notice.getId() + " does not exist in database!");
        }
    }

    public void deleteNotice(Long id) throws IllegalAccessException {
        Optional<Notice> existingNotice = noticeRepository.findById(id);
        String currentUser = getCurrentUsername();
        if (existingNotice.isPresent()) {
            if (existingNotice.get().getCreatedBy().equals(currentUser)) {
                noticeRepository.deleteById(id);
            } else {
                throw new IllegalAccessException("You don't have right to delete this notice!");
            }
        } else {
            throw new IllegalArgumentException("Notice with given ID: " + id + " does not exist in database!");
        }
    }

    private String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Transactional
    public void addFileToNotice(Long noticeId, File file) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));
        file.setNotice(notice);
        fileRepository.save(file);
    }

    @Transactional
    public List<File> getAllFiles(Long noticeId){
        return fileRepository.findAllByNoticeId(noticeId);
    }

    public List<String> populateTags(){
        return noticeRepository.populateTags();
    }

}
