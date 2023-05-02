package cn.edu.njupt.sctpan.wqms.repository;

import cn.edu.njupt.sctpan.wqms.model.Model;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ModelRepository extends Repository<Model, Integer> {
    public Model save(Model model);
    public List<Model> findModelByTargetOrderByRmseAsc(String target);
    public List<Model> findModelByTargetAndMethodOrderByRmseAsc(String target, String method);
    public void delete(Model model);
    @Query("select distinct method from Model m where m.target = ?1")
    public List<String> findAllModelsByTarget(String target);
    public Model findModelById(Integer id);
}
