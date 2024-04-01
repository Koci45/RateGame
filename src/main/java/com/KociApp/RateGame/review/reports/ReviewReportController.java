package com.KociApp.RateGame.review.reports;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReviewReportController {

    private final ReviewReportService reviewReportService;

    @GetMapping
    public List<ReviewReport> getReports(){
        return reviewReportService.findAll();
    }

    @GetMapping("/byId/{id}")
    public ReviewReport getReportById(@PathVariable Long id){
        return reviewReportService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewReport createReport(@RequestBody ReviewReportRequest reviewReportRequest){
        return reviewReportService.save(reviewReportRequest);
    }

    @GetMapping("/raport")
    public List<ReviewReportRaport> raport(){
        return reviewReportService.raport();
    }
}
