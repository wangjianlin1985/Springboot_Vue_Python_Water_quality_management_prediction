package cn.edu.njupt.sctpan.wqms.repository;

import cn.edu.njupt.sctpan.wqms.model.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserRepository extends Repository<User, Integer> {
    public User findUserByUsername(String username);
    public User findUserById(Integer id);
    public User save(User user);
    public List<User> findAll();
    public boolean existsUserByUsername(String username);
    public void deleteById(int id);
    public List<User> findByUsernameContaining(String username);
}
