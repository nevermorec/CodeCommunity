package com.community.community.dto;

import com.community.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Integer id;
    private Long gmtCreate;
    private Integer status;
    private User notifier;
    private Integer outerId;
    private String outerTitle;
    private Integer type;
    private String typeName;

}
