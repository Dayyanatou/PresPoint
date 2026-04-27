package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.repository.UtilisateurRepository;
import com.fst.gestionpresencepointage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UtilisateurRepository repository;

    @Override
    public Integer save(Object dto) {
        return 0;
    }

    @Override
    public List findALL() {
        return List.of();
    }

    @Override
    public Object findById(Long id) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
