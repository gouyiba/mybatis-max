package cloub.gouyiba.service.impl;

import cloub.gouyiba.service.UserService;
import cloub.gouyiba.core.service.impl.BaseServiceImpl;
import cloub.gouyiba.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper> implements UserService {
}
