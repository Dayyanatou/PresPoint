package com.fst.gestionpresencepointage.service;

import java.util.List;

public interface AbstractService <T>{
    Integer save(T dto);//permet de faire la modification et ajout
    List<T> findALL();
    T findById(Long id);
    void delete(Integer id);
}
