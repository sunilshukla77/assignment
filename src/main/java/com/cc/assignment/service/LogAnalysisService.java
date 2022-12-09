package com.cc.assignment.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface LogAnalysisService {
    int analysisFile(InputStream is);
}
