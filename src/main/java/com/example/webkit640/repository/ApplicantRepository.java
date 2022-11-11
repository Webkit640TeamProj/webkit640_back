package com.example.webkit640.repository;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    Member findByMember(int memberId);
    //Applicant findBySchool(String school);
    Applicant findBySchoolNum(String schoolNum);
    //Applicant findByMajor(String major);
    Applicant findByMemberId(int id);
    @Query("select t from t_applicant t where t.name = ?1")
    List<Applicant> findByName(String name);
    @Query("select t from t_applicant t where t.school = ?1")
    List<Applicant> findBySchool(String school);
    @Query("select t from t_applicant t where t.major = ?1")
    List<Applicant> findByMajor(String major);

    @Query("select (count(t) > 0) from t_applicant t where t.member.id = ?1")
    Boolean existsByMember_Id(int id);

    List<Applicant> findAllByCreateDateContaining(String yearMonth);

    @Query("select t from t_applicant t where t.name = ?1 and t.major = ?2 and t.school = ?3 and t.schoolNum = ?4")
    Applicant findByNameAndMajorAndSchoolAndSchoolNum(String name, String major, String school, String schoolNum);


}
