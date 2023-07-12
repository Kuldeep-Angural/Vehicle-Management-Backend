package com.navv.repository;

import com.navv.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer> {

    @Query(value = "select t from Token t inner join User u on t.user.id =u.id  where u.id=:userId and (t.expired=false or t.active=true)")
    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
