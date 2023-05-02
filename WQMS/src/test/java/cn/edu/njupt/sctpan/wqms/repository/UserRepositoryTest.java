package cn.edu.njupt.sctpan.wqms.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void findUserById() {
        int id = 1;
        System.out.println(userRepository.findUserById(id));
    }

    @Test
    public void findUserByName() {

    }


}