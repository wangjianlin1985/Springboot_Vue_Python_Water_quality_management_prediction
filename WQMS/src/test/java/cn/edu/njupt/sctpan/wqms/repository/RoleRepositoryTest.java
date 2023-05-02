package cn.edu.njupt.sctpan.wqms.repository;

import cn.edu.njupt.sctpan.wqms.model.Role;
import cn.edu.njupt.sctpan.wqms.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveRole() {
        Role role = new Role();
        role.setName("admin");
        User user = userRepository.findUserById(1);
        user.setRole(role);
        roleRepository.save(role);
        userRepository.save(user);
    }
}
