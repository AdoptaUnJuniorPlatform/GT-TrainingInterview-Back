package com.example.demo.Repository;

import com.example.demo.model.Questionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionaryRepository extends JpaRepository<Questionary, Long> {

    List<Questionary> findByRoleAndExperienceAndTheme(String role, String experience, String theme);

}
