package com.pwr.project.dto;

public record MessageDTO(
        String message,
        String senderId,
        String recipientId,
        long timestamp,
        String senderName
) {}
