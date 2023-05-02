package cn.edu.njupt.sctpan.wqms.repository;

import cn.edu.njupt.sctpan.wqms.model.Role;
import org.springframework.data.repository.Repository;

public interface RoleRepository extends Repository<Role, Integer> {
    public Role save(Role role);
    public Role findRoleByName(String name);
}
