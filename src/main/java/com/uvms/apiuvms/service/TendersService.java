// TendersService.java in service folder
package com.uvms.apiuvms.service;

import com.uvms.apiuvms.entity.Tenders;
import com.uvms.apiuvms.repository.TendersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TendersService {

    @Autowired
    private TendersRepository tendersRepository;

    public List<Tenders> getAllTenders() {
        return tendersRepository.findAll();
    }

    public Optional<Tenders> getTenderById(Integer id) {
        return tendersRepository.findById(id);
    }

    public Tenders saveTender(Tenders tender) {
        return tendersRepository.save(tender);
    }

    public void deleteTenderById(Integer id) {
        tendersRepository.deleteById(id);
    }

    // Additional business logic methods can be added here
}