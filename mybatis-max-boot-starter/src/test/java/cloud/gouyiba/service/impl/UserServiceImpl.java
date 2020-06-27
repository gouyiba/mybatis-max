package cloud.gouyiba.service.impl;

import cloud.gouyiba.core.service.impl.BaseServiceImpl;
import cloud.gouyiba.mapper.UserMapper;
import cloud.gouyiba.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper> implements UserService {
}
