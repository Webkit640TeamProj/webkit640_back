package com.example.webkit640.repository;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
    @Query("select t from t_trainee t where t.applicant.id = ?1")
    Applicant findByApplicant_Id(int applicantId);

    Trainee findByCardinal(String cardinal);

    @Query("select t from t_trainee t where t.applicant.id = ?1")
    Trainee findApplicant_id(int applicantId);

}
