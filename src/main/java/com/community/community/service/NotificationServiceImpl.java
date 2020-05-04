package com.community.community.service;

import com.community.community.dto.NotificationDTO;
import com.community.community.dto.PaginationDTO;
import com.community.community.enums.NotificationStatusEnum;
import com.community.community.enums.NotificationTypeEnum;
import com.community.community.mapper.NotificationMapper;
import com.community.community.mapper.QuestionMapper;
import com.community.community.mapper.UserMapper;
import com.community.community.model.Notification;
import com.community.community.model.NotificationExample;
import com.community.community.model.Question;
import com.community.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public long unreadCount(Integer userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    @Override
    public PaginationDTO<NotificationDTO> listByUserId(Integer userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO();
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        Integer totalCount = (int)notificationMapper.countByExample(example);
        Integer totalPage = (int)Math.ceil(totalCount/(double)size);

        if (page<1) page = 1;
        if (page>totalPage) page = totalPage;

        int offset = (page-1)*size;

        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));


        for (Notification notification : notifications) {
            // 通过userId查找user
            User user = userMapper.selectByPrimaryKey(notification.getNotifier());
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setNotifier(user);

            // 通过userId查找title
            Question question = questionMapper.selectByPrimaryKey(notification.getOuterId());
            String title = question.getTitle();
            notificationDTO.setOuterTitle(title);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));

            notificationDTOList.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOList);
        paginationDTO.setPagination(totalCount, page, size);
        return paginationDTO;
    }

    @Override
    public void read(Integer id, User user) {
        Notification notification = new Notification();
        notification.setId(id);
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKeySelective(notification);
    }

    @Override
    public Notification findById(Integer id) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        return notification;
    }
}
