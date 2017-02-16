package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WxApp;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WxApp entity.
 */
@SuppressWarnings("unused")
public interface WxAppRepository extends JpaRepository<WxApp,Long> {

    @Query("select wxApp from WxApp wxApp where wxApp.user.login = ?#{principal.username}")
    List<WxApp> findByUserIsCurrentUser();

}
