package com.emocare.demo.service;

import com.emocare.demo.repository.SystemSettingRepository;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingsService {
    private final SystemSettingRepository repository;

    public SystemSettingsService(SystemSettingRepository repository) {
        this.repository = repository;
    }

    public int getInt(String key, int defaultValue) {
        return repository.findById(key).map(s -> Integer.parseInt(s.getValue())).orElse(defaultValue);
    }
}