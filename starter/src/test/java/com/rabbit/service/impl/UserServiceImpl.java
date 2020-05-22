package com.rabbit.service.impl;

import com.rabbit.core.service.impl.BaseServiceImpl;
import com.rabbit.mapper.UserMapper;
import com.rabbit.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper> implements UserService {
}
