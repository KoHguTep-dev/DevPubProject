package main.repository;

import main.model.entities.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface CaptchaCodeRepository extends JpaRepository<CaptchaCode, Integer> {
    CaptchaCode findBySecretCode(String secretCode);

    @Modifying
    @Query("delete from CaptchaCode c where c.time < :time")
    @Transactional
    void deleteOldCaptcha(Date time);
}
