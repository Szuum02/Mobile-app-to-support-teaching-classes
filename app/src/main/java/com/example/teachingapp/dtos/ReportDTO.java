package com.example.teachingapp.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDTO {
    private final Long groupId;
    private final String subject;
    private List<StudentReportDTO> studentReports;

    public ReportDTO(Long groupId, String subject) {
        this.groupId = groupId;
        this.subject = subject;
    }
}