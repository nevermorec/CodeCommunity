package com.community.community.service;

import com.community.community.dto.NotificationDTO;
import com.community.community.dto.PaginationDTO;
import com.community.community.model.Notification;
import com.community.community.model.User;

public interface NotificationService {
    PaginationDTO<NotificationDTO> listByUserId(Integer userId, Integer page, Integer size);

    long unreadCount(Integer userId);

    void read(Integer id, User user);

    Notification findById(Integer id);
}
